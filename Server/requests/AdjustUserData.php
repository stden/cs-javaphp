<?php

function processAdjustUserDataRequest($request) {
    $prfx = DB_PREFIX;

    $user_row = RequestUtils::testSession($request->sessionID);

    $adjust_user_id = $request->userID;
    $adjust_user_row = Data::getRow(sprintf("SELECT * FROM ${prfx}user WHERE id=%s", Data::quote_smart($adjust_user_id)));

    if (!$adjust_user_row)
        throwBusinessLogicError(2);

    if ($user_row['user_type'] === 'Participant')
        throwBusinessLogicError(0);

    if ($user_row['user_type'] === 'ContestAdmin' && $user_row['contest_id'] != $adjust_user_row['contest_id'])
        throwBusinessLogicError(0);

    $queries = array();
    if (!is_null($request->login))
        $queries['login'] = $request->login;
    if (!is_null($request->password))
        $queries['password'] = $request->password;
    if (!is_null($request->userData))
        $queries['user_data'] = @serialize($request->userData);
    if (!is_null($request->newType))
        $queries['user_type'] = $request->newType;

    $q = Data::composeUpdateQuery(
        "user",
        $queries,
        sprintf("id=%s", Data::quote_smart($adjust_user_id))
    );

    Data::submitModificationQuery($q);

    return new AcceptedResponse();
}

?>