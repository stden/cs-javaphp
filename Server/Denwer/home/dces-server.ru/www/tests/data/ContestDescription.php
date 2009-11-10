<?php
require_once('ResultsAccessPolicy.php');
require_once('ContestTiming.php');
class ContestDescription{
    public $contestID;
    public $name;
    public $description;
    public $start;
    public $finish;
    public $registrationType;
    public $data;
    public $resultsAccessPolicy;
    public $contestTiming;

    function __construct(){
        $this->contestID = -1;
        $this->name = 'Sample contest';
        $this->description = 'Description of sample contest';
        $this->start = time();
        $this->finish = time() + 3600;
        $this->registrationType = 'ByAdmins';
        $this->data = array();
        $this->resultsAccessPolicy = new ResultsAccessPolicy();
        $this->contestTiming = new ContestTiming();
    }
};
?>
