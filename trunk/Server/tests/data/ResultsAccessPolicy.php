<?php
class ResultsAccessPolicy{
    public $contestPermission;
    public $contestEndingPermission;
    public $afterContestPermission;

    function __construct(){
        $this->contestPermission = 'FullAccess';
        $this->contestEndingPermission = 'FullAccess';
        $this->afterContestPermission = 'FullAccess';
    }
};
?>
