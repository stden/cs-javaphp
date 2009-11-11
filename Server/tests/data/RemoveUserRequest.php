<?php
class RemoveUserRequest{
    public $sessionID;
    public $userID;

    function __construct(){
        $this->sessionID = null;
        $this->userID = null;
    }
};
?>
