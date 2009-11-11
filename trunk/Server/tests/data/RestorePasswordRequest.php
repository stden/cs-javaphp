<?php
class RestorePasswordRequest{
    public $contestID;
    public $login;

    function __construct(){
        $this->contestID = null;
        $this->login = 'test_login';
    }
};
?>
