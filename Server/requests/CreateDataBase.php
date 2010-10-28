<?php
//request is correct if it is not a comment of the form /*...*/ and is not empty
//function isCorrectRequest($request) {
//    return !preg_match('/^/\*.*/\*$/', $request) && $request !== "";
//}

function processCreateDataBaseRequest($request) {

    //test if creation is necessary
    if (in_array(DB_PREFIX . "user", Data::getTables()))
        throwBusinessLogicError(13);

    //read queries from file
    $lines = @file_get_contents("utils/dces-create-db.sql") or throwServerProblem(64);
    //remove # comments
    $lines = preg_replace("/^\\s*#.*$/m", "", $lines);
    //remove /*! ... */ comments
    $lines = preg_replace('/\/\*!\d+.*\*\//', "", $lines);
    //replace PREFIX_ with real prefix
    $lines = preg_replace('/PREFIX_/', DB_PREFIX, $lines);
    //split requests by ;
    $requests = preg_split('/\\s*;\\s*$/m', $lines);
    
    foreach ($requests as $r) {
        if (trim($r) !== "")
            Data::submitModificationQuery($r);
    }

    $col_value = array(
        'login' => $request->login,
        'password' => $request->password,
        'user_data' => serialize(array()),
        'contest_id' => 0,
        'user_type' => 'SuperAdmin'
    );
    Data::submitModificationQuery(Data::composeInsertQuery('user', $col_value));

    return new AcceptedResponse();
}

?>