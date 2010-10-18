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
 *   - checker columns
 *   - result columns
 *   user table
 *   - results 
 */

class CalculatedFieldsUpdater {

    /**
     * Updates user result for a specified problem
     * @static
     * @param  $user_id id of the user
     * @param  $problem_id id of the problem
     * @param  $choice type of choice, Self of Last
     * @param  $transfer_settings settings of transfer
     * @param  $plugin plugin associated with the problem, needed only when $choice == Best
     * @param  $all_results all user results, as were taken from the user row
     * @param  $last_result last result of user if present
     * @return void
     */
    public static function updateUserResults($user_id, $problem_id, $choice, $transfer_settings, $plugin, $all_results, $last_result = null) {

        $prfx = DB_PREFIX;

        if (!isset($all_results[$problem_id])) {
            $all_results[$problem_id] = array();
            $old_result = null;
        } else
            $old_result = $all_results[$problem_id]; 

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
            $all_results = array();
            if ($last_result)
                $all_results[] = $last_result;
            $rows = Data::getRows($all_results_request);
            while ($row = Data::getNextRow($rows))
                $all_results[] = $row['result'];

            $num_results = count($all_results);

            if ($num_results == 0)
                $new_result = array();
            else {
                $new_result = $all_results[$num_results - 1];
                for ($i = $num_results - 2; $i >= 0; $i--)
                    if ($plugin->compareResults($all_results[$i], $new_result) === 1)
                        $new_result = $all_results[$i];
            }

            //TODO insert optimization
/*            if (isset($all_results[$problem_id]['r'])) {
                if ($plugin->compareResults($last_result, $user_results[$problem_id]['r']) === 1) {
                    $new_result = $last_result;
                    $result_changed = true;
                }
            } else {
                $new_result = $last_result;
                $result_changed = true;
            }*/
        }

        $user_results[$problem_id]['r'] = $new_result;
        $transfer = new ResultsTransfer($transfer_settings);
        $user_results[$problem_id]['rt'] = $transfer->convert($new_result);

        if ($user_results[$problem_id] !== $old_result) {
            Data::submitModificationQuery(Data::composeUpdateQuery(
                'user', array('results' => serialize($user_results)), "id=$user_id"
            ));
        }
    }

    /**
     * Updates checker columns of the specified problem
     * @static
     * @param  $problem_id
     * @param  $plugin
     * @return void
     */
    public static function updateCheckerColumns($problem_id, $plugin_class = null) {

        if (! $plugin_class) {
            $problem = new Problem(getProblemFile($problem_id));
            $plugin_class = $problem->getServerPlugin();
        }

        $cols = ${$plugin_class}::getColumnNames();

        Data::submitModificationQuery(Data::composeUpdateQuery(
            'problem', array('checker_columns' => serialize($cols)), "id=$problem_id"
        ));
    }

    /**
     * Updates table columns of the specified problem 
     * @static
     * @param  $problem_id
     * @param  $plugin
     * @return void
     */
    public static function updateResultColumns($problem_id, $transfer_settings, $plugin_class = null) {

        //TODO remove code duplications with the previous function

        if (! $plugin_class) {
            $problem = new Problem(getProblemFile($problem_id));
            $plugin_class = $problem->getServerPlugin();
        }

        $cols = ${$plugin_class}::getColumnNames();

        $rt = new ResultsTransfer($transfer_settings);
        $result_cols = $rt->getResultKeys($cols);

        Data::submitModificationQuery(Data::composeUpdateQuery(
            'problem', array('result_columns' => serialize($result_cols)), "id=$problem_id"
        ));
    }

}