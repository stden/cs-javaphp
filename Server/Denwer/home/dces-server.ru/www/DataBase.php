<?php

function createDataBase($con) {
 //$sql_file_name = "dces2.sql";
 $sql_file_name = "create-db.sql";
 $fsql = fopen($sql_file_name, "r") or die("failed to find SQL file to create db");
 $sql = fread($fsql, filesize($sql_file_name));
 fclose($fsql);

 $dbname = $GLOBALS['dces_mysql_db'];
 $sql = str_replace("\$db", "$dbname", $sql);

 mysql_query($sql, $con) or die ("failed to execute SQL query to create db:".mysql_error());

 //echo $sql;
}

function connectToDB() {
  $con = mysql_connect($GLOBALS["dces_mysql_host"], $GLOBALS["dces_mysql_user"], $GLOBALS["dces_mysql_password"]);
  if (!$con) die('Could not connect: ' . mysql_error());
  if (!mysql_select_db($GLOBALS["dces_mysql_db"], $con)) {
     createDataBase($con);
     mysql_select_db($GLOBALS["dces_mysql_db"], $con) or die("failed to select db: ".mysql_error());
  }
  return $con;

}

?>