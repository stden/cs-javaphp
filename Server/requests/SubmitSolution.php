<?php

require_once 'utils/Problem.php';

/*
function setResultsColumns($user_row, $problem_id, $new_cols) {
    //get old results       
    $new_results = Data::_unserialize($user_row['results']);
    //set new columns for the problem    
    $new_results[$problem_id] = $new_cols;
    //compose query
    $q = Data::composeUpdateQuery('user', array('results' => serialize($new_results)), "id=${user_row['id']}");
    //put query to the queue
    Data::submitModificationQuery($q);
}
*/

function getSetting($contest_setting, $problem_setting) {
    return is_null($problem_setting) ? $contest_setting : $problem_setting;
}

function processSubmitSolutionRequest($request) {
    $prfx = DB_PREFIX;

    //get user_id or die, if session is invalid
    $userRow = RequestUtils::testSession($request->sessionID);
    $user_id = $userRow['id'];

    //authorize user for this operation
    // get contest ID
    $user_type = $userRow['user_type'];

    //get problem row
    $problem_row = Data::getRow(
        sprintf("SELECT * FROM ${prfx}problem WHERE id=%s", Data::quote_smart($request->problemID))
    );
    if (!$problem_row)
        throwBusinessLogicError(4);

    //get contest id of a problem
    $problem_contest_id = $problem_row['contest_id'];

    //test if we have rights to submit solution for the contest
    $contest_id = RequestUtils::getRequestedContest($problem_contest_id, $userRow['contest_id'], $user_type);

    $contest_settings = $userRow['settings'];

    if ($contest_id < 0)
        throwBusinessLogicError(0);

    //get problem
    $problem = new Problem(getProblemFile($request->problemID));

    $problem_settings = Data::_unserialize($problem_row['contest_settings']);

    //get plugin_alias
    $plugin_alias = $problem->getServerPlugin();

    //get plugin
    require_once(getServerPluginFile());
    require_once(getServerPluginFile($plugin_alias));

    $plugin = new $plugin_alias($problem);

    //get submissions history
    $hist = Data::getRow(
        sprintf(
            "SELECT COUNT(*) AS cnt FROM ${prfx}submission_history WHERE (problem_id=%s) AND (user_id=%s)",
            Data::quote_smart($request->problemID),
            Data::quote_smart($user_id)
        )
    );

    //test that not all submission attempts were used
    if ($hist >= getSetting($contest_settings->problemsDefaultSettings->sendCount, $problem_settings->sendCount))
        throwBusinessLogicError(21);

    //call plugin to check solution
    $check_result = $plugin->checkSolution($request->problemResult);

    //save submition result in history
    $cur_php_time = getdate();

    $col_value = array();
    $col_value['problem_id'] = $request->problemID;
    $col_value['user_id'] = $user_id;
    $col_value['submission'] = serialize($request->problemResult);
    $col_value['result'] = serialize($check_result);
    $col_value['submission_time'] = DatePHPToMySQL($cur_php_time[0]);

    Data::submitModificationQuery(Data::composeInsertQuery('submission_history', $col_value));

    //return submission result
    $res = new SubmitSolutionResponse();
    $res->problemResult = $check_result;

    return $res;
}

?>