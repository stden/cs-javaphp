<?php

function createFailRes($code, $failReason = 'BusinessLogicError', $info = '')
{
    $res = new RequestFailedResponse();
    
    $res->failReason = $failReason;
    $res->failErrNo = $code.'';
    $res->extendedInfo = $info;
    
    return $res;
}

function createUser($login, $pass, $type = 'Participant', $data = array())
{
    $user = new UserDescription();
    $user->login = $login;
    $user->password = $pass;
    $user->userType = $type;
    
    $user->data = $data;
        
    return $user;
}


?>