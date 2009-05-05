<?php

  require_once(getServerPluginFile());

  //gets contents of $s, treats it as a zip, opens zip and returns it.
  //remove_handle is a value to be passed to closeZip() function
  function openZip($s, $zip_file) {
    if (!file_put_contents($zip_file, $s)) return false;
    $zip = new ZipArchive();
    $res = $zip->open($zip_file);
    if ($res === true)
      return $zip;
    else
      return false;
  }

  function queryForContestDescription($c, $contest_id, $con) {
    //get current contest settings
    $prfx = $GLOBALS['dces_mysql_prefix'];
    $rows = mysql_query("SELECT settings FROM ${prfx}contest WHERE id=$contest_id", $con) or throwServerProblem(84, mysql_error());
    $row = mysql_fetch_array($rows, $con) or throwBusinessLogicError(14);
    $settings = Data::_unserialize($row['settings']);

    //TODO make normal copy of settings

    //adjust name
    if (!is_null($c->name))
      $settings->name = $c->name;

    //adjust description
    if (!is_null($c->description))
      $settings->description = $c->description;

    //adjust start
    if (!is_null($c->start))
      $settings->start = $c->start;

    //adjust finish
    if (!is_null($c->finish))
      $settings->finish = $c->finish;

    //adjust registration type
    if (!is_null($c->registrationType)) {
      $settings->registrationType = $c->registrationType;
    }  

    //adjust result access policy
    if (!is_null($c->resultsAccessPolicy)) {
      if (!is_null($c->resultsAccessPolicy->contestPermission))
        $settings->resultsAccessPolicy->contestPermission = $c->resultsAccessPolicy->contestPermission;

      if (!is_null($c->resultsAccessPolicy->contestEndingPermission))
        $settings->resultsAccessPolicy->contestEndingPermission = $c->resultsAccessPolicy->contestEndingPermission;

      if (!is_null($c->resultsAccessPolicy->afterContestPermission))
        $settings->resultsAccessPolicy->afterContestPermission = $c->resultsAccessPolicy->afterContestPermission;
    }

    //adjust submission policy
    if (!is_null($c->contestTiming)) {
      $settings->contestTiming->selfContestStart = $c->contestTiming->selfContestStart;

      if ($c->contestTiming->maxContestDuration !== -1)
        $settings->contestTiming->maxContestDuration = $c->contestTiming->maxContestDuration;

      if ($c->contestTiming->contestEndingDuration !== -1)
        $settings->contestTiming->contestEndingFinish = $c->contestTiming->contestEndingFinish;

      if ($c->contestTiming->contestEndingStart !== -1)
        $settings->contestTiming->contestEndingStart = $c->contestTiming->contestEndingStart;                  
    }

    $col_value = array('settings' => @serialize($settings));
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
                  sprintf("SELECT server_plugin_alias FROM ${prfx}problem WHERE id=%s", Data::quote_smart($p->id))
                , $con) or throwServerProblem(12, mysql_error());
        $row = mysql_fetch_array($rows, $con) or throwBusinessLogicError(4);        
        $plugin_alias = $row['server_plugin_alias'];
      }
      elseif ($p->id == -1 && is_null($p->serverPluginAlias))
        throwBusinessLogicError(1);

      //get current server plugin
      //TODO improve security here            
      require_once(getServerPluginFile($plugin_alias));
              
      if ($p->id == -1) {
        //We are to create a directory for new problem
        //New dir is temporary because we don't know problem id 
        $temp = $GLOBALS['dces_dir_temp'] . '/' . RequestUtils::random_str(10);
        $temp_dirs[] = $temp;
        mkdir($temp);
        $plugin = new $plugin_alias($temp);
      }
      else //here plugin dir is made from the known problem id
        $plugin = new $plugin_alias($GLOBALS['dces_dir_problems'] . "/$p->id");

      //set statementData
      if (!is_null($p->statementData)) {
        if ($p->id == -1)
          $zip_file = $GLOBALS['dces_dir_temp'] . '/' . RequestUtils::random_str(10) . '.zip';
        else {
          $problem_id = $p->id;
          $zip_file = $GLOBALS['dces_dir_problems'] . "/${problem_id}_statement.zip";
        }
        $zip = openZip($p->statementData, $zip_file);
        if ($zip === false) throwBusinessLogicError(7);
        $data_updated = $plugin->updateStatementData($zip);
        if ($data_updated === false) throwBusinessLogicError(9);
        $col_value['statement'] = serialize($data_updated);
        $col_value['column_names'] = serialize($plugin->getColumnNames($data_updated));
        if ($p->id == -1)
          $temp_statement_zips[] = $zip_file;
      } else $all_set = false;

      //set answerData
      //TODO think abount code duplication. May be there is no need in two concepts: statement and answer 
      if (!is_null($p->answerData)) {
        if ($p->id == -1)
          $zip_file = $GLOBALS['dces_dir_temp'] . '/' . RequestUtils::random_str(10) . '.zip';
        else {
          $problem_id = $p->id;
          $zip_file = $GLOBALS['dces_dir_problems'] . "/${problem_id}_answer.zip";
        }
        $zip = openZip($p->answerData, $zip_file);
        if ($zip === false) throwBusinessLogicError(8);
        $data_updated = $plugin->updateAnswerData($zip);
        if ($data_updated === false) throwBusinessLogicError(10);
        $col_value['answer'] = serialize($data_updated);
        if ($p->id == -1)
          $temp_answer_zips[] = $zip_file;
      } else $all_set = false;

      //set contest position
      $col_value['contest_pos'] = $contest_pos ++;

      if ($p->id == -1)
      {
        //create new task
        if (!$all_set) throwBusinessLogicError(1);

        $queries[] = composeInsertQuery('problem', $col_value);
      }
      else
      {
        //update the task
        $queries[] = composeUpdateQuery('problem', $col_value, "id='$p->id'");
        $changed_probs[$p->id] = 1;
      }

    }

    //queries to remove problems
    $res = mysql_query(
             sprintf("SELECT id FROM ${prfx}problem WHERE contest_id=%s", Data::quote_smart($contest_id))
           , $con) or throwServerProblem(13, mysql_error());
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
    $userRow = RequestUtils::testSession($request->sessionID);
    $user_id = $userRow['id'];

    //authorize user for this operation
    // get contest ID
    $user_type = $userRow['user_type'];
        
    $contest_id = RequestUtils::getRequestedContest($request->contest->contestID, $userRow['contest_id'], $user_type);
    if ($user_type === "Participant") $contest_id = -1;

    if ($contest_id < 0) throwBusinessLogicError(0);

    //get elements to adjust
    $queries = array();

    //adjust contest description
    $query_1 = queryForContestDescription($request->contest, $contest_id, $con);    
    if ($query_1 != "")
        $queries[] = $query_1;

    //now adjust problems
    $temp_dirs = array(); // will contain set of created temporary plugin directories
    $queries_2 = array();
    if (!is_null($request->problems))
      $queries_2 = queriesToAdjustProblems($con, $request->problems, $contest_id, $temp_dirs, $temp_statement_zips, $temp_answer_zips);

    foreach ($queries_2 as $q)
      $queries[] = $q;

    $responseIDs = array();
    foreach ($request->problems as $p)
      $responseIDs[] = $p->id;
    $skipped_index = 0;

    //run transaction
    if (count($queries) != 0) {
      $inserted_ids = array();
      transaction($con, $queries, $inserted_ids) or throwServerProblem(60);

      //rename temporary dirs
      if ( count($temp_dirs) != count($inserted_ids) ||
           count($temp_dirs) != count($temp_statement_zips) ||
           count($temp_dirs) != count($temp_answer_zips) ) {
             throwServerProblem(61);
           }

      for ($i = 0; $i < count($temp_dirs); $i++) {
        $temp_dir = $temp_dirs[$i];
        $inserted_id = $inserted_ids[$i];
        $stat_zip = $temp_statement_zips[$i];
        $ans_zip = $temp_answer_zips[$i];
        @rename($temp_dir, $GLOBALS['dces_dir_problems'] . "/${inserted_id}") /*or do nothing*/;
        @rename($stat_zip, $GLOBALS['dces_dir_problems'] . "/${inserted_id}_statement.zip") /*or do nothing*/;
        @rename($ans_zip,  $GLOBALS['dces_dir_problems'] . "/${inserted_id}_answer.zip") /*or do nothing*/;

        //insert index into new problem ids list
        while ($skipped_index < count($responseIDs) && $responseIDs[$skipped_index] != -1)
            $skipped_index ++;
        if ($skipped_index >= count($responseIDs)) throwServerProblem(159);
        $responseIDs[$skipped_index] = $inserted_id;
      }
    }
    else
      throwBusinessLogicError(11);

    $response = new AdjustContestResponse();
    $response->problemIDs = $responseIDs;
    return $response;
  }
?>