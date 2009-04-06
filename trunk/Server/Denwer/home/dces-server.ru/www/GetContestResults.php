<?php
/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 21:56:14
 */

function processGetContestResultsRequest($request) {

    $prfx = $GLOBALS['dces_mysql_prefix'];

    //get contest settings and check rights
    if (!is_null($request->sessionID)) {
        $user_contest_row = testSession($request->sessionID);

        $contest_id = getRequestedContest(
            $request->contestID,
            $user_contest_row['contest_id'],
            $user_contest_row['user_type']
        );
        if ($contest_id < 0) throwBusinessLogicError(0);
        $serialized_contest_settings = $user_contest_row['settings'];
        $is_anonymous = false;
    }
    else
    {
        if ($request->contestID === 0) throwBusinessLogicError(14);
        $contest_row = Data::getRow(sprintf(
            "SELECT *
             FROM ${prfx}contest
             WHERE id=%s
            ", quote_smart($request->contestID))
        );
        if (!$contest_row) throwBusinessLogicError(14);

        $serialized_contest_settings = $contest_row['settings'];

        $is_anonymous = true;
    }
    $contest_settings = @unserialize($serialized_contest_settings) or throwServerProblem(106);
    //TODO also check rights

    $all_problems_rows = Data::getRows(
                           sprintf("SELECT * FROM ${prfx}problem LEFT JOIN ${prfx}problem_status ON ${prfx}problem.id = ${prfx}problem_status.problem_id WHERE ${prfx}problem.contest_id=%s ORDER BY ${prfx}problem.contest_pos ASC", quote_smart($request->contestID))
                         );

    $all_users_rows = Data::getRows(
                        sprintf("SELECT * FROM ${prfx}user WHERE ${prfx}user.contest_id=%s", quote_smart($request->contestID))
                      );

    $result = new GetContestDataResponse();

    //set first columns of the result
    $result->headers = array();
    $result->headers[] = "Участник";

    $result->minorHeaders = array();

    $user_id_to_table_row = array();
    $table_rows = 0;

    $result->table = array();

    //TODO доделать
    while ($problem_row = Data::getNextRow($all_problems_rows)) {
        $uid = $problem_row['user_id'];
        if (is_null($user_id_to_table_row[$uid])) {
            $result->table[$table_rows] = array();
            $result->table[$table_rows][0] = array("Имя участника");
            $user_id_to_table_row[$uid] = $table_rows++;
        }

        $row = $user_id_to_table_row[$uid];

        $cols = @unserialize($problem_row['columns']) or $cols = array(); 
        $result->table[$row][$problem_row['contest_pos']] = $cols;
    }

    return $result;
}

?>