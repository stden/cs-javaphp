<?php

require_once 'utils/Problem.php';

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

    if ($contest_id < 0)
        throwBusinessLogicError(0);

    //get all settings
    $contest_settings = $userRow['settings'];

    $problem_settings = Data::_unserialize($problem_row['contest_settings']);

    //test that not all submission attempts were used    
    $hist = Data::getRow(
        sprintf(
            "SELECT COUNT(*) AS cnt FROM ${prfx}submission_history WHERE (problem_id=%s) AND (user_id=%s)",
            Data::quote_smart($request->problemID),
            Data::quote_smart($user_id)
        )
    );

    if ($hist >= getSetting($contest_settings->problemsDefaultSettings->sendCount, $problem_settings->sendCount))
        throwBusinessLogicError(21);

    //save submission result in history
    $cur_php_time = getdate();

    $col_value = array();
    $col_value['problem_id'] = $request->problemID;
    $col_value['user_id'] = $user_id;
    $col_value['submission'] = serialize($request->problemResult);
    $col_value['result'] = null;//serialize($check_result);
    $col_value['submission_time'] = DatePHPToMySQL($cur_php_time[0]);

    //TODO implement plugin that run asynchronously
    //synchronous plugin

    //get problem and create plugin
    $problem = new Problem(getProblemFile($request->problemID));
    $plugin_alias = $problem->getServerPlugin();
    require_once(getServerPluginFile());
    require_once(getServerPluginFile($plugin_alias));
    $plugin = new $plugin_alias($problem);

    $last_result = $plugin->checkSolution(Data::getInsertedID(), $request->problemResult);
    $col_value['result'] = serialize($last_result);

    //update results table
    $choice = getSetting($contest_settings->problemsDefaultSettings->tableResultChoice, $problem_settings->tableResultChoice);
    $user_results = Data::_unserialize($userRow['results']);
    if (!isset($user_results[$request->problemID])) {
        $user_results[$request->problemID] = array();
    }

    //get new result
    $result_changed = false;

    if ($choice === 'Last') {
        $new_result = $last_result;
        $result_changed = true;
    } else { // $choice === 'Best'
        if (isset($user_results[$request->problemID]['r'])) {
            if ($plugin->compareResults($last_result, $user_results[$request->problemID]['r']) === 1) {
                $new_result = $last_result;
                $result_changed = true;
            }
        } else {
            $new_result = $last_result;
            $result_changed = true;
        }
    }

    if ($result_changed) {
        $user_results[$request->problemID]['r'] = $new_result;
        require_once 'utils/SubmissionHistoryUtils.php';
        $user_results[$request->problemID]['rt'] = SubmissionHistoryUtils::convertToTable(
            $new_result,
            getSetting($contest_settings->problemsDefaultSettings->resultTransition, $problem_settings->resultTransition)
        );
    }

    Data::submitModificationQuery(Data::composeInsertQuery('submission_history', $col_value));
    Data::submitModificationQuery(Data::composeUpdateQuery(
        'user', array('results' => serialize($user_results)), "id=$user_id"
    ));

    //return submission result
    $res = new AcceptedResponse();

    return $res;
}

?>