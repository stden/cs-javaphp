<?php

  require_once("ServerPlugin.php");
  require_once("Utils.php");

  function processGetContestDataRequest($request) {
    //get db connection
    $con = connectToDB();

    //get user_id or die, if session is invalid
    $user_id = testSession($con, $request->sessionID);

    //authorize user for this operation
    // get contest ID
    $userRow = getUserRow($con, $user_id);
    $user_type = $userRow['user_type'];

    //compare requested contest and user contest
    //TODO by the way this is a code duplication with AdjustContest.php
    $contest_id = getRequestedContest($request->contest->contestID, $userRow['contest_id'], $user_type);

    if ($contest_id < 0) throwError("You don't have permissions to get data from this contest");

    //create response
    $res = new GetContestDataResponse();

    //create contest description
    $c = new ContestDescription();

    //fill contest description with data
    //query db
    //TODO prevent SQL injection
    $contest_rows = mysql_query("SELECT * FROM contest WHERE id=$contest_id", $con) or die("DB error 15: ".mysql_error());
    $row = mysql_fetch_array($contest_rows) or throwError("Found no contest with specified id");

    //TODO remove this code duplication, the code is simular to AvailableContests.php
    $c->contestID = (int)$row['id'];
    $c->name = $row['name'];
    $c->description = $row['description'];
    $c->start = DateMySQLToPHP($row['start_time']);
    $c->finish = DateMySQLToPHP($row['finish_time']);
    $c->registrationType = $row['reg_type'];
    $c->data = unserialize($row['user_data']) or die("DB error 20");
    $c->compulsory = unserialize($row['user_data_compulsory']) or die("DB error 21");

    $res->contest = $c;

    //fill problem data
    $res->problems = array();
    //get type of requested data
    $info_type = $request->infoType;
    $extended_data = $request->extendedData;
    //query db to find out problems
    $problems_rows = mysql_query("SELECT * FROM problem WHERE contest_id=$contest_id ORDER BY contest_pos ASC", $con)
                     or die("DB error 16: ".mysql_error());

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
      require_once($GLOBALS['dces_dir_server_plugins'] . '/' . $p->serverPluginAlias . '.php');      
      $plugin = new $p->serverPluginAlias ($con, $GLOBALS['dces_dir_problems'] . "/$p->id");

      //fill extended data: statement or statementData and answerData
      if (is_null($extended_data) || in_array($p->id,$extended_data)) {
        if ($info_type === "ParticipantInfo") {
          $statement = unserialize($row['statement']) or die("DB error 17: ".mysql_error());
          $p->statement = $plugin->getStatement($user_id, $statement);
        }
        elseif ($info_type === "AdminInfo") {
          if ($user_type === "Participant") throwError("You don't have permissions to get admin info for a problem");
          $p->statementData = $plugin->getStatementData($p->id);
          $p->answerData = $plugin->getAnswerData($p->id);
        }
      }
      
      $res->problems[] = $p;
    }

    return $res;
  }

?>