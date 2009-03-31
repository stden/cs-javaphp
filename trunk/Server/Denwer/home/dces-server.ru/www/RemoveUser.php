<?php

  function processRemoveUserRequest($request) {

    $con = connectToDB();

    $user_row = testSession($con, $request->sessionID);
    $user_id = $user_row['id'];

    $remove_user_id = $request->userID;
    $remove_user_row = getUserRow($con, $remove_user_id);
    if (!$remove_user_row)
      throwBusinessLogicError(2);

    if ($user_row['user_type'] === 'Participant')
        throwBusinessLogicError(0);

    if ($user_row['user_type'] === 'ContestAdmin' && $user_row['contest_id'] != $remove_user_row['contest_id'])
        throwBusinessLogicError(0);

    //remove user $remove_user_id

    $prfx = $GLOBALS['dces_mysql_prefix'];

    $queries = array();
    //from 'users' table
    $queries[] = sprintf("DELETE FROM ${prfx}user WHERE id=%s", quote_smart($remove_user_id));
    $queries[] = sprintf("DELETE FROM ${prfx}session WHERE user_id=%s", quote_smart($remove_user_id));
    $queries[] = sprintf("DELETE FROM ${prfx}submission_history WHERE user_id=%s", quote_smart($remove_user_id));

    transaction($con, $queries) or throwServerProblem(71);

    return new AcceptedResponse();
  }

?>