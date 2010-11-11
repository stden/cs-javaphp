<?php

function processDisconnectRequest($request) {
    $prfx = DB_PREFIX;
    $where = sprintf("WHERE session_id = %s", Data::quote_smart($request->sessionID));

    //TODO think how to implement this more efficient in our architecture
    if (!Data::getRow("SELECT * FROM ${prfx}session $where"))
        throwBusinessLogicError(3);

    Data::submitModificationQuery("DELETE FROM ${prfx}session ${where}");

    return new AcceptedResponse();
}

?>