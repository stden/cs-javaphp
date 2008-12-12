<?php

  function processRemoveUserRequest($request) {

    $con = connectToDB();

    $user_row = testSession($con, $request->sessionID);
    $user_id = $user_row['id'];

    $remove_user_id = $request->userID;
    $remove_user_row = getUserRow($con, $remove_user_id);
    if (!$remove_user_row)
      throwError("Can not find user $remove_user_id to delete");

    $err = "You don't have permission to remove user $remove_user_id";
    if ($user_row['user_type'] === 'Participant')
        throwError($err);

    if ($user_row['user_type'] === 'ContestAdmin' && $user_row['contest_id'] != $remove_user_row['contest_id'])
        throwError($err);

    //remove user $remove_user_id

    $prfx = $GLOBALS['dces_mysql_prefix'];

    $queries = array();
    //from 'users' table
    $queries[] = sprintf("DELETE FROM ${prfx}user WHERE id=%s", quote_smart($remove_user_id));
    $queries[] = sprintf("DELETE FROM ${prfx}session WHERE user_id=%s", quote_smart($remove_user_id));
    $queries[] = sprintf("DELETE FROM ${prfx}task_result WHERE user_id=%s", quote_smart($remove_user_id));

    transaction($con, $queries) or throwError("failed to remove user $remove_user_id");

    return new AcceptedResponse();
  }

?>