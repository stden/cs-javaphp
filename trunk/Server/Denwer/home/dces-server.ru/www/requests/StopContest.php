<?php
/**
 * Created by IntelliJ IDEA.
 * User: Посетитель
 * Date: 17.04.2009
 * Time: 15:57:17
 * To change this template use File | Settings | File Templates.
 */

function processStopContestRequest($request) {

    $user_row = RequestUtils::testSession($request->sessionID);

    $requested_contest_id = RequestUtils::getRequestedContest(
        $request->contestID,
        $user_row['contest_id'],
        $user_row['user_type']
    );

    if ($requested_contest_id < 0) throwBusinessLogicError(0);

    $settings = Data::_unserialize($user_row['settings']);

    if (! $settings->contestTiming->selfContestStart)
        throwBusinessLogicError(0);

    $time = getCurrentContestTime(
        $settings,
        DateMySQLToPHP($user_row['contest_start']),
        DateMySQLToPHP($user_row['contest_finish'])
    );
    
    if ($time['interval'] !== 'before')
        throwBusinessLogicError(19);

    if ($time['interval'] !== 'before')
        throwBusinessLogicError(20);

    $now = getdate();
    $now = $now[0];
    Data::submitModificationQuery(
        composeUpdateQuery(
            'user',
            array('contest_finish' => DatePHPToMySQL($now)),
            "id=${user_row['id']}"
        )
    );
}