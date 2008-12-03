<?php

  require_once("Utils.php");

  function processSubmitSolutionRequest($request) {
    //get db connection
    $con = connectToDB();

    //get user_id or die, if session is invalid
    $user_id = testSession($con, $request->sessionID);

    //authorize user for this operation
    // get contest ID
    $userRow = getUserRow($con, $user_id);

    $contest_id = getRequestedContest($request->contest->contestID, $userRow['contest_id'], $user_type);

    if ($contest_id < 0) throwError("You don't have permissions to submit solution for this contest");

    //get plugin_alias
    //TODO prevent SQL injection
    $problem_rows = mysql_query("SELECT plugin_alias FROM problem WHERE id='$requsest->problemID'", $con) or
                      die("DB error 21. ".mysql_error());
    if (! ($problem_row = mysql_fetch_array($problem_rows)) ) throwError("Problem with specified ID not found");

    $plugin_alias = $problem_row['server_plugin_alias'];

    //get plugin
    require_once('ServerPlugin.php');
    require_once($GLOBALS['dces_dir_server_plugins'] . '/' . $plugin_alias . '.php');

    $plugin = new $plugin_alias($con, $GLOBALS['dces_dir_problems'] . '/' . $requsest->problemID);

    //get answer data
    $answer_data = unserialize($problem_row['answer']) or die("DB error 22.");

    //get previous result
    $previous_result_query = mysql_query("SELECT result FROM task_result WHERE ORDER BY submission_time DESC")
                               or die("DB error 23. ".mysql_error());
    $previous_result_row = mysql_fetch_array($last_result_query);
    if (!$previous_result_row)
      $previous_result = null;
    else
      $previous_result = unserialize($previous_result_row['result']) or die("DB error 24.");

    //call plugin to check solution
    $check_result = $plugin->checksolution($request->problemResult, $user_id, $answer_data, $previous_result);

    //save submition result in db
    $serialized_submission = serialize($request->problemResult);
    $serialized_result = serialize($check_result);
    $cur_php_time = getdate();
    $cur_sql_time = DatePHPToMySQL($cur_php_time[0]);
    mysql_query("INSERT INTO task_result (contest_id, problem_id, user_id, submission, result, submission_time) " .
                "VALUES ($contest_id, $request->problemID, $user_id, '$serialized_submission', '$serialized_result', '$cur_time')")
           or die("DB error 25." . mysql_error());

    //return submission result
    $res = new SubmitSolutionResponse();
    $res->problemResult = $check_result;
  }

?>