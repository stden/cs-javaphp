<?php

//TODO rewrite with preg (not ereg) and make it easier
function processCreateDataBaseRequest($request) {

    //get connection
    $dbname = DB_NAME;
    $prfx = DB_PREFIX;

    //test if creation is necessary
    //TODO get rid of mysql function
    $tables = mysql_list_tables($dbname);
    while (list ($temp) = mysql_fetch_array($tables))
        if ($temp === "${prfx}user")
            throwBusinessLogicError(13);

    //read query lines from files
    $lines = @file("utils/dces-create-db.sql") or throwServerProblem(64);

    //fill queries list
    $sql = "";
    foreach ($lines as $line)
    {
        $line = str_replace("PREFIX_", "$prfx", $line);
        $sql .= $line;
        $is_last_line = ereg(".*;[[:space:]]*$", $line) || ereg(".*;\*/[[:space:]]*$", $line);
        if ($is_last_line) {
            $sql = ereg_replace(";\*/[[:space:]]*$", "*/", $sql);
            $sql = ereg_replace(";[[:space:]]*$", "", $sql);

            Data::submitModificationQuery($sql);
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
    Data::submitModificationQuery(Data::composeInsertQuery('user', $col_value));


    return new AcceptedResponse();

}

?>