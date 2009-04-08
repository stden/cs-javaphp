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
    $user_data = @unserialize($user_row['user_data']) or throwServerProblem(108);
    foreach ($user_data_cols as $ud) {
        if ($is_admin || $ud->showInResult)
            $data_cols[] = $user_data[$ind];
        $ind++;
    }
    $row[] = $data_cols;

    //fill problem results
    $results = @unserialize($user_row['results']) or throwServerProblem(109);
    $ind = 0;
    foreach ($problem_ids as $pid) {
        $cells = $results[$pid];
        if (is_null($cells)) //add $problem_cols_sizes[$ind] empty cells
            for ($i = 0; $i < $problem_cols_sizes[$ind] ; $i++)
                $cells[] = '';
        $ind++;        
    }

    return $row;
}

function processGetContestResultsRequest($request) {

    $prfx = $GLOBALS['dces_mysql_prefix'];

    //get $is_anonymous, $contest_id, $user_contest_row
    if (!is_null($request->sessionID)) {
        $is_anonymous = false;
        $user_contest_row = testSession($request->sessionID);

        $contest_id = getRequestedContest(
            $request->contestID,
            $user_contest_row['contest_id'],
            $user_contest_row['user_type']
        );
        if ($contest_id < 0) throwBusinessLogicError(14);
    }
    else
    {
        $is_anonymous = true;
        $contest_id = $request->contestID;
    }

    //get $serialized_contest_settings
    $need_request_for_contest_data = $is_anonymous || ($user_contest_row['user_type'] === 'SuperAdmin');
    if ($need_request_for_contest_data) {
        if ($contest_id === 0) throwBusinessLogicError(14);
        $contest_row = Data::getRow(sprintf(
            "SELECT *
             FROM ${prfx}contest
             WHERE id=%s
            ", quote_smart($contest_id))
        );
        if (!$contest_row) throwBusinessLogicError(14);
        $serialized_contest_settings = $contest_row['settings'];
    }
    else
     $serialized_contest_settings = $user_contest_row['settings'];

    //get $contest_settings
    $contest_settings = @unserialize($serialized_contest_settings) or throwServerProblem(106);

    //get $is_admin
    $is_admin = ($user_contest_row['user_type'] === 'SuperAdmin') || ($user_contest_row['user_type'] === 'ContestAdmin');

    //get $permission
    $ctime = getCurrentContestTime();
    if (!is_admin) {
        if ($ctime['interval'] === 'before') throwBusinessLogicError(19);

        if ($ctime['interval'] === 'contest' && !$ctime['is_ending'])
            $permission = $contest_settings->resultAcessPolicy->contestPermission;
        else if ($ctime['is_ending'])
            $permission = $contest_settings->resultAcessPolicy->contestEndingPermission;
        else if ($ctime['interval'] === 'after' && !$ctime['is_ending'])
            $permission = $contest_settings->resultAcessPolicy->afterContestPermission;
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
                                    quote_smart($request->contestID))
                         );

    //get users rows
    if ($permission === 'FullAccess')
        $all_users_rows = Data::getRows(
                            sprintf("SELECT *
                                     FROM ${prfx}user
                                     WHERE ${prfx}contest_id=%s"
                                    , quote_smart($request->contestID))
                          );
    else /* if $permission === 'OnlySelfResults'*/
        $all_users_rows = $user_contest_row;               

    //create result
    $result = new GetContestDataResponse();

    //fill columns ids
    $col_ids = array();
    $result->headers = array();
    $result->minorHeaders = array();
    //the first column with 'user_id' and 'login'
    if ($is_admin) {
        $col_ids[] = 'sysinfo';
        $result->headers[] = '';
        $result->minorHeaders[] = array('id', 'login');
    }
    //column with participant data
    $col_ids[] = 'data';
    $result->headers[] = 'Участник';
    //get participant subcolumns
    $data_subs = array();
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
        $col_names = @unserialize($problem_row['column_names']) or throwServerProblem(107);
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