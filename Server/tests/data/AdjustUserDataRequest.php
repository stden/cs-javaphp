<?php
class AdjustUserDataRequest{
    public $userID;
    public $sessionID;
    public $login;
    public $password;
    public $newType;
    public $userData;

    function __construct(){
        $this->userID = 0;
        $this->sessionID = null;
        $this->login = 'test_login';
        $this->password = 'test_password';
        $this->newType = 'Participant';
        $this->userData = array();
    }
};
?>
