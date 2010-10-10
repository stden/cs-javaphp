<?php

  require_once(getServerPluginFile());
  require_once('utils/Problem.php');

  function processGetContestDataRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];    

    $is_anonymous = is_null($request->sessionID);
    if (!$is_anonymous) {
        //get user_id or die, if session is invalid
        $userRow = RequestUtils::testSession($request->sessionID);
        $user_id = $userRow['id'];

        //authorize user for this operation
        // get contest ID
        $user_type = $userRow['user_type'];

        //compare requested contest and user contest
        $contest_id = RequestUtils::getRequestedContest($request->contestID, $userRow['contest_id'], $user_type);
    }
    else
    {
        $contest_id = $request->contestID;
    }
    if ($contest_id <= 0) throwBusinessLogicError(0);

    //create response
    $res = new GetContestDataResponse();

    //fill contest description with data
    //query db    
    $row = Data::getRow(
                      sprintf("SELECT * FROM ${prfx}contest WHERE id=%s", Data::quote_smart($contest_id))
                    ) or throwBusinessLogicError(14);

    //TODO remove this code duplication, the code is simular to AvailableContests.php
    $c = Data::_unserialize($row['settings']);
    $c->contestID = (int)$row['id'];
    $res->contest = $c;

    //fill problem data
    $res->problems = array();

    if ($is_anonymous) return $res;
                                  
    //get type of requested data
    $info_type = $request->infoType;
    $extended_data = $request->extendedData;
    //query db to find out problems
    $problems_rows = Data::getRows(
                       sprintf("SELECT * FROM ${prfx}problem WHERE contest_id=%s ORDER BY contest_pos ASC", Data::quote_smart($contest_id))
                     );

    while ($row = Data::getNextRow($problems_rows)) {
      $pd = new ProblemDescription();
      $pd->id = (int)$row['id'];
      $problem = new Problem(getProblemFile($pd->id));
     
      //load plugin
      $pluginName = $problem->getServerPlugin();
      require_once(getServerPluginFile($pluginName));      
      $plugin = new $pluginName ($problem);
      
      $pd->settings = Data::_unserialize($row['contest_settings']);

      //fill extended data: statement or statementData and answerData
      if ((!is_null($extended_data) && in_array($pd->id, $extended_data)) || is_null($extended_data)) {
        if ($info_type === "ParticipantInfo")          
          $pd->problem = $problem->getParticipantVersion($user_id)->getProblemBytes();
        elseif ($info_type === "AdminInfo") {
          if ($user_type === "Participant") throwBusinessLogicError(0);          
          $pd->problem = $problem->getProblemBytes();
        }
      }
      
      $res->problems[] = $pd;
    }

    return $res;
  }

?>