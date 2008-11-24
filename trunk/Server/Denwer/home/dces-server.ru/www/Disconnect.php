<?php

function processDisconnectRequest($request) {
  $con = connectToDB();
  removeSession($con, $request->sessionID);  
  return new AcceptedResponse();
}

?>