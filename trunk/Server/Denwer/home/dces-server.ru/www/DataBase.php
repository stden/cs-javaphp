<?php

function transaction($con, $queries, &$inserted_ids = null) {
  $retval = 1;

  mysql_query("START TRANSACTION", $con);

  foreach($queries as $qa){
    //echo "query = $qa\n";
    $res = mysql_query($qa, $con);
    if ( ! $res ) {
      $retval = 0;
      break;
    }
    if (!is_null($inserted_ids)) {
      $ins = mysql_insert_id();
      if ($ins)
       $inserted_ids[] = $ins;
    }
  }

  if($retval == 0){
    mysql_query("ROLLBACK", $con);
    return false;
  }else{
    mysql_query("COMMIT", $con);
    return true;
  }
}

//returns query string
function composeInsertQuery($table, $col_value) {

  $prfx = $GLOBALS['dces_mysql_prefix'];

  if (count($col_value) == 0) return "";

  $cols = "";
  $vals = "";
  foreach ($col_value as $col => $val) {
    $cols .= "$col,";
    $vals .= quote_smart($val) . ',';
  }
  $cols = rtrim($cols,',');
  $vals = rtrim($vals,',');

  return "INSERT INTO $prfx$table ($cols) VALUES ($vals)";

}

//returns query string
function composeUpdateQuery($table, $col_value, $where) {

  $prfx = $GLOBALS['dces_mysql_prefix'];

  if (count($col_value) == 0) return "";

  $values = "";
  foreach ($col_value as $col => $val) {
    $values .= "$col='$val',";
  }  
  $values = rtrim($values,',');

  return "UPDATE $prfx$table SET $values WHERE $where";
}

function createDataBase($con) {
 $lines = file("dces-create-db.sql") or die("failed to read sql file");

 $dbname = $GLOBALS['dces_mysql_db'];
 $sql = "";
 $queries = array();
 foreach ($lines as $line)
 {   
   $line = str_replace("___DCES___INITIAL___DB___", "$dbname", $line);
   $sql .= $line;
   $is_last_line = ereg(".*;[[:space:]]*$", $line) || ereg(".*;\*/[[:space:]]*$", $line);
   if ($is_last_line) {
     $sql = ereg_replace(";\*/[[:space:]]*$", "*/", $sql);
     $sql = ereg_replace(";[[:space:]]*$", "", $sql);
       
     $queries[] = $sql;
     $sql = "";
   }
 }

 transaction($con, $queries); //Try to create a db. If fail, error will be noticed later
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