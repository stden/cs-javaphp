<?php

require_once(getServerPluginFile());
require_once('utils/Problem.php');

function processGetContestDataRequest($request) {
    $prfx = DB_PREFIX;

    $is_anonymous = is_null($request->sessionID);
    if (!$is_anonymous) {
        //get user_id or die, if session is invalid
        $userRow = RequestUtils::testSession($request->sessionID);
        $user_id = $userRow['id'];

        //authorize user for this operation
        // get contest ID
        $user_type = $userRow['user_type'];

        //compare requested contest and user contest
        $contest_id = RequestUtils::getRequestedContest($request->contestID, $userRow['contest_id'], $user_type);
    }
    else
    {
        $contest_id = $request->contestID;
    }
    if ($contest_id <= 0) throwBusinessLogicError(0);

    //create response
    $res = new GetContestDataResponse();

    //fill contest description with data
    //query db    
    $row = Data::getRow(
        sprintf("SELECT * FROM ${prfx}contest WHERE id=%s", Data::quote_smart($contest_id))
    ) or throwBusinessLogicError(14);

    //TODO remove this code duplication, the code is similar to AvailableContests.php
    $c = Data::_unserialize($row['settings']);
    $c->contestID = (int) $row['id'];
    $res->contest = $c;

    //fill problem data

    if ($is_anonymous) return $res;

    //query db to find out problems
    $problems_rows = Data::getRows(
        sprintf("SELECT * FROM ${prfx}problem WHERE contest_id=%s ORDER BY contest_pos ASC", Data::quote_smart($contest_id))
    );

    //fill problems data
    $res->problems = array();
    $info_type = $request->infoType;
    $extended_data = $request->extendedData;
    while ($row = Data::getNextRow($problems_rows)) {
        $pd = new ProblemDescription();
        $res->problems[] = $pd;

        $pd->id = (int) $row['id'];
        $pd->settings = Data::_unserialize($row['contest_settings']);

        //do we need any information
        if ($info_type == 'NoInfo')
            continue;
        //do we need to return some info for this problem
        if (!is_null($extended_data) && !in_array($pd->id, $extended_data))
            continue;

        $problem = new Problem(getProblemFile($pd->id));

        if ($info_type !== 'NoInfo') {
            //fill extended data: statement or statementData and answerData
            if ($info_type === "ParticipantInfo")
                $pd->problem = $problem->getParticipantVersion($user_id)->getProblemBytes();
            elseif ($info_type === "AdminInfo") {
                if ($user_type === "Participant") throwBusinessLogicError(0);
                $pd->problem = $problem->getProblemBytes();
            }
        }
    }

    return $res;
}

/*
 load plugin
 $pluginName = $problem->getServerPlugin();
 require_once(getServerPluginFile($pluginName));
 */

?>