<?php

function createDataBase() {
//TODO implement database creation
}

function connectToDB() {
  $con = mysql_connect($GLOBALS["dces_mysql_host"], $GLOBALS["dces_mysql_user"], $GLOBALS["dces_mysql_password"]);
  if (!$con) die('Could not connect: ' . mysql_error());
  if (!mysql_select_db($GLOBALS["dces_mysql_db"], $con))
     createDataBase();
  
  return $con;
}

?>