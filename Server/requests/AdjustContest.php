<?php

require_once 'utils/Problem.php';
require_once getServerPluginFile();
require_once 'utils/ResultUtils.php';

/**
 * Copies fields from from to to, doesn't copy values that have 'ignore' values
 * @param $from from
 * @param $to to
 * @return int, number of copied (non-default) values
 */
function copyValues($from, $to) {
    if (!$from)
        return 0;

    $res = 0;
    foreach ($from as $fld => $val) {
        //test val to be default value  $a = array();
        $is_default = $val === -1 || $val === NULL;
        if (!$is_default) {
            //copy values, recurse if val is an object
            if (is_object($val))
                $res += copyValues($val, $to->$fld);
            else
                $to->$fld = $val;
            $res++;
        }
    }

    return $res;
}

//TODO implement coping only of changed values
function queryForContestDescription($contest_description, $contest_id) {
    //get current contest settings
/*    $prfx = DB_PREFIX;
    $row = Data::getRow("SELECT settings FROM ${prfx}contest WHERE id=$contest_id");
    $settings = Data::_unserialize($row['settings']);*/

    //copyValues($contest_description, $settings);

    RequestUtils::assertContestSettingsIntegrity($contest_description);

    //$__new_contest_settings = $settings;

    $col_value = array('settings' => @serialize($contest_description));
    Data::submitModificationQuery(
        Data::composeUpdateQuery("contest", $col_value, "id=$contest_id")
    );
}

/**
 * creates queries to change problem set
 * @param $problems new problems
 * @param $contest_id contest id
 * @return array() list of temporary files. NULL if the existing file was used
 */
function queriesToAdjustProblems($problems, $contest_id) {
    $prfx = DB_PREFIX;

    $changed_probs = array(); //problems that will be changed by request    
    $temp_probs = array();

    //find all contest problems
    $prob2settings = array();
    $res = Data::getRows(sprintf("SELECT * FROM ${prfx}problem WHERE contest_id=%s", Data::quote_smart($contest_id)));
    while ($row = Data::getNextRow($res))
        $prob2settings[$row['id']] = Data::_unserialize($row['contest_settings']);

    $contest_pos = 1;
    foreach ($problems as $p) {
        $col_value = array();
        //new problem must have 1) data 2) settings

        //set contest id
        $col_value['contest_id'] = $contest_id;
        $col_value['contest_pos'] = $contest_pos++;

        if ($p->id != -1 && !isset($prob2settings[$p->id]))
            throwBusinessLogicError(4);

        //find problem file or make temporary if a new problem was sent
        if ($p->problem) {
            $problem_file = getTemporaryProblemFile();
            @file_put_contents($problem_file, $p->problem) or throwServerProblem('200', 'failed to write problem file');
            $temp_probs[] = $problem_file;
        } else {
            if ($p->id < 0)
                throwBusinessLogicError(1);
            $problem_file = getProblemFile($p->id);
            $temp_probs[] = NULL;
        }

        $problem = new Problem($problem_file);

        //get server plugin
        //TODO improve security here
        $plugin_alias = $problem->getServerPlugin();
        require_once(getServerPluginFile($plugin_alias));
        $plugin = new $plugin_alias($problem);
        //TODO consider calling updaters here instead of manual insertion of values
        //TODO recheck all values if new plugin specified
        $col_value['checker_columns'] = serialize($plugin->getColumnNames());
        $col_value['result_columns'] = serialize(array());

        //copy per contest settings
        if ($p->settings) {
            if ($p->id != -1) {
                $new_settings = $prob2settings[$p->id];
                copyValues($p->settings, $new_settings);
            } else $new_settings = $p->settings;
            $col_value['contest_settings'] = serialize($new_settings);
        } else if ($p->id < 0) throwBusinessLogicError(1);

        //query depends on whether we add or change a problem
        if ($p->id == -1) {
            Data::submitModificationQuery(
                Data::composeInsertQuery('problem', $col_value)
            );
        } else {
            Data::submitModificationQuery(
                Data::composeUpdateQuery('problem', $col_value, "id='$p->id'")
            );
            $changed_probs[$p->id] = 1;
        }

    }

    //delete extra problems
    foreach (array_keys($prob2settings) as $id)
        if (!isset($changed_probs[$id])) {
            Data::submitModificationQuery(
                "DELETE FROM ${prfx}problem WHERE id='$id'"
            );
        }

    return $temp_probs;
}

function processAdjstContestRequest($request) {

    if (!$request->contest)
        throwBusinessLogicError(1, 'contest is null');

    //get user_id or die, if session is invalid
    $userRow = RequestUtils::testSession($request->sessionID);

    //authorize user for this operation
    // get contest ID
    $user_type = $userRow['user_type'];
    $contest_id = RequestUtils::getRequestedContest($request->contest->contestID, $userRow['contest_id'], $user_type);
    if ($user_type === "Participant") $contest_id = -1;
    if ($contest_id < 0) throwBusinessLogicError(0);

    queryForContestDescription($request->contest, $contest_id);

    //now adjust problems
    if (!is_null($request->problems))
        $tmp_files = queriesToAdjustProblems($request->problems, $contest_id);

    Data::execPendingQueries();
    $new_ids = Data::getInsertedIDs();
    $id_ind = 0;

    //rename temporary files and fill responseIDs
    if (!is_null($request->problems)) {
        $responseIDs = array();
        $probs_cnt = count($request->problems);
        for ($i = 0; $i < $probs_cnt; $i++) {
            $p = $request->problems[$i];
            $tmp = $tmp_files[$i];
            if ($tmp) {
                $new_id = $p->id;
                if ($new_id < 0)
                    $new_id = $new_ids[$id_ind++];
                @rename($tmp, getProblemFile($new_id));
                $responseIDs[] = $new_id;
            } else {
                $responseIDs[] = $p->id;
                if ($p->id < 0) //for new tasks it must have been created a temporary file
                    throwServerProblem(202);
            }
        }

    } else
        $responseIDs = NULL;

    $response = new AdjustContestResponse();
    $response->problemIDs = $responseIDs;
    return $response;
}

?>