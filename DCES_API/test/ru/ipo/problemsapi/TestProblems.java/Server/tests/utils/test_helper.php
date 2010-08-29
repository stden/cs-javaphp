<?php

function createFailRes($code, $info = '', $failReason = 'BusinessLogicError')
{
    $res = new RequestFailedResponse();
    
    $res->failReason = $failReason;
    $res->failErrNo = $code.'';
    $res->extendedInfo = $info;
    
    return $res;
}

function createUser($login, $pass, $type = 'Participant', $dataValue = array())
{
    $user = new UserDescription();
    $user->login = $login;
    $user->password = $pass;
    $user->userType = $type;
    
    $user->dataValue = $dataValue;
        
    return $user;
}

function createConnectToContestReq($l, $p, $contestID)
{
    $req = new ConnectToContestRequest();
     
     $req->contestID = $contestID;
     $req->login = $l;
     $req->password = $p;
     
     return $req;
}

function fillObjWithArray($name, $ar)
{
    $res = new $name();
    
    foreach($ar as $prop_name => $value)
        $res->$prop_name = $value;
    
    return $res;
}

?>