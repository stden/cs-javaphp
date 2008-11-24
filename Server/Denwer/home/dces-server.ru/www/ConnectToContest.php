﻿﻿<?php

function processConnectToContestRequest($request) {  
  $con = connectToDB();

  //find user in table
  //TODO prevent SQL injection
  $contest_rows = mysql_query("SELECT * FROM user WHERE login='$request->login' AND contest_id = $request->contestID");
  if ( ! $contest_rows ) die("db error");

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
  //TODO 'uncomment user data'
  //$res->user->dataValue = unserialize($row['user_data']);
  $res->user->userType = $row['user_type'];

  return $res;
}

?>