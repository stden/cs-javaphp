<?php
/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 23.03.2009
 * Time: 21:56:14
 */

function processGetContestResultsRequest($request) {

    $prfx = $GLOBALS['dces_mysql_prefix'];

    //get db connection
    $con = connectToDB();

    //get user_id or die, if session is invalid
    $user_row = testSession($con, $request->sessionID);

    $all_problem_rows = mysql_query(
                         sprintf("SELECT * FROM ${prfx}problem LEFT JOIN ${prfx}problem_status ON ${prfx}problem.id = ${prfx}problem_status.problem_id WHERE ${prfx}problem.contest_id=%s ORDER BY ${prfx}problem.contest_pos ASC ", quote_smart($request->contestID))
                       , $con) or throwServerProblem(73, mysql_error());

    $result = new GetContestDataResponse();

    $result->headers = array();
    $result->headers[] = "Участник";

    $result->minorHeaders = array();

    $user_id_to_table_row = array();
    $table_rows = 0;

    $result->table = array();

    //TODO доделать
    while ($problem_row = mysql_fetch_array($all_problem_rows)) {
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