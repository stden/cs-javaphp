<?php

class Data {

    private static $con = null; //dbase connection    
    private static $queries = array(); //pending modification queries

    private static function connectToDB() {
        $con = mysql_connect($GLOBALS["dces_mysql_host"], $GLOBALS["dces_mysql_user"], $GLOBALS["dces_mysql_password"]);
        if (!$con) throwServerProblem(66);
        mysql_select_db($GLOBALS["dces_mysql_db"], $con) or throwServerProblem(67, mysql_error());

        return $con;
    }

    public static function getRows($query) {
        if (is_null(Data::$con))
            Data::$con = connectToDB();

        $rows = mysql_query($query, Data::$con) or throwServerProblem(100);

        return $rows;
    }

    public static function getNextRow($rows) {
        $row = mysql_fetch_array($rows, Data::$con);
        return $row;
    }

    public static function getRow($query, $assert_the_only_row = false) {
        $rows = Data::getRows($query);
        $row = Data::getNextRow($rows);
        if ($assert_the_only_row)
            if (Data::getNextRow($rows))
                throwServerProblem(101);
        return $row;
    }

    public static function submitModificationQuery($query) {
        Data::$queries[] = $query;
    }

    public static function execPendingQueries() {
        if (is_null(Data::$con))
            Data::$con = connectToDB();

        $error_msg = false;

        mysql_query("START TRANSACTION", Data::$con) or throwServerProblem(101, mysql_error());

        foreach(Data::$queries as $qa){
            //echo "query = $qa\n";
            $res = mysql_query($qa, Data::$con);
            if ( ! $res ) {
                $error_msg = mysql_error();
                break;
            }
        }

        if($error_msg !== false){
            mysql_query("ROLLBACK", Data::$con) or throwServerProblem(102, mysql_error());
            throwServerProblem(104, $error_msg);
        } else {
            mysql_query("COMMIT", Data::$con) or throwServerProblem(103, mysql_error());
        }
    }

}

//------------------------------------------------------------------------------
//                  Procedures ! To be inserted in the class Data
//------------------------------------------------------------------------------

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
    $qval = quote_smart($val);
    $values .= "$col='$qval',";
  }  
  $values = rtrim($values,',');

  return "UPDATE $prfx$table SET $values WHERE $where";
}

  function connectToDB() {
    $con = mysql_connect($GLOBALS["dces_mysql_host"], $GLOBALS["dces_mysql_user"], $GLOBALS["dces_mysql_password"]);
    if (!$con) throwServerProblem(66);
    mysql_select_db($GLOBALS["dces_mysql_db"], $con) or throwServerProblem(67, mysql_error());

    return $con;
  }

?>