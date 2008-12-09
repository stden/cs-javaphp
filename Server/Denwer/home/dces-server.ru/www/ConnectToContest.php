<?php

function processConnectToContestRequest($request) {
  $prfx = $GLOBALS['dces_mysql_prefix'];
  $con = connectToDB();

  //find user in table
  $contest_rows = mysql_query(
                    sprintf("SELECT * FROM ${prfx}user WHERE login=%s AND contest_id=%s",
                            quote_smart($request->login),
                            quote_smart($request->contestID)
                           )
                  ) or die("db error 26:". mysql_error());

  //test if there is at least one user
  if ( !($row = mysql_fetch_array($contest_rows)) )
      throwError("User, password or contest not found");

  //test password
  if ($row['password'] != $request->password)
      throwError("User, password or contest not found");

  //TODO implement the test: if user $row['id'] is a participant, then connection is allowed only duing the contest time

  //start new session
  $session_id = createSession($con, $row['id']);

  $res = new ConnectToContestResponse();
  $res->sessionID = $session_id;
  $res->user = new UserDescription();
  $res->user->login = $request->login;
  $res->user->dataValue = unserialize($row['user_data']) or $res->user->dataValue = array();
  $res->user->userType = $row['user_type'];

  return $res;
}

?>