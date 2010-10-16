<?php
/**
 * Created by IDEA.
 * User: ilya
 * Date: 12.10.2010
 * Time: 0:40:42
 */

class SubmissionHistoryUtils {

    static function setResult($submission_id, $result) {
        //TODO move code from SubmitSolution.php
    }

    /**
     * Returns result for result table
     * @static
     * @param  $result result to convert
     * @param  $conversionSettings settings for conversion
     * @return array entry for results table */
    static function convertToTable($res, $conversionSettings) {
        $rt = array();
        eval($conversionSettings);
        return $rt;
    }

}