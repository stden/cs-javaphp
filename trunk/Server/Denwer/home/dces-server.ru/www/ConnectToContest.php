<?php

function processConnectToContestRequest($request) {  
  $con = connectToDB();

  //find user in table
  //TODO prevent SQL injection
  $contest_rows = mysql_query("SELECT * FROM user WHERE login='$request->login' AND contest_id = $request->contestID");
  if ( ! $contest_rows ) die("db error");

  if ( !($row = mysql_fetch_array($contest_rows)) )
      throwError("User, password or contest not found");

  if ($row['password'] != $request->password)
      throwError("User, password or contest not found");

  $session_id = createSession($con, $row['user_id']);

  $res = new ConnectToContestResponse();
  $res->sessionID = $session_id;
  $res->user = new UserDescription();
  $res->user->login = "";
  $res->user->dataValue = unserialize($row['user_data']);
  $res->user->userType = $row['user_type'];

  //INSERT INTO table_name (column1, column2, column3,...)
  //VALUES (value1, value2, value3,...)
}

?>