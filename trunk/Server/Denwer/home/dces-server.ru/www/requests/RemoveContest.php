<?php

  function processRemoveContestRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    $user_row = testSession($request->sessionID);
    $contest_id = $request->contestID;

    //simple security check
    if (!is_numeric($contest_id)) throwBusinessLogicError(14);

    if ($user_row['user_type'] !== 'SuperAdmin')
      throwBusinessLogicError(0);
    else
      if ($contest_id === 0) throwBusinessLogicError(16);

    //get all users of the contest
    $contest_user_rows = Data::getRows("SELECT id FROM ${prfx}user WHERE contest_id=$contest_id");

    //compose where clause for delete query
    $where_user_id = "";
    while (list($user_id) = Data::getNextRow($contest_user_rows))
      $where_user_id .= 'user_id=' . $user_id . 'OR';
    $where_user_id .= '0=1';    

    Data::submitModificationQuery("DELETE FROM ${prfx}contest WHERE id=$contest_id");
    Data::submitModificationQuery("DELETE FROM ${prfx}problem WHERE contest_id=$contest_id");
    Data::submitModificationQuery("DELETE FROM ${prfx}problem_status WHERE $where_user_id");
    Data::submitModificationQuery("DELETE FROM ${prfx}session WHERE $where_user_id");
    Data::submitModificationQuery("DELETE FROM ${prfx}submission_history WHERE $where_user_id");
    Data::submitModificationQuery("DELETE FROM ${prfx}user WHERE contest_id=$contest_id");

    //start transaction

    return new AcceptedResponse();
  }

?>