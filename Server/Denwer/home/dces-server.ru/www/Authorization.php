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
    mysql_query("INSERT INTO session (session_id, user_id) VALUES ('$session_id', $user_id)", $con)
      or die("DB problem 1 ".mysql_error());
  } else {
    //session for the user is ALREADY created
    mysql_query("UPDATE session SET session_id='$session_id' WHERE user_id=$user_id", $con)
      or die("DB problem 2 ".mysql_error());
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
  $session_regexp = "^[a-zA-Z0-9_]+$";
  if ( !ereg($session_regexp, $session_id) ) throwError("invalid session");
  mysql_query("DELETE FROM session WHERE session_id='$session_id'", $con) or die("DB error 8: ".mysql_error());
}

function getUserRow($con, $user_id) {
  $user_rows = mysql_query(
                   sprintf("SELECT * FROM user WHERE id=%s", quote_smart($user_id))
                 , $con) or die('DB error 3: '.mysql_error());
  $user_row = mysql_fetch_array($user_rows) or die ("DB error 4 no user with id $user_id");
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

  // Функция экранирования переменных
  function quote_smart($value)
  {
    /*
    // если magic_quotes_gpc включена - используем stripslashes
    if (get_magic_quotes_gpc()) {
        $value = stripslashes($value);
    }
    */
    // Если переменная - число, то экранировать её не нужно
    // если нет - то окружем её кавычками, и экранируем
    if (!is_numeric($value)) {
        $value = "'" . mysql_real_escape_string($value) . "'";
    }
    return $value;
  }

?>