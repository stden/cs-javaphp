<?php

  function processSubmitSolutionRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    //get db connection
    $con = connectToDB();

    //get user_id or die, if session is invalid
    $userRow = testSession($con, $request->sessionID);
    $user_id = testSession($con, $request->sessionID);

    //authorize user for this operation
    // get contest ID
    $user_type = $userRow['user_type'];

    $contest_id = getRequestedContest($request->contestID, $userRow['contest_id'], $user_type);

    if ($contest_id < 0) throwError("You don't have permissions to submit solution for this contest");

    //get plugin_alias
    $problem_rows = mysql_query(
                      sprintf("SELECT server_plugin_alias, answer FROM ${prfx}problem WHERE id=%s", quote_smart($request->problemID))
                    , $con) or die("DB error 21. ".mysql_error());
    if (! ($problem_row = mysql_fetch_array($problem_rows)) ) throwError("Problem with specified ID not found");

    $plugin_alias = $problem_row['server_plugin_alias'];

    //get plugin
    require_once('ServerPlugin.php');
    require_once($GLOBALS['dces_dir_server_plugins'] . '/' . $plugin_alias . '.php');

    $plugin = new $plugin_alias($con, $GLOBALS['dces_dir_problems'] . '/' . $requsest->problemID);

    //get answer data
    $answer_data = unserialize($problem_row['answer']) or die("DB error 22.");

    //get previous result
    $previous_result_query = mysql_query(
                               sprintf("SELECT result FROM ${prfx}task_result WHERE problem_id=%s AND user_id=%s ORDER BY submission_time DESC",
                                       quote_smart($request->problemID),
                                       quote_smart($user_id)
                               ), $con) or die("DB error 23. ".mysql_error());
    $previous_result_row = mysql_fetch_array($previous_result_query);
    if (!$previous_result_row)
      $previous_result = null;
    else
      $previous_result = unserialize($previous_result_row['result']) or die("DB error 24.");      

    //call plugin to check solution
    $check_result = $plugin->checkSolution($request->problemResult, $user_id, $answer_data, $previous_result);

    //save submition result in db
    $cur_php_time = getdate();

    $col_value = array();
    $col_value['problem_id'] = $request->problemID;
    $col_value['user_id'] = $user_id;
    $col_value['submission'] = serialize($request->problemResult);
    $col_value['result'] = serialize($check_result);
    $col_value['submission_time'] = DatePHPToMySQL($cur_php_time[0]);

    mysql_query(composeInsertQuery('task_result', $col_value), $con) or die("DB error 25." . mysql_error());

    //return submission result
    $res = new SubmitSolutionResponse();
    $res->problemResult = $check_result;

    return $res;
  }

?>