<?php

function processDisconnectRequest($request) {
  $con = connectToDB();
  if (!removeSession($con, $request->sessionID))
    throwError('Invalid session ID');
  return new AcceptedResponse();
}

?>