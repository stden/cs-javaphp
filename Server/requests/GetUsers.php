<?php

function processGetUsersRequest($request) {

    $user_row = RequestUtils::testSession($request->sessionID);

    $prfx = DB_PREFIX;

    $user_type = $user_row['user_type'];
    $contest_id = RequestUtils::getRequestedContest($request->contestID, $user_row['contest_id'], $user_type);

    //make superadmin possible to get users of zero-contest
    if ($user_type == 'SuperAdmin' && ($request->contestID == 0 || $request->contestID == -1))
        $contest_id = 0;

    if ($contest_id < 0 || $user_type === 'Participant')
        throwBusinessLogicError(0);

    $rows = Data::getRows(sprintf("SELECT * FROM ${prfx}user WHERE contest_id=$contest_id"));

    $res = new GetUsersResponse();
    $res->users = array();
    while ($row = Data::getNextRow($rows)) {
        $ud = new UserDescription();

        $ud->userID = (int) $row['id'];
        $ud->login = $row['login'];
        $ud->password = $row['password'];
        $ud->dataValue = Data::_unserialize($row['user_data'], array());
        $ud->userType = $row['user_type'];

        $res->users[] = $ud;
    }

    return $res;
}

?>