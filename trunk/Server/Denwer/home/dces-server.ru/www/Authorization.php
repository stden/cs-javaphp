<?php

function random_str($length)
{
  $char_list = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  $char_list .= "abcdefghijklmnopqrstuvwxyz";
  $char_list .= "1234567890";
  $char_list .= "_";

  $random= "";
  for($i = 0; $i < $length; $i++)
  {
    $random .= substr($char_list, rand(0, strlen($char_list)-1), 1);
  }
  
  return $random;
}

function createSession($con, $user_id) { //returns session string
  //test if session for user_id is already set
  $open_session = mysql_query("SELECT * FROM session WHERE user_id=$user_id", $con);
  if ( ! $open_session ) die("DB error: ".mysql_error());
  $open_session_rows = mysql_fetch_array($open_session);

  $session_id = random_str(24);
  if ( ! $open_session_rows ) {
    //session for the user is NOT YET created
    if ( ! mysql_query("INSERT INTO session (session_id, user_id) VALUES ('$session_id', $user_id)", $con) )
      die("DB problem 1 ".mysql_error());
  } else {
    //session for the user is ALREADY created
    if ( ! mysql_query("UPDATE session SET session_id='$session_id' WHERE user_id=$user_id", $con) )
      die("DB problem 2 ".mysql_error());
  }

  return $session_id;
}

//$con - connection to MySQL
//$sessionID - string to test
function testSession($con, $session_id) { //returns user id, dies absolutely if session is invalid
  //test session ID to have only alphanumeric characters
  $session_regexp = "^[a-zA-Z0-9_]+$";
  if ( !ereg($session_regexp, $session_id) )
    throwError("invalid session");  

  //find user with the specified session
  $user_find = mysql_query("SELECT user_id FROM session WHERE session_id='$session_id'", $con);
  if ( ! $user_find ) die ("DB error 6: ".mysql_error());

  //test if there is at least one such user
  if ( !($user_row = mysql_fetch_array($user_find)) )
    throwError("invalid session");

  //return found user
  return $user_row['user_id'];  
}

function removeSession($con, $session_id) {
  //test if session for user_id is already set
  mysql_query("DELETE FROM session WHERE session_id='$session_id'", $con) or die("DB error 8: ".mysql_error());
}

function getUserType($con, $user_id) {
  $user_rights = mysql_query("SELECT user_type FROM user WHERE id=$user_id", $con) or die('DB error 3: '.mysql_error());
  $user_rights_row = mysql_fetch_array($user_rights) or die ("DB error 4 invalid session for user");
  return $user_rights_row['user_type'];
}

function getUserRow($con, $user_id) {
  $user_rights = mysql_query("SELECT * FROM user WHERE id=$user_id", $con) or die('DB error 3: '.mysql_error());
  $user_rights_row = mysql_fetch_array($user_rights) or die ("DB error 4 invalid session for user");
  return $user_rights_row;
}

?>