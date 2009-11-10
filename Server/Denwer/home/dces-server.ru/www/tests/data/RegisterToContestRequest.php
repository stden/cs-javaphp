<?php
require_once('UserDescription.php');
class RegisterToContestRequest{
    public $sessionID;
    public $contestID;
    public $user;

    function __construct(){
        $this->sessionID = null;
        $this->contestID = null;
        $this->user = new UserDescription();
    }
};
?>
