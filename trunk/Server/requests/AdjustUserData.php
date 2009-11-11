<?php

  function getUserRow($con, $user_id) {
      $prfx = $GLOBALS['dces_mysql_prefix'];
      $user_rows = mysql_query(
                       sprintf("SELECT * FROM ${prfx}user WHERE id=%s", Data::quote_smart($user_id))
                     , $con) or throwServerProblem(63, mysql_error());
      if (! ($user_row = mysql_fetch_array($user_rows)))
        return false;
      return $user_row;
  }

  function processAdjustUserDataRequest($request) {
    $con = connectToDB();

    $user_row = RequestUtils::testSession($request->sessionID);
    $user_id = $user_row['id'];

    $adjust_user_id = $request->userID;
    $adjust_user_row = getUserRow($con, $adjust_user_id);

    if (!$adjust_user_row)
      throwBusinessLogicError(2);

    if ($user_row['user_type'] === 'Participant')
        throwBusinessLogicError(0);

    if ($user_row['user_type'] === 'ContestAdmin' && $user_row['contest_id'] != $adjust_user_row['contest_id'])
        throwBusinessLogicError(0);

    $queries = array();
    if (!is_null($request->login))
        $queries['login'] = @serialize($request->login);
    if (!is_null($request->password))
        $queries['password'] = @serialize($request->password);
    if (!is_null($request->userData))
        $queries['user_data'] = @serialize($request->userData);
    if (!is_null($request->newType))
        $queries['user_type'] = @serialize($request->newType);

    $q = composeUpdateQuery(
           "user",
           $queries,
           sprintf("id=%s",Data::quote_smart($adjust_user_id))
         );

    mysql_query($q, $con) or throwServerProblem(47, mysql_error());

    return new AcceptedResponse();     
  }

?>