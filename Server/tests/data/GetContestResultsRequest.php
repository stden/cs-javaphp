<?php
class GetContestResultsRequest{
    public $sessionID;
    public $contestID;

    function __construct(){
        $this->sessionID = null;
        $this->contestID = null;
    }
};
?>