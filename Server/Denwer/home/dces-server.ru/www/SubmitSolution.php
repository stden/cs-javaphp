<?php

  function processSubmitSolutionRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    //get db connection
    $con = connectToDB();

    //get user_id or die, if session is invalid
    $userRow = testSession($con, $request->sessionID);
    $user_id = $userRow['id'];

    //authorize user for this operation
    // get contest ID
    $user_type = $userRow['user_type'];

    //get problem row
    $problem_rows = mysql_query(
                      sprintf("SELECT * FROM ${prfx}problem WHERE id=%s", quote_smart($request->problemID))
                    , $con) or throwServerProblem(21, mysql_error());
    if (! ($problem_row = mysql_fetch_array($problem_rows)) ) throwBusinessLogicError(4);

    //get contest id of a problem
    $problem_contest_id = $problem_row['contest_id'];

    //test if we have rights to submit solution for the contest
    $contest_id = getRequestedContest($problem_contest_id, $userRow['contest_id'], $user_type);

    if ($contest_id < 0) throwBusinessLogicError(0);

    //get plugin_alias
    $plugin_alias = $problem_row['server_plugin_alias'];

    //get plugin
    require_once('ServerPlugin.php');
    require_once($GLOBALS['dces_dir_server_plugins'] . '/' . $plugin_alias . '.php');

    $plugin = new $plugin_alias($con, $GLOBALS['dces_dir_problems'] . '/' . $request->problemID);

    //get answer data
    $answer_data = @unserialize($problem_row['answer']) or throwServerProblem(22);

    //get previous result
    $problem_status_query = mysql_query(
                               sprintf("SELECT * FROM ${prfx}problem_status WHERE problem_id=%s AND user_id=%s",
                                       quote_smart($request->problemID),
                                       quote_smart($user_id)
                               ), $con) or throwServerProblem(23, mysql_error());
    $problem_status_row = mysql_fetch_array($problem_status_query);

    if (!$problem_status_row) {
      $current_result = null;
      $current_cols = array();      
      $do_status_update = false;
    }
    else {
      $current_result = @unserialize($problem_status_row['status']) or $current_result = null;
      $current_cols = @unserialize($problem_status_row['columns']) or $current_cols = array();
      $do_status_update = true;
    }

    //call plugin to check solution
    //public function checkSolution($solution, $user_id, $answer_data, &$current_result, &$table_cols) {
    $check_result = $plugin->checkSolution($request->problemResult, $user_id, $answer_data, $current_result, $current_cols);

    //save submition result in db
    $cur_php_time = getdate();

    $update_queries = array();

    $col_value = array();
    $col_value['problem_id'] = $request->problemID;
    $col_value['user_id'] = $user_id;
    $col_value['submission'] = serialize($request->problemResult);
    $col_value['result'] = serialize($check_result);
    $col_value['submission_time'] = DatePHPToMySQL($cur_php_time[0]);

    $update_queries[] = composeInsertQuery('submission_history', $col_value);

    $col_value = array();
    $col_value['problem_id'] = $request->problemID;
    $col_value['user_id'] = $user_id;
    $col_value['status'] = serialize($current_result);
    $col_value['columns'] = serialize($current_cols);

    if ($do_status_update) {
      $where = sprintf("problem_id=%s AND user_id=%s", $request->problemID, $user_id);
      $update_queries[] = composeUpdateQuery('problem_status', $col_value, $where);
    }
    else
      $update_queries[] = composeInsertQuery('problem_status', $col_value);

    //function transaction($con, $queries, &$inserted_ids = null) {
    transaction($con, $update_queries) or throwServerProblem(25, mysql_error());

    //return submission result
    $res = new SubmitSolutionResponse();
    $res->problemResult = $check_result;

    return $res;
  }

?>