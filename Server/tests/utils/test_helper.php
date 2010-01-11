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

?>