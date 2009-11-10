<?php
class UserDescription{
    public $userID;
    public $login;
    public $password;
    public $dataValue;
    public $userType;

    function __construct(){
        $this->userID = null;
        $this->login = 'test_login';
        $this->password = 'test_password';
        $this->dataValue = array();
        $this->userType = 'Participant';
    }
};
?>
