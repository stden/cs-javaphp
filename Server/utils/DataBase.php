<?php

class Data {

    private static $con = null; //dbase connection    
    private static $queries = array(); //pending modification queries
    private static $inserted_ids = array();

    private static function connectToDB() {
        $con = mysql_connect($GLOBALS["dces_mysql_host"], $GLOBALS["dces_mysql_user"], $GLOBALS["dces_mysql_password"]);
        if (!$con) throwServerProblem(66);
        mysql_select_db($GLOBALS["dces_mysql_db"], $con) or throwServerProblem(67, mysql_error());

        return $con;
    }

    public static function getRows($query) {
        if (is_null(Data::$con))
            Data::$con = connectToDB();

        $rows = mysql_query($query, Data::$con) or throwServerProblem(100, mysql_error());

        return $rows;
    }

    public static function getNextRow($rows) {
        return mysql_fetch_array($rows);
    }
         
    public static function getRow($query, $assert_the_only_row = false) {
        $rows = Data::getRows($query);
        $row = Data::getNextRow($rows);
        if ($assert_the_only_row)
            if (Data::getNextRow($rows))
                throwServerProblem(101);
        return $row;
    }

    public static function hasRows($query) {
        $rows = Data::getRows($query);
        $row = Data::getNextRow($rows);
        if ($row)
            return true;
        else
            return false;
    }

    public static function submitModificationQuery($query) {
        Data::$queries[] = $query;
    }

    public static function execPendingQueries() {
    	$inserted_ids = array();    	
    	
        if (is_null(Data::$con))
            Data::$con = connectToDB();

        $error_msg = false;

        mysql_query("START TRANSACTION", Data::$con) or throwServerProblem(101, mysql_error());

        foreach(Data::$queries as $qa) {        	
            $res = mysql_query($qa, Data::$con);
            if ( ! $res ) {
                $error_msg = mysql_error();
                break;
            }
            $iid = mysql_insert_id();
            if ($iid)
            	Data::$inserted_ids[] = $iid;
        }

        if($error_msg !== false){
            mysql_query("ROLLBACK", Data::$con) or throwServerProblem(102, mysql_error());
            throwServerProblem(104, $error_msg);
        } else {
            mysql_query("COMMIT", Data::$con) or throwServerProblem(103, mysql_error());
            Data::$queries = array();
        }
    }
    
    public static function getInsertedIDs() {
    	return Data::$inserted_ids;
    }

    // ������� ������������� ����������
    public static function quote_smart($value)
    {
        /*
        // ���� magic_quotes_gpc �������� - ���������� stripslashes
        if (get_magic_quotes_gpc()) {
            $value = stripslashes($value);
        }
        */

        if (is_null(Data::$con))
            Data::$con = connectToDB();

        // ���� ���������� - �����, �� ������������ � �� �����
        // ���� ��� - �� ������� � ���������, � ����������
        if (!is_numeric($value)) {
        	if (!is_string($value))
        		throwServerProblem(203);
            $value = "'" . mysql_real_escape_string($value, Data::$con) . "'";
        }
        return $value;
    }

    public static function _unserialize($val, $default = false) {
      $res = @unserialize($val);
      if ($res === false)
        if ($default === false)
            throwServerProblem(110);
        else
            $res = $default;        

      return $res;
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
    $vals .= Data::quote_smart($val) . ',';
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
    $qval = Data::quote_smart($val);
    $values .= "$col=$qval,";
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