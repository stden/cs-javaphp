<?php
class GetContestDataRequest{
    public $sessionID;
    public $contestID;
    public $infoType;
    public $extendedData;

    function __construct(){
        $this->sessionID = null;
        $this->contestID = null;
        $this->infoType = 'ParticipantInfo';
        $this->extendedData = array();
    }
};
?>
