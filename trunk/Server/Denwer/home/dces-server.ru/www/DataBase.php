<?php

function createDataBase($con) {
 $lines = file("dces-create-db.sql") or die("failed to read sql file");

 $dbname = $GLOBALS['dces_mysql_db'];
 $sql = "";
 foreach ($lines as $line)
 {   
   $line = str_replace("\$db", "$dbname", $line);
   $sql .= $line;
   $pos = strpos("$line", ";");
   if ($pos) {
     $sql = str_replace(";", "", $sql);
     mysql_query($sql, $con) or die("failed to create DB, last query: ".$sql." error: ".mysql_error());
     $sql = "";
   }   
 }

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

function transaction($con, $queries) {
  $retval = 1;

  mysql_query("START TRANSACTION", $con);  

  foreach($queries as $qa){
    mysql_query($qa, $con);
    if (mysql_affected_rows() == 0){ $retval = 0; }
  }

  if($retval == 0){
    mysql_query("ROLLBACK", $con);
    return false;
  }else{
    mysql_query("COMMIT", $con);
    return true;
  }

}

?>