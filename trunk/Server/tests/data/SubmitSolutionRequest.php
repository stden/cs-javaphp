<?php
class SubmitSolutionRequest{
    public $sessionID;
    public $problemID;
    public $problemResult;

    function __construct(){
        $this->sessionID = null;
        $this->problemID = null;
        $this->problemResult = array();
    }
};
?>
