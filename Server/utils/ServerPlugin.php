<?php

abstract class ServerPlugin {

    protected $problem;

    public function __construct($problem) {
        //may be overriden, but must call parent constructor
        $this->problem = $problem;
    }

    /** checks solution
     * @param $submissionID
     * @param $submission
     * @return hash map string->string with columns data
     */
    public abstract function checkSolution($submission_id, $submission);

    /**
     * Compares two solutions
     * @param  $res1 the first result
     * @param  $res2 the second result
     * @return int 0 if res1 = res2, 1 if res1 > res2 and -1 if res1 < res2
     */
    public abstract function compareResults($res1, $res2);

    public static abstract function getColumnNames();

}

?>