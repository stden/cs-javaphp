﻿<?php

function processCreateContestRequest($request) {
  //open db connection
  $con = connectToDB();

  //get user_id or die, if session is invalid
  $user_id = testSession($con, $request->sessionID);

  //authorize user for this operation
  $user_rights = mysql_query("SELECT user_type FROM user WHERE id=$user_id") or die('DB error 3: '.mysql_error());
  $user_rights_row = mysql_fetch_array($user_rights) or die ("DB error 4 invalid session for user");
  $user_type = $user_rights_row['user_type'];
  if ($user_type != 'ContestAdmin' && $user_type != 'SuperAdmin')
    throwError("The user has no rights to the operation");

  //get contest
  $c = $request->contest;

  //name
  $columns = "name, start_time, finish_time, description, reg_type, user_data, user_data_compulsory";
  $values = "'$c->name', ";
  $values .= "'". DatePHPToMySQL($c->start) . "', ";
  $values .= "'". DatePHPToMySQL($c->finish) . "', ";
  $values .= "'$c->description', ";
  $values .= "'$c->registrationType', ";
  $values .= "'". serialize($c->data) . "', ";
  $values .= "'". serialize($c->compulsory) . "'"; //the only line without ,

  mysql_query("INSERT INTO contest ($columns) VALUES ($values)") or die("DB error 5: ".mysql_error());

  return new AcceptedResponse();
}

?>