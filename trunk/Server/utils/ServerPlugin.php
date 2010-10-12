<?php

require_once 'utils/SubmissionHistoryUtils.php';

abstract class ServerPlugin {

    protected $problem;

    public function __construct($problem) {
        //may be overriden, but must call parent constructor
        $this->problem = $problem;
    }

    /** saves state */
    protected final function saveState($state) {
        //TODO implement
    }

    /**
     * Submits solution if result may be obtained without delay
     * @param  $submission_id
     * @param  $result
     * @return void
     */
    protected final function submitResult($submission_id, $result) {
        //SubmissionHistoryUtils::
    }

    /** Must be overridden.
     * @param $submissionID
     * @param $submission
     * @return hash map string->string with columns data
     */
    public abstract function checkSolution($submission_id, $submission);

    /**
     * Should usually be overridden
     * @param  $res1 the first result
     * @param  $res2 the second result
     * @return int 0 if res1 = res2, 1 if res1 > res2 and -1 if res1 < res2
     */
    public abstract function compareResults($res1, $res2);

}

?>