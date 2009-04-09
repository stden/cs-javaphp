<?php

  require_once(getServerPluginFile());

  function processGetContestDataRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    //get db connection
    $con = connectToDB();

    //get user_id or die, if session is invalid
    $userRow = testSession($request->sessionID);
    $user_id = $userRow['id']; 

    //authorize user for this operation
    // get contest ID
    $user_type = $userRow['user_type'];

    //compare requested contest and user contest
    $contest_id = getRequestedContest($request->contestID, $userRow['contest_id'], $user_type);

    if ($contest_id < 0) throwBusinessLogicError(0);

    //create response
    $res = new GetContestDataResponse();

    //fill contest description with data
    //query db
    $contest_rows = mysql_query(
                      sprintf("SELECT * FROM ${prfx}contest WHERE id=%s", Data::quote_smart($contest_id))
                    , $con) or throwServerProblem(15, mysql_error());
    $row = mysql_fetch_array($contest_rows) or throwBusinessLogicError(14);

    //TODO remove this code duplication, the code is simular to AvailableContests.php
    $c = @unserialize($row['settings']) or throwServerProblem(81);
    $c->contestID = (int)$row['id'];    

    $res->contest = $c;

    //fill problem data
    $res->problems = array();
    //get type of requested data
    $info_type = $request->infoType;
    $extended_data = $request->extendedData;
    //query db to find out problems
    $problems_rows = mysql_query(
                       sprintf("SELECT * FROM ${prfx}problem WHERE contest_id=%s ORDER BY contest_pos ASC", Data::quote_smart($contest_id))
                     , $con) or throwServerProblem(16, mysql_error());

    while ($row = mysql_fetch_array($problems_rows)) {
      $p = new ProblemDescription();

      $p->id = (int)$row['id'];
      $p->clientPluginAlias = $row['client_plugin_alias'];
      $p->serverPluginAlias = $row['server_plugin_alias'];
      $p->name = $row['name'];

      $p->statement = null;
      $p->statementData = null;
      $p->answerData = null;

      //load plugin
      require_once(getServerPluginFile($p->serverPluginAlias));      
      $plugin = new $p->serverPluginAlias ($con, $GLOBALS['dces_dir_problems'] . "/$p->id");

      //fill extended data: statement or statementData and answerData
      if (is_null($extended_data) || in_array($p->id,$extended_data)) {
        if ($info_type === "ParticipantInfo") {
          $statement = @unserialize($row['statement']) or throwServerProblem(83);
          //TODO process error: statement not found and return correct error info
          $p->statement = $plugin->getStatement($user_id, $statement);
        }
        elseif ($info_type === "AdminInfo") {
          if ($user_type === "Participant") throwBusinessLogicError(0);
          //TODO process error: statement not found and return correct error info
          $p->statementData = $plugin->getStatementData($p->id);
          $p->answerData = $plugin->getAnswerData($p->id);
        }
      }
      
      $res->problems[] = $p;
    }

    return $res;
  }

?>