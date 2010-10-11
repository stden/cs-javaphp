<?php

function week_password($password) {
    return strlen($password) < 4;
}

function processRegisterToContestRequest($request) {
    $prfx = DB_PREFIX;

    //get user_id or die, if session is invalid 
    if (is_null($request->sessionID)) {
        if (!is_numeric($request->contestID)) throwBusinessLogicError(14);
        $contest_id = (int) ($request->contestID);
        $request_user_type = '__Anonymous';
    }
    else
    {
        $userRow = RequestUtils::testSession($request->sessionID);
        $request_user_id = $userRow['id'];
        $request_user_type = $userRow['user_type'];
        $contest_id = RequestUtils::getRequestedContest($request->contestID, $userRow['contest_id'], $request_user_type);

        //make possible for superadmin to register users of zero-contest
        if ($request_user_type == 'SuperAdmin' && ($request->contestID == 0 || $request->contestID == -1))
            $contest_id = 0;

        if ($contest_id == -1)
            throwBusinessLogicError(0);
    }

    //test permissions
    if ($contest_id != 0) {
        $contest_row = Data::getRow(
            sprintf("SELECT * FROM ${prfx}contest WHERE id=%s", Data::quote_smart($contest_id))
        ) or throwBusinessLogicError(14);

        //test if this contest gets users only by admins
        $contest_settings = @unserialize($contest_row['settings']);
        if ($contest_settings->registrationType === 'ByAdmins')
            if ($request_user_type !== "ContestAdmin" && $request_user_type !== "SuperAdmin")
                throwBusinessLogicError(0);
    } else {
        if ($request_user_type !== "ContestAdmin")
            throwBusinessLogicError(0);
    }

    //get user from request
    $u = $request->user;

    //test that superadmins are registered only for 0 contest
    if ($u->userType === 'SuperAdmin' && $contest_id != 0)
        throwBusinessLogicError(18);

    //test that there is no user with the same login in this contest
    if (Data::hasRows(sprintf("SELECT * FROM ${prfx}user WHERE contest_id=%s AND login=%s",
        Data::quote_smart($contest_id),
        Data::quote_smart($u->login)
    ))
    )
        throwBusinessLogicError(14);

    //not participants may be added only by admins
    if ($u->userType !== "Participant")
        if ($request_user_type !== "ContestAdmin" && $request_user_type !== "SuperAdmin")
            throwBusinessLogicError(0);

    //add user finally
    $col_value = array();
    $col_value['login'] = $u->login;
    $col_value['password'] = $u->password;
    $col_value['user_data'] = @serialize($u->dataValue);
    $col_value['contest_id'] = $contest_id;
    $col_value['user_type'] = $u->userType;
    $col_value['results'] = @serialize(array());

    if (strlen($u->login) == 0)
        throwBusinessLogicError(22);
    if (week_password($u->password))
        throwBusinessLogicError(23);

    Data::submitModificationQuery(Data::composeInsertQuery('user', $col_value));

    return new AcceptedResponse();
}

//TODO prevent creation of users with empty logins and weak passwords
?>