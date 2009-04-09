<?php

  function processRemoveClientPluginRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    $con = connectToDB();
    $user_row = testSession($request->sessionID);

    //authorize
    if ($user_row['user_type'] !== 'SuperAdmin')
      throwBusinessLogicError(0);

    //remove from db
    mysql_query(
      sprintf("DELETE FROM ${prfx}client_plugin WHERE alias=%s", Data::quote_smart($request->pluginAlias))
    , $con) or throwServerProblem(35, mysql_error());

    //remove from disk
    //TODO don't remove files outside the client plugins folder
    unlink('client_plugins/' . $request->pluginAlias . '.jar');

    return new AcceptedResponse();
  }

?>