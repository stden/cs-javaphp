<?php

class ServerPlugin {

    protected $problem;

    public function __construct($problem) {
        //may be overriden, but must call parent constructor
        $this->problem = $problem;
    }

    /** saves state */
    protected final function saveState($state) {
        //TODO implement
    }

    /** Must be overriden.
     * @param $solution
     * @param $submission
     * @return hash map string->string with columns data
     */
    public function checkSolution($solution) {

    }

    /**
     * Should usually be overriden
     * @param  $res1 the first result
     * @param  $res2 the second result
     * @return int 0 if res1 = res2, 1 if res1 > res2 and -1 if res1 < res2
     */
    public function compareResults($res1, $res2) {
        return 0;
    }

}

?>