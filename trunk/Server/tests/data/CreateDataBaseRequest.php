<?php
class CreateDataBaseRequest{
    public $login;
    public $password;

    function __construct(){
        $this->login = 'admin';
        $this->password = 'superpassword';
    }
};
?>
