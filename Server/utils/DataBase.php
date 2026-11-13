<?php

class Data {

    private static $con = null; //dbase connection    
    private static $queries = array(); //pending modification queries
    private static $inserted_ids = array();

    private static function connectToDB() {
        $con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
        if (!$con) throwServerProblem(66);
        mysql_select_db(DB_NAME, $con) or throwServerProblem(67, mysql_error());

        return $con;
    }

    public static function getRows($query) {
        if (is_null(Data::$con))
            Data::$con = Data::connectToDB();

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

    //returns array with table names
    public static function getTables() {
        if (is_null(Data::$con))
            Data::$con = Data::connectToDB();

        $tables = mysql_list_tables(DB_NAME, Data::$con) or throwServerProblem(207, mysql_error());

        $res = array();
        while ($table = mysql_fetch_array($tables))
            $res[] = $table[0];
        return $res;
    }

    public static function submitModificationQuery($query) {
        Data::$queries[] = $query;
    }

    public static function execPendingQueries() {
        if (is_null(Data::$con))
            Data::$con = Data::connectToDB();

        $error_msg = false;

        mysql_query("START TRANSACTION", Data::$con) or throwServerProblem(101, mysql_error());

        foreach (Data::$queries as $qa) {
            $res = mysql_query($qa, Data::$con);
            if (!$res) {
                $error_msg = mysql_error();
                break;
            }
            $iid = mysql_insert_id();
            if ($iid)
                Data::$inserted_ids[] = $iid;
        }

        if ($error_msg !== false) {
            mysql_query("ROLLBACK", Data::$con) or throwServerProblem(102, mysql_error());
            throwServerProblem(104, $error_msg . ' QUERY: "' . $qa . '"');
        } else {
            mysql_query("COMMIT", Data::$con) or throwServerProblem(103, mysql_error());
            Data::$queries = array();
        }
    }

    public static function getInsertedID() {
        return Data::$inserted_ids[0];
    }

    public static function getInsertedIDs() {
        return Data::$inserted_ids;
    }

    // Функция экранирования переменных
    public static function quote_smart($value) {
        /*
        // если magic_quotes_gpc включена - используем stripslashes
        if (get_magic_quotes_gpc()) {
            $value = stripslashes($value);
        }
        */

        if (is_null(Data::$con))
            Data::$con = Data::connectToDB();

        // Если переменная - число, то экранировать её не нужно
        // если нет - то окружем её кавычками, и экранируем
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

    //returns query string
    public static function composeInsertQuery($table, $col_value) {

        $prfx = DB_PREFIX;

        if (count($col_value) == 0) return "";

        $cols = "";
        $vals = "";
        foreach ($col_value as $col => $val) {
            $cols .= "$col,";
            $vals .= Data::quote_smart($val) . ',';
        }
        $cols = rtrim($cols, ',');
        $vals = rtrim($vals, ',');

        return "INSERT INTO $prfx$table ($cols) VALUES ($vals)";

    }

    //returns query string
    public static function composeUpdateQuery($table, $col_value, $where) {

        $prfx = DB_PREFIX;

        //TODO invent another way to do nothing in the query
        if (count($col_value) == 0) return "SELECT * FROM ${prfx}client_plugin WHERE 0";

        $values = "";
        foreach ($col_value as $col => $val) {
            $qval = Data::quote_smart($val);
            $values .= "$col=$qval,";
        }
        $values = rtrim($values, ',');

        return "UPDATE $prfx$table SET $values WHERE $where";
    }


}

?>