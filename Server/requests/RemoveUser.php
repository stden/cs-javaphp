<?php

  function processRemoveUserRequest($request) {
    $prfx = DB_PREFIX;

    $user_row = RequestUtils::testSession($request->sessionID);    

    $remove_user_id = $request->userID;
    $remove_user_row = Data::getRow(sprintf(
                                    "SELECT *
                                     FROM ${prfx}user
                                     WHERE id=%s", Data::quote_smart($remove_user_id))
                                   );
    if (!$remove_user_row)
      throwBusinessLogicError(2);

    if ($user_row['user_type'] === 'Participant')
        throwBusinessLogicError(0);

    if ($user_row['user_type'] === 'ContestAdmin' && $user_row['contest_id'] != $remove_user_row['contest_id'])
        throwBusinessLogicError(0);

    //remove user $remove_user_id

    $prfx = DB_PREFIX;

    //from 'users' table
    Data::submitModificationQuery(sprintf("DELETE FROM ${prfx}user WHERE id=%s", Data::quote_smart($remove_user_id)));
    Data::submitModificationQuery(sprintf("DELETE FROM ${prfx}session WHERE user_id=%s", Data::quote_smart($remove_user_id)));
    Data::submitModificationQuery(sprintf("DELETE FROM ${prfx}submission_history WHERE user_id=%s", Data::quote_smart($remove_user_id)));
    Data::submitModificationQuery(sprintf("DELETE FROM ${prfx}problem_status WHERE user_id=%s", Data::quote_smart($remove_user_id)));

    return new AcceptedResponse();
  }

?>