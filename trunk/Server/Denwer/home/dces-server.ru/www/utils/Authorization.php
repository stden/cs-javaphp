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

function createSession($user_id) { //returns session string
  $prfx = $GLOBALS['dces_mysql_prefix'];

  //test if session for user_id is already set
  $open_session_rows = Data::getRow("SELECT * FROM ${prfx}session WHERE user_id=$user_id");

  $session_id = random_str(24);
  if ( ! $open_session_rows ) {
    //session for the user is NOT YET created
    Data::submitModificationQuery("INSERT INTO ${prfx}session (session_id, user_id) VALUES ('$session_id', $user_id)");
  } else {
    //session for the user is ALREADY created
    Data::submitModificationQuery("UPDATE ${prfx}session SET session_id='$session_id' WHERE user_id=$user_id");
  }

  return $session_id;
}

//$con - connection to MySQL
//$sessionID - string to test
//TODO try to implement it with one query with INNER JOIN
function testSession($session_id) { //returns user id, dies absolutely if session is invalid
  $prfx = $GLOBALS['dces_mysql_prefix'];
  //test session ID to have only alphanumeric characters
  $session_regexp = "^[a-zA-Z0-9_]+$";
  if ( !ereg($session_regexp, $session_id) )
    throwBusinessLogicError(3);

  //test if there is at least one such user
  $user_row = Data::getRow(
  "SELECT ${prfx}user.*, ${prfx}contest.settings
   FROM ${prfx}session
   INNER JOIN ${prfx}user
   ON ${prfx}session.user_id=${prfx}user.id
   LEFT JOIN ${prfx}contest
   ON ${prfx}user.contest_id=${prfx}contest.id
   WHERE session_id='$session_id'"
  );
  if ( !$user_row )
    throwBusinessLogicError(3);

  if (is_null($user_row['settings']))
    $user_row['settings'] = serialize(null);  

  //return found user
  return $user_row;  
}

//return boolean - true if succeeded and false otherwise
function removeSession($con, $session_id) {
  $prfx = $GLOBALS['dces_mysql_prefix'];  
  //test if session for user_id is already set
  $session_regexp = "^[a-zA-Z0-9_]+$";
  if ( !ereg($session_regexp, $session_id) )
    throwBusinessLogicError(3);
  mysql_query("DELETE FROM ${prfx}session WHERE session_id='$session_id'", $con) or throwServerProblem(8, mysql_error());
  if (mysql_affected_rows() === 0) return false;
                              else return true; 
}

function getUserRow($con, $user_id) {
  $prfx = $GLOBALS['dces_mysql_prefix'];
  $user_rows = mysql_query(
                   sprintf("SELECT * FROM ${prfx}user WHERE id=%s", Data::quote_smart($user_id))
                 , $con) or throwServerProblem(63, mysql_error());
  if (! ($user_row = mysql_fetch_array($user_rows)))
    return false;
  return $user_row;
}

function getRequestedContest($requested_contest_id, $user_contest_id, $user_type) {

    if (!is_numeric($requested_contest_id)) return -1;
    $requested_contest_id = (int)$requested_contest_id;

    $contest_id = -1;
    if ($requested_contest_id < 0 && $user_contest_id != 0)
      $contest_id = $user_contest_id;
    elseif ($requested_contest_id == $user_contest_id && $user_contest_id != 0)
      $contest_id = $user_contest_id;
    elseif ($requested_contest_id != $user_contest_id && $user_type === "SuperAdmin")
      $contest_id = $requested_contest_id;

    return $contest_id;
  }

?>