<?php

  function processRemovePluginRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    $con = connectToDB();
    $user_row = RequestUtils::testSession($request->sessionID);

    //authorize
    if ($user_row['user_type'] !== 'SuperAdmin')
      throwBusinessLogicError(0);
      
    $plugin_type = $request->side === 'Client' ? 'client' : 'server';
    $plugin_ext = $request->side === 'Client' ? '.jar' : '.php';

    //remove from db
    mysql_query(
      sprintf("DELETE FROM ${prfx}${plugin_type}_plugin WHERE alias=%s", Data::quote_smart($request->pluginAlias))
    , $con) or throwServerProblem(35, mysql_error());

    //remove from disk
    //TODO don't remove files outside the client plugins folder
    unlink('${plugin_type}_plugins/' . $request->pluginAlias . $plugin_ext);

    return new AcceptedResponse();
  }

?>