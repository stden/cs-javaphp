<?php
require_once('ContestDescription.php');
class AdjustContestRequest{
    public $sessionID;
    public $contest;
    public $problems;

    function __construct(){
        $this->sessionID = null;
        $this->contest = new ContestDescription();
        $this->problems = null;
    }
};
?>
