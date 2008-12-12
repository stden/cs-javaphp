<?php

  function processRegisterToContestRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    //get db connection
    $con = connectToDB();

    //get user_id or die, if session is invalid
    if (is_null($request->sessionID)) {
      if (!is_numeric($request->contestID)) throwError('Contest id is not a number');
      $contest_id = (int)($request->contestID);
      $request_user_type = '__Anonymous';
    }
    else
    {
      $userRow = testSession($con, $request->sessionID);
      $request_user_id = $userRow['id'];
      $request_user_type = $userRow['user_type'];
      $contest_id = getRequestedContest($request->contestID, $userRow['contest_id'], $request_user_type);

      //make possible for superadmin to register users of zero-contest
      if ($user_type == 'SuperAdmin' && ($request->contestID == 0 || $request->contestID == -1))
        $contest_id = 0;

      if ($contest_id == -1)
        throwError("You don't have permissions to add users for contest $contest_id");
    }

    //get contest registration type
    $contest_rows = mysql_query(
                      sprintf("SELECT * FROM ${prfx}contest WHERE id=%s", quote_smart($contest_id))
                    , $con) or die ("DB error 29: ".mysql_error());
    $contest_row = mysql_fetch_array($contest_rows) or throwError("Contest with id $contest_id not found");              

    //test if this contest gets users only by admins
    if ($contest_row['reg_type'] === "ByAdmins")
      if ($request_user_type !== "ContestAdmin" && $request_user_type !== "SuperAdmin")
        throwError("Only an administrator can register users to this contest");

    //get user from request
    $u = $request->user;    

    //test that superadmins are registered only for 0 contest
    if ($u->userType === "SuperAdmin" && $contest_id != 0)
      throwError("Super Admin can be registered only for zero contest");

    //test that there is no user with the same login in this contest
    $users_with_the_same_login = mysql_query(
                                   sprintf("SELECT * FROM ${prfx}user WHERE contest_id=%s AND login=%s",
                                   quote_smart($contest_id),
                                   quote_smart($u->login)
                                   ) , $con) or die("DB error 31: ".mysql_error());
    if (mysql_fetch_array($users_with_the_same_login))
      throwError("User with the same login is already registered for this contest");

    //not participants may be added only by admins
    if ($u->userType !== "Participant")
      if ($request_user_type !== "ContestAdmin" && $request_user_type !== "SuperAdmin")
        throwError("Not participants may be added only by admins");

    //add user finally
    $col_value = array();
    $col_value['login'] = $u->login;
    $col_value['password'] = $u->password;
    $col_value['user_data'] = serialize($u->dataValue);
    $col_value['contest_id'] = $contest_id;
    $col_value['user_type'] = $u->userType;

    mysql_query(composeInsertQuery('user', $col_value), $con) or die("DB error 30: ".mysql_error());

    return new AcceptedResponse();
  }
//TODO prevent creation of users with empty logins and "weak" passwords
?>