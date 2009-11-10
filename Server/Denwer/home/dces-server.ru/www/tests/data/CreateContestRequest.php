<?php
require_once('ContestDescription.php');
class CreateContestRequest{
    public $sessionID;
    public $contest;

    function __construct(){
        $this->sessionID = null;
        $this->contest = new ContestDescription();
    }
};
?>
