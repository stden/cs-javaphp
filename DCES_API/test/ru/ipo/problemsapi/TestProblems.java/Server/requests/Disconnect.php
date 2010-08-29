<?php

function processDisconnectRequest($request) {
  $con = connectToDB();
  if (!RequestUtils::removeSession($con, $request->sessionID))
    throwBusinessLogicError(3);
  return new AcceptedResponse();
}

?>