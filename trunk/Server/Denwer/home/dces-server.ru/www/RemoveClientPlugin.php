<?php

  function processRemoveClientPluginRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    $con = connectToDB();
    $user_row = testSession($con, $request->sessionID);

    //authorize
    if ($user_row['user_type'] !== 'SuperAdmin')
      throwError("You don't have enough rights to remove client plugin");

    //remove from db
    mysql_query(
      sprintf("DELETE FROM ${prfx}client_plugin WHERE alias=%s", quote_smart($request->pluginAlias))
    , $con) or die("DB error 35: ".mysql_error());

    //remove from disk
    //TODO don't remove files outside the client plugins folder
    unlink('client_plugins/' . $request->pluginAlias . '.jar');

    return new AcceptedResponse();
  }

?>