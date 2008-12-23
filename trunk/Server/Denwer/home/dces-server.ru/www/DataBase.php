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

  //TODO invent another way to do nothing in the query
  if (count($col_value) == 0) return "SELECT * FROM ${prfx}client_plugin WHERE 0";

  $values = "";
  foreach ($col_value as $col => $val) {
    $values .= "$col='$val',";
  }  
  $values = rtrim($values,',');

  return "UPDATE $prfx$table SET $values WHERE $where";
}

function connectToDB() {
  $con = mysql_connect($GLOBALS["dces_mysql_host"], $GLOBALS["dces_mysql_user"], $GLOBALS["dces_mysql_password"]);
  if (!$con) die('Could not connect: ' . mysql_error());
  mysql_select_db($GLOBALS["dces_mysql_db"], $con) or die("failed to select db: ".mysql_error());
     
  return $con;
}

?>