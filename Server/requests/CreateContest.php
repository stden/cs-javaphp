<?php

function processCreateContestRequest($request) {
    //get user_id or die, if session is invalid
    $user_row = RequestUtils::testSession($request->sessionID);

    //authorize user for this operation
    $user_type = $user_row['user_type'];
    if ($user_type !== 'SuperAdmin')
        throwBusinessLogicError(0);

    unset($request->contest->contestID);

    RequestUtils::assertContestSettingsIntegrity($request->contest);

    $col_value = array('settings' => serialize($request->contest));

    Data::submitModificationQuery(Data::composeInsertQuery('contest', $col_value));
    Data::execPendingQueries();
    $id = Data::getInsertedID();

    $ccr = new CreateContestResponse();
    $ccr->createdContestID = $id;
    return $ccr;
}

?>