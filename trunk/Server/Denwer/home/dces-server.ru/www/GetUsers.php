<?php

  function processGetUsersRequest($request) {

    //get connection
    $con = connectToDB();

    $user_row = testSession($con, $request->sessionID);
    $user_id = $user_row['id'];

    $prfx = $GLOBALS['dces_mysql_prefix'];

    $user_type = $user_row['user_type'];
    $contest_id = getRequestedContest($request->contestID, $user_row['contest_id'], $user_type);

    //make superadmin possible to get users of zero-contest
    if ($user_type == 'SuperAdmin' && ($request->contestID == 0 || $request->contestID == -1))
      $contest_id = 0;
      
    if ($contest_id < 0 || $user_type === 'Participant')
      throwError('You do not have permission to get user list of this contest');

    $rows = mysql_query(sprintf("SELECT * FROM ${prfx}user WHERE contest_id=$contest_id"), $con)
              or die("DB error 31: ".mysql_error());

    $res = new GetUsersResponse();
    $res->users = array();
    while ($row = mysql_fetch_array($rows)) {
      $ud = new UserDescription();

      $ud->login = $row['login'];
      $ud->password = $row['password'];
      $ud->dataValue = @unserialize($row['user_data']) or $ud->dataValue = array();
      $ud->userType = $row['user_type'];

      $res->users[] = $ud;
    }

    return $res;
  }

?>