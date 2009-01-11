<?php

  function processAdjustUserDataRequest($request) {
    $con = connectToDB();

    $user_row = testSession($con, $request->sessionID);
    $user_id = $user_row['id'];

    $adjust_user_id = $request->userID;
    $adjust_user_row = getUserRow($con, $remove_user_id);

    if (!$adjust_user_row)
      throwError("Can not find user $adjust_user_id to delete");

    $err = "You don't have permission to change data for user $adjust_user_id";
    if ($user_row['user_type'] === 'Participant')
        throwError($err);

    if ($user_row['user_type'] === 'ContestAdmin' && $user_row['contest_id'] != $adjust_user_row['contest_id'])
        throwError($err);

    $q = composeUpdateQuery(
           "user",
           array('user_data' => @serialize($request->userData)),
           sprintf("id=%s",quote_smart($request->userID))
         );
  }

?>