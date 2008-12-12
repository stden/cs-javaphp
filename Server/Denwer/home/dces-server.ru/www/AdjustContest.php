<?php

  require_once("ServerPlugin.php");  

  function queryForContestDescription($c, $contest_id) {
    $col_value = array();

    //adjust name
    if (!is_null($c->name))
      $col_value["name"] = $c->name;      

    //adjust description
    if (!is_null($c->description))
      $col_value["description"] = $c->description;

    //adjust start
    if (!is_null($c->start)) {
      $date = DatePHPToMySQL($c->start);
      $col_value["start_time"] = $date;      
    }

    //adjust finish
    if (!is_null($c->finish)) {
      $date = DatePHPToMySQL($c->finish);
      $col_value["finish_time"] = $date;
    }

    //adjust registration type
    if (!is_null($c->registrationType))
      $col_value["reg_type"] = $c->registrationType;

    return composeUpdateQuery("contest", $col_value, "id=$contest_id");
  }

  function queriesToAdjustProblems($con, $problems, $contest_id, &$temp_dirs, &$temp_statement_zips, &$temp_answer_zips) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    $changed_probs = array(); //problems that will be changed by request
    $queries = array();

    $contest_pos = 1;
    foreach ($problems as $p) {
      $col_value = array(); 
      $all_set = true;

      //set contest id
      $col_value['contest_id'] = $contest_id;

      //set client plugin
      if (!is_null($p->clientPluginAlias)) {
        $col_value['client_plugin_alias'] = $p->clientPluginAlias;
      } else $all_set = false;

      //set server plugin
      if (!is_null($p->serverPluginAlias)) {
        $col_value['server_plugin_alias'] = $p->serverPluginAlias;
      } else $all_set = false;

      //set name
      if (!is_null($p->name)) {
        $col_value['name'] = $p->name;
      } else $all_set = false;

      //skip statement value

      //get current server plugin alias
      if ( !is_null($p->serverPluginAlias) )
        $plugin_alias = $p->serverPluginAlias;
      if ($p->id != -1 && is_null($p->serverPluginAlias) ) {
        $rows = mysql_query(
                  sprintf("SELECT server_plugin_alias FROM ${prfx}problem WHERE id=%s", quote_smart($p->id))
                , $con) or die("DB error 12".mysql_error());
        $row = mysql_fetch_array($rows, $con) or throwError("Problem with specified ID not found");        
        $plugin_alias = $row['server_plugin_alias'];
      }
      elseif ($p->id == -1 && is_null($p->serverPluginAlias)) throwError("Server plugin not specified in the task being created");

      //get current server plugin
      //TODO improve security here
      $server_plugin_file = $GLOBALS['dces_dir_server_plugins'] . '/' . $plugin_alias . '.php';
      if (!file_exists($server_plugin_file)) throwError("Server plugin '$plugin_alias' not found");
      require_once($server_plugin_file);
              
      if ($p->id == -1) {
        //We are to create a directory for new problem
        //New dir is temporary because we don't know problem id 
        $temp = $GLOBALS['dces_dir_temp'] . '/' . random_str(10);
        $temp_dirs[] =  $temp;
        mkdir($temp);
        $plugin = new $plugin_alias($con, $temp);
      }
      else //here plugin dir is made from the known problem id
        $plugin = new $plugin_alias($con, $GLOBALS['dces_dir_problems'] . "/$p->id");

      //set statementData
      if (!is_null($p->statementData)) {
        if ($p->id == -1)
          $zip_file = $GLOBALS['dces_dir_temp'] . '/' . random_str(10) . '.zip';
        else {
          $problem_id = $p->id;
          $zip_file = $GLOBALS['dces_dir_problems'] . "/${problem_id}_statement.zip";
        }
        $zip = openZip($p->statementData, $zip_file);
        if ($zip === false) throwError("Statement data seems to be not a zip compressed set of files");
        $data_updated = $plugin->updateStatementData($zip);
        if ($data_updated === false) throwError('Server plugin did not accept statement data');
        $col_value['statement'] = serialize((string)$data_updated);        
        if ($p->id == -1)
          $temp_statement_zips[] = $zip_file;
      } else $all_set = false;

      //set answerData
      //TODO think abount code duplication. May be there is no need in two concepts: statement and answer 
      if (!is_null($p->answerData)) {
        if ($p->id == -1)
          $zip_file = $GLOBALS['dces_dir_temp'] . '/' . random_str(10) . '.zip';
        else {
          $problem_id = $p->id;
          $zip_file = $GLOBALS['dces_dir_problems'] . "/${problem_id}_answer.zip";
        }
        $zip = openZip($p->answerData, $zip_file);
        if ($zip === false) throwError("Answer data seems to be not a zip compressed set of files");
        $data_updated = $plugin->updateAnswerData($zip);
        if ($data_updated === false) throwError('Server plugin did not accept answer data');
        $col_value['answer'] = serialize((string)$data_updated);
        if ($p->id == -1)
          $temp_answer_zips[] = $zip_file;
      } else $all_set = false;

      //set contest position
      $col_value['contest_pos'] = $contest_pos ++;

      if ($p->id == -1)
      {
        //create new task
        if (!$all_set) throwError("An attempt to insert a new Problem with not all parameters set");

        $queries[] = composeInsertQuery('problem', $col_value);
      }
      else
      {
        //update a task
        $queries[] = composeUpdateQuery('problem', $col_value, "id='$p->id'");
        $changed_probs[$p->id] = 1;
      }

    }

    //queries to remove problems
    $res = mysql_query(
             sprintf("SELECT id FROM ${prfx}problem WHERE contest_id=%s", quote_smart($contest_id))
           , $con) or die("DB error 13: ".mysql_error());
    while ($row = mysql_fetch_array($res))
      if ($changed_probs[$row['id']] != 1) {
        $pid = $row['id']; 
        $queries[] = "DELETE FROM problem WHERE id='$pid'";
      }

    return $queries;
  }

  function processAdjstContestRequest($request) {
    //get db connection
    $con = connectToDB();

    //get user_id or die, if session is invalid
    $userRow = testSession($con, $request->sessionID);
    $user_id = $userRow['id'];

    //authorize user for this operation
    // get contest ID
    $user_type = $userRow['user_type'];
        
    $contest_id = getRequestedContest($request->contest->contestID, $userRow['contest_id'], $user_type);
    if ($user_type === "Participant") $contest_id = -1;

    if ($contest_id < 0) throwError("You don't have permissions to adjust this contest");

    //get elements to adjust
    $queries = array();

    //adjust contest description
    $query_1 = queryForContestDescription($request->contest, $contest_id);
    if ($query_1 != "")
        $queries[] = $query_1;

    //now adjust problems
    $temp_dirs = array(); // will contain set of created temporary plugin directories
    $queries_2 = array();
    if (!is_null($request->problems))
      $queries_2 = queriesToAdjustProblems($con, $request->problems, $contest_id, $temp_dirs, $temp_statement_zips, $temp_answer_zips);

    foreach ($queries_2 as $q)
      $queries[] = $q;

    //run transaction
    if (count($queries) != 0) {
      $inserted_ids = array();
      transaction($con, $queries, $inserted_ids) or die("Failed to make update, db error or incorrect data");

      //server 2003 enterprise ru 32 SP2

      //rename temporary dirs
      if ( count($temp_dirs) != count($inserted_ids) ||
           count($temp_dirs) != count($temp_statement_zips) ||
           count($temp_dirs) != count($temp_answer_zips) ) {
             var_dump($temp_dirs);
             var_dump($inserted_ids);
             var_dump($temp_statement_zips);
             var_dump($temp_answer_zips);
             die("Assertion failed, call developers");
           }

      for ($i = 0; $i < count($temp_dirs); $i++) {
        $temp_dir = $temp_dirs[$i];
        $inserted_id = $inserted_ids[$i];
        $stat_zip = $temp_statement_zips[$i];
        $ans_zip = $temp_answer_zips[$i];
        @rename($temp_dir, $GLOBALS['dces_dir_problems'] . "/${inserted_id}") /*or do nothing*/;
        @rename($stat_zip, $GLOBALS['dces_dir_problems'] . "/${inserted_id}_statement.zip") /*or do nothing*/;
        @rename($ans_zip,  $GLOBALS['dces_dir_problems'] . "/${inserted_id}_answer.zip") /*or do nothing*/;
      }
    }
    else
      throwError("Nothing updated by request");

    return new AcceptedResponse();
  }
?>