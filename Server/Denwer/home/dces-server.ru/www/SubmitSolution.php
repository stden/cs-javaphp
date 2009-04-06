<?php

  function setResultsColumns($user_row, $problem_id, $new_cols) {
    //get old results
    $new_results = @unserialize($user_row['results']) or throwServerProblem(105);
    //set new columns for the problem
    $new_results[problem_id] = $new_cols;
    //compose query
    $q = composeUpdateQuery('user', array('results' => $new_results), "id=${user_row['id']}");
    //put query to the queue
    Data::submitModificationQuery($q);
  }

  function processSubmitSolutionRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];    

    //get user_id or die, if session is invalid
    $userRow = testSession($request->sessionID);
    $user_id = $userRow['id'];

    //authorize user for this operation
    // get contest ID
    $user_type = $userRow['user_type'];

    //get problem row
    $problem_row = Data::getRow(
                      sprintf("SELECT * FROM ${prfx}problem WHERE id=%s", quote_smart($request->problemID))
                    );
    if ( !$problem_row ) throwBusinessLogicError(4);

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

    $plugin = new $plugin_alias($GLOBALS['dces_dir_problems'] . '/' . $request->problemID);

    //get answer data
    $answer_data = @unserialize($problem_row['answer']) or throwServerProblem(22);

    //get previous result
    $problem_status_row = Data::getRow(
                               sprintf("SELECT * FROM ${prfx}problem_status WHERE problem_id=%s AND user_id=%s",
                                       quote_smart($request->problemID),
                                       quote_smart($user_id)
                               ));

    if (!$problem_status_row) {
      $current_result = null;
      $do_status_update = false;
    }
    else {
      $current_result = @unserialize($problem_status_row['status']) or $current_result = null;
      $do_status_update = true;
    }
    $current_cols = null;

    //call plugin to check solution
    //public function checkSolution($solution, $user_id, $answer_data, &$current_result, &$table_cols) {
    $check_result = $plugin->checkSolution($request->problemResult, $user_id, $answer_data, $current_result, $current_cols);

    //if result columns changed, set them
    if (!is_null($current_cols))
        setResultsColumns($userRow, $request->problemID, $current_cols);    

    //save submition result in db
    $cur_php_time = getdate();

    $col_value = array();
    $col_value['problem_id'] = $request->problemID;
    $col_value['user_id'] = $user_id;
    $col_value['submission'] = serialize($request->problemResult);
    $col_value['result'] = serialize($check_result);
    $col_value['submission_time'] = DatePHPToMySQL($cur_php_time[0]);

    Data::submitModificationQuery(composeInsertQuery('submission_history', $col_value));

    $col_value = array();
    $col_value['problem_id'] = $request->problemID;
    $col_value['user_id'] = $user_id;
    $col_value['status'] = serialize($current_result);
    $col_value['columns'] = serialize($current_cols);

    if ($do_status_update) {
      $where = sprintf("problem_id=%s AND user_id=%s", $request->problemID, $user_id);
      Data::submitModificationQuery(composeUpdateQuery('problem_status', $col_value, $where));
    }
    else
      Data::submitModificationQuery(composeInsertQuery('problem_status', $col_value));    

    //return submission result
    $res = new SubmitSolutionResponse();
    $res->problemResult = $check_result;

    return $res;
  }

?>