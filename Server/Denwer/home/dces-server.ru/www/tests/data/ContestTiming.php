<?php
class ContestTiming{
    public $selfContestStart;
    public $maxContestDuration;
    public $contestEndingStart;
    public $contestEndingFinish;

    function __construct(){
        $this->selfContestStart = false;
        $this->maxContestDuration = 60;
        $this->contestEndingStart = 15;
        $this->contestEndingFinish = 15;
    }
};
?>
