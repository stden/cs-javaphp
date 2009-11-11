<?php
class ConnectToContestRequest{
    public $contestID;
    public $login;
    public $password;

    function __construct(){
        $this->contestID = null;
        $this->login = 'test_login';
        $this->password = 'test_password';
    }
};
?>
