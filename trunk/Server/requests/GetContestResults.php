<?php
/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 21:56:14
 */

function getTableRow($user_row, $is_admin, $problem_ids, $problem_cols_sizes, $user_data_cols) {
    //fill id and login
    $row = array();
    if ($is_admin)
        $row[] = array($user_row['id'], $user_row['login']);

    //fill user data columns
    $data_cols = array();
    $ind = 0;
    $user_data = Data::_unserialize($user_row['user_data']);
    foreach ($user_data_cols as $ud) {        
        if ($is_admin || $ud->showInResult)
            $data_cols[] = $user_data[$ind];
        $ind++;
    }
    $row[] = $data_cols;

    //fill problem results
    $results = Data::_unserialize($user_row['results']);
    $ind = 0;
    foreach ($problem_ids as $pid) {
        $cells = $results[$pid];
        if (is_null($cells)) //add $problem_cols_sizes[$ind] empty cells
            for ($i = 0; $i < $problem_cols_sizes[$ind] ; $i++)
                $cells[] = '';

        $row[] = $cells;        

        $ind++;
    }

    return $row;
}

function processGetContestResultsRequest($request) {

    $prfx = $GLOBALS['dces_mysql_prefix'];

    //get $is_anonymous, $contest_id, $user_contest_row, $user_contest_start_time
    if (!is_null($request->sessionID)) {
        $is_anonymous = false;
        $user_contest_row = RequestUtils::testSession($request->sessionID);

        $contest_id = RequestUtils::getRequestedContest(
            $request->contestID,
            $user_contest_row['contest_id'],
            $user_contest_row['user_type']
        );
        if ($contest_id < 0) throwBusinessLogicError(14);

        $user_contest_start_time = DateMySQLToPHP($user_contest_row['contest_start']);        
        $user_contest_finish_time = DateMySQLToPHP($user_contest_row['contest_finish']);        
    }
    else
    {
        $is_anonymous = true;
        $contest_id = $request->contestID;
        $user_contest_start_time = null; //contest was not started for anonymous
        $user_contest_finish_time = null; //and was not finished
    }

    //get $serialized_contest_settings
    $need_request_for_contest_data = $is_anonymous || ($user_contest_row['user_type'] === 'SuperAdmin');
    if ($need_request_for_contest_data) {
        if ($contest_id === 0) throwBusinessLogicError(14);
        $contest_row = Data::getRow(sprintf(
            "SELECT *
             FROM ${prfx}contest
             WHERE id=%s
            ", Data::quote_smart($contest_id))
        );
        if (!$contest_row) throwBusinessLogicError(14);
        $serialized_contest_settings = $contest_row['settings'];
    }
    else
        $serialized_contest_settings = $user_contest_row['settings'];

    //get $contest_settings
    $contest_settings = Data::_unserialize($serialized_contest_settings);

    //get $is_admin
    $is_admin = !$is_anonymous && (
    					($user_contest_row['user_type'] === 'SuperAdmin') ||
    					($user_contest_row['user_type'] === 'ContestAdmin')
    				);

    //get $permission
    $ctime = getCurrentContestTime($contest_settings, $user_contest_start_time, $user_contest_finish_time);
    if (!$is_admin) {
        if ($ctime['interval'] === 'before') throwBusinessLogicError(19);

        if ($ctime['interval'] === 'contest' && !$ctime['is_ending'])
            $permission = $contest_settings->resultsAccessPolicy->contestPermission;
        else if ($ctime['is_ending'])
            $permission = $contest_settings->resultsAccessPolicy->contestEndingPermission;
        else if ($ctime['interval'] === 'after' && !$ctime['is_ending'])
            $permission = $contest_settings->resultsAccessPolicy->afterContestPermission;
    }
    else
        $permission = 'FullAccess';

    //test rights
    if ($permission === 'NoAccess') throwBusinessLogicError(0);
    if ($is_anonymous && $permission === "OnlySelfResults") throwBusinessLogicError(0);

    //get problem rows
    $all_problems_rows = Data::getRows(
                           sprintf("SELECT *
                                    FROM ${prfx}problem
                                    WHERE ${prfx}problem.contest_id=%s
                                    ORDER BY ${prfx}problem.contest_pos ASC",
                                    Data::quote_smart($contest_id))
                         );

    //get users rows
    if ($permission === 'FullAccess')
        $all_users_rows = Data::getRows(
                            sprintf("SELECT *
                                     FROM ${prfx}user
                                     WHERE contest_id=%s"
                                    , Data::quote_smart($contest_id))
                          );
    else /* if $permission === 'OnlySelfResults'*/
        $all_users_rows = $user_contest_row;

    //create result
    $result = new GetContestResultsResponse();

    //fill columns ids
    $result->headers = array();
    $result->minorHeaders = array();
    //the first column with 'user_id' and 'login'
    if ($is_admin) {
        $result->headers[] = 'admin info';
        $result->minorHeaders[] = array('id', 'login');
    }
    //column with participant data
    $result->headers[] = 'participant';
    //get participant subcolumns
    $data_subs = array();
    
    $contest_user_data = $contest_settings->data;
    if ($contest_user_data)
    	foreach ($contest_settings->data as $df)
        	if ($is_admin || $df->showInResult)
            	$data_subs[] = $df->data;
    $result->minorHeaders[] = $data_subs;

    //columns with problems
    $problem_ids = array();
    $problem_cols_sizes = array();
    while ($problem_row = Data::getNextRow($all_problems_rows)) {
        $problem_ids[] = $problem_row['id'];
        $result->headers[] = $problem_row['name'];
        $col_names = Data::_unserialize($problem_row['column_names']);
        $result->minorHeaders[] = $col_names;
        $problem_cols_sizes[] = count($col_names);
    }

    //fill results table
    $result->table = array();

    if ($permission === 'OnlySelfResults') {
        $result->table[] = getTableRow($user_contest_row, $is_admin, $problem_ids, $problem_cols_sizes, $contest_settings->data);
        $result->userLine = 0;
    } else {
        $ind = 0;
        $result->userLine = -1;
        while ($user_row = Data::getNextRow($all_users_rows)) {
            $result->table[] = getTableRow($user_row, $is_admin, $problem_ids, $problem_cols_sizes, $contest_settings->data);
            if ($user_row['id'] == $user_contest_row['id'])
                $result->userLine = $ind;
            $ind++;
        }
    }

    return $result;
}

?>