<?php

  function processRemoveContestRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    $con = connectToDB();
    $user_row = testSession($con, $request->sessionID);
    $contest_id = $request->contestID;

    //simple security check
    if (!is_numeric($contest_id)) throwError("Contest id is not a number");

    if ($user_row['user_type'] !== 'SuperAdmin')
      throwError("You don't have permission to remove contest $contest_id");
    else
      if ($contest_id === 0) throwError("Can not remove zero contest");

    //get all users of the contest
    $contest_user_rows = mysql_query("SELECT id FROM ${prfx}user WHERE contest_id=$contest_id")
      or die("DB error 32: ".mysql_error());

    //compose where clause for delete query
    $where_user_id = "";
    while (list($user_id) = mysql_fetch_row($contest_user_rows))
      $where_user_id .= 'user_id=' . $user_id . 'OR';
    $where_user_id .= '0=1';

    $queries = array();
    $queries[] = "DELETE FROM ${prfx}contest WHERE id=$contest_id";
    $queries[] = "DELETE FROM ${prfx}problem WHERE contest_id=$contest_id";
    $queries[] = "DELETE FROM ${prfx}session WHERE $where_user_id";
    $queries[] = "DELETE FROM ${prfx}task_result WHERE $where_user_id";
    $queries[] = "DELETE FROM ${prfx}user WHERE contest_id=$contest_id";

    //start transaction
    transaction($con, $queries) or throwError("Failed to remove contest $contest_id");

    return new AcceptedResponse();
  }

?>