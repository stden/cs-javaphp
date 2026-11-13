<?php

require_once 'utils/Problem.php';
require_once 'utils/ResultsTransfer.php';

/**
 * Created by IDEA.
 * User: ilya
 * Date: 12.10.2010
 * Time: 0:40:42
 */

/*
 * Calculated columns are:
 *   problem table
 *   - checker columns (*)
 *   - result columns  (**)
 *   user table
 *   - results results (**)
 *   history
 *   - result (checker) (*)
 *
 *   (*) reevaluated only when RECHECK is called, is slow enough
 *   (**) reevaluated when transfer policy changed. (transfer settings changed, RECHECK)
 */

class ResultUtils {

    /**
     * Updates user result for a specified problem
     * @static
     * @param  $user_id id of the user
     * @param  $problem_id id of the problem
     * @param  $choice type of choice, Self of Last
     * @param  $transfer_settings settings of transfer
     * @param  $plugin plugin associated with the problem, needed only when $choice == Best to find best submission     
     * @param  $last_result last result of user if present
     * @return array array with results for results table
     */
    public static function getUserResults($user_id, $problem_id, $choice, $transfer_settings, $plugin, $last_result = null) {

        $prfx = DB_PREFIX;

        //get new result

        $all_results_request = "
                    SELECT result, submission_time
                    FROM ${prfx}submission_history
                    WHERE user_id=$user_id
                    ORDER BY submission_time DESC
        ";

        if ($choice === 'Last') {
            if ($last_result)
                $new_result = $last_result;
            else {
                //get all rows sorted with descending time, and then get first
                $row = Data::getRow($all_results_request);

                if (! $row)
                    $new_result = array(); //no results
                else
                    $new_result = Data::_unserialize($row['result']);
            }
        } else { // $choice === 'Best'
            $history_results = array();
            if ($last_result)
                $history_results[] = $last_result;
            $rows = Data::getRows($all_results_request);
            while ($row = Data::getNextRow($rows))
                $history_results[] = $row['result'];

            $num_results = count($history_results);

            if ($num_results == 0)
                $new_result = array();
            else {
                $new_result = $history_results[$num_results - 1];
                for ($i = $num_results - 2; $i >= 0; $i--)
                    if ($plugin->compareResults($history_results[$i], $new_result) === 1)
                        $new_result = $history_results[$i];
            }
        }

        $transfer = new ResultsTransfer($transfer_settings);        

        return $transfer->convert($new_result);
    }


    /*
    /**
     * Updates checker columns of the specified problem
     * @static
     * @param  $problem_id
     * @param  $plugin
     * @return string an update request
     *
    public static function updateCheckerColumns($problem_id, $plugin_class = null) {

        if (! $plugin_class) {
            $problem = new Problem(getProblemFile($problem_id));
            $plugin_class = $problem->getServerPlugin();
        }

        $cols = ${$plugin_class}::getColumnNames();

        return Data::composeUpdateQuery(
            'problem', array('checker_columns' => serialize($cols)), "id=$problem_id"
        );
    }

    /**
     * Updates table columns of the specified problem 
     * @static
     * @param  $problem_id
     * @param  $plugin
     * @return void
     *
    public static function updateResultColumns($problem_id, $transfer_settings, $plugin_class = null) {

        //T O D O remove code duplications with the previous function

        if (! $plugin_class) {
            $problem = new Problem(getProblemFile($problem_id));
            $plugin_class = $problem->getServerPlugin();
        }

        $cols = ${$plugin_class}::getColumnNames();

        $rt = new ResultsTransfer($transfer_settings);
        $result_cols = $rt->getResultKeys($cols);

        return Data::composeUpdateQuery(
            'problem', array('result_columns' => serialize($result_cols)), "id=$problem_id"
        );
    }
    */
}