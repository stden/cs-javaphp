<?php

  function processAdjustClientPluginRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    $con = connectToDB();
    $user_row = testSession($con, $request->sessionID);

    //authorize
    if ($user_row['user_type'] !== 'SuperAdmin')
      throwError("You don't have enough rights to adjust client plugin");

    

    return new AcceptedResponse();
  }

?>