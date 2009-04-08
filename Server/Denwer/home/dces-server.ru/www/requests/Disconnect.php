<?php

function processDisconnectRequest($request) {
  $con = connectToDB();
  if (!removeSession($con, $request->sessionID))
    throwBusinessLogicError(3);
  return new AcceptedResponse();
}

?>