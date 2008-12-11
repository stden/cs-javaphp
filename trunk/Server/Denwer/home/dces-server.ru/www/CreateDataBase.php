<?php

  function processCreateDataBaseRequest($request) {

   //get connection
   $con = connectToDB();

   $dbname = $GLOBALS['dces_mysql_db'];
   $prfx = $GLOBALS['dces_mysql_prefix'];

   //test if creation is necessary
   $tables = mysql_list_tables($dbname, $con); 
     while (list ($temp) = mysql_fetch_array ($tables))
       if ($temp == "${prfx}user")
         throwError('DataBase already exists');

   //read query lines from files
   $lines = file("dces-create-db.sql") or die("failed to read sql file");

   //fill queries list
   $sql = "";
   $queries = array();
   foreach ($lines as $line)
   {
     $line = str_replace("PREFIX_", "$prfx", $line);
     $sql .= $line;
     $is_last_line = ereg(".*;[[:space:]]*$", $line) || ereg(".*;\*/[[:space:]]*$", $line);
     if ($is_last_line) {
       $sql = ereg_replace(";\*/[[:space:]]*$", "*/", $sql);
       $sql = ereg_replace(";[[:space:]]*$", "", $sql);

       $queries[] = $sql;
       $sql = "";
     }
   }

   $col_value = array(
     'login' => "$request->login",
     'password' => "$request->password",
     'user_data' => serialize(array()),
     'contest_id' => 0,
     'user_type' => 'SuperAdmin'
   );
   $queries[] = composeInsertQuery('user', $col_value);

   transaction($con, $queries) or throwError('Failed to create a DB');

   return new AcceptedResponse();

  }

?>