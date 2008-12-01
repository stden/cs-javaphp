<?php

  require_once("ServerPlugin.php");

  function queryForContestDescription($c, $contest_id) {
    //TODO prevent sql injection    
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

  function queriesToAdjustProblems($con, $problems, $contest_id, &$temp_dirs) {
    $changed_probs = array();
    $queries = array();

    foreach ($problems as $p) {
      $col_value = array(); 
      $all_set = true;

      //set contest id
      $col_value['contest_id'] = $contest_id;

      //set client plugin
      if (!is_null($p->clientPluginID)) {
        //lookup client plugin
        $rows = mysql_query("SELECT id FROM client_plugin WHERE alias='$p->clientPluginID'", $con)
                or die("DB error 10".mysql_error());
        $client_plugin_row = mysql_fetch_array($rows) or throwError("Client plugin id not found");

        $col_value['client_plugin'] = $client_plugin_row['id']; 
      } else $all_set = false;

      //set server plugin
      if (!is_null($p->serverPluginID)) {
        //lookup server plugin
        $rows = mysql_query("SELECT id FROM server_plugin WHERE alias='$p->serverPluginID'", $con)
                or die("DB error 11".mysql_error());
        $server_plugin_row = mysql_fetch_array($rows) or throwError("Server plugin id not found");

        $col_value['server_plugin'] = $server_plugin_row['id'];
      } else $all_set = false;

      //set name
      if (!is_null($p->name)) {
        $col_value['name'] = $p->name;
      } else $all_set = false;

      //skip statement value

      //get current server plugin alias
      if (! is_null($p->serverPluginID) ) {
        $plugin_alias = $p->serverPluginID;
      }
      elseif ($p->id != -1 && is_null($server_plugin_row)) {
        $row = mysql_fetch_array($rows, $con) or throwError("Problem with specified ID not found");
        $rows = mysql_query("SELECT alias FROM problem INNER JOIN server_plugin ON problem.server_plugin_id=server_plugin.id WHERE problem.id=$p->id", $con)
                or die("DB error 12".mysql_error());
        $plugin_alias = $row['alias'];
      }
      elseif ($p->id == -1 && is_null(server_plugin_row)) throwError("Server plugin not specified in new creating task");

      //get current server plugin
      require_once("server_plugins/$plugin_alias.php");
      if ($p->id == -1) {
        $temp = random_str(10);
        $temp_dirs[] = $temp;
        $plugin = new $plugin_alias($con, "problems/$temp");
      }
      else
        $plugin = new $plugin_alias($con, "problems/$p->id");

      //set statementData
      if (!is_null($p->statementData)) {
        $handle = null;
        $zip = openZip($p->statementData, $handle);
        //if (!$zip) throwError("Statement data seems to be not a zip compressed set of files");
        //$col_value['statement'] = $plugin->updateStatementData($zip);
        $col_value['statement'] = 'this is a statement';
        closeZip($handle);
        //if (!$col_value['statement']) throwError('Server plugin did not accept statement data');
      } else $all_set = false;

      //set answerData
      if (!is_null($p->answerData)) {
        $handle = null;
        $zip = openZip($p->answer, $handle);
        //if (!$zip) throwError("Answer data seems to be not a zip compressed set of files");
        //$col_value['answer'] = $plugin->updateStatementData($zip);
        $col_value['statement'] = 'this is an answer';
        closeZip($handle);
        //if (!$col_value['answer']) throwError('Server plugin did not accept answer data');
      } else $all_set = false;

      if ($p->id == -1)
      {
        //create new task
        if (!all_set) throwError("An attempt to insert a new Problem with not all parameters set");

        $queries[] = composeInsertQuery('problem', $col_value);
      }
      else
      {
        //adjust a task
        $queries[] = composeUpdateQuery('problem', $col_value, "id='$p->id'");
        $changed_probs[$p->id] = 1;
      }

    }

    //removing queries
    $res = mysql_query("SELECT id FROM problem WHERE contest_id=$contest_id", $con) or die("DB error 13: ".mysql_error());
    while ($row = mysql_fetch_array)
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
    $user_id = testSession($con, $request->sessionID);

    //authorize user for this operation
    // get contest ID
    $userRow = getUserRow($con, $user_id);
    $user_type = $userRow['user_type'];
        
    $requested_contest_id = $request->contest->contestID;
    $user_contest_id = $userRow['contest_id'];

    $contest_id = -1;
    if ($requested_contest_id < 0 && $user_contest_id != 0)
      $contest_id = $user_contest_id;
    elseif ($requested_contest_id == $user_contest_id && $user_contest_id != 0)
      $contest_id = $user_contest_id;
    elseif ($requested_contest_id != $user_contest_id && $user_type == "SuperAdmin")
      $contest_id = $requested_contest_id;

    if ($user_type == "Participant") $contest_id = -1;            

    if ($contest_id < 0) throwError("You don't have permissions to adjust this contest");

    //get elements to adjust
    $queries = array();

    //adjust contest description
    $query_1 = queryForContestDescription($request->contest, $contest_id);
    if ($query_1 != "")
        $queries[] = $query_1;

    //now adjust problems
    $temp_dirs = array();
    if (!is_null($request->problems))
      $queries += queriesToAdjustProblems($con, $request->problems, $contest_id, $temp_dirs);

    //run transaction
    if (count($queries) != 0) {
      $inserted_ids = array();
      transaction($con, $queries, $inserted_ids) or die("Failed to make update, db error or incorrect data");

      //rename temporary dirs
      if (count($temp_dirs) != count($inserted_ids)) die("Assertion failed, call developers");

      for ($i = 0; $i < count($temp_dirs); $i++) {
        $temp_dir = $temp_dirs[$i];
        $inserted_id = $inserted_ids[$i];
        rename("problems/$temp_dir", "problems/$inserted_id") /*or do nothing*/;
      }
    }
    else
      throwError("Nothing updated by request");

    return new AcceptedResponse();
  }
?>