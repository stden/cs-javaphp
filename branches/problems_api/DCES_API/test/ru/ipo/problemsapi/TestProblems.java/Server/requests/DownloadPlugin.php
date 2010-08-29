<?php

  function processDownloadPluginRequest($request) {
    //test rights
    if ($request->side !== 'Client') {
    	$user = RequestUtils::testSession($request->sessionID);
    	if ($user['user_type'] !== 'SuperAdmin')
    		throwBusinessLogicError(0); 
    }
    
    //test plugin alias
    if (preg_match('/^[\p{L}0-9 ]+$/', $request->pluginAlias) === 0)
        throwBusinessLogicError(238);

    //test file name to be a pure file name
    if (strpos($request->pluginAlias, '/') ||
        strpos($request->pluginAlias, '\\')
       ) throwBusinessLogicError(6);

    $res = new DownloadPluginResponse();    
    
    if ($request->side === 'Client')  
    	$res->pluginBytes =
      		@file_get_contents($GLOBALS['dces_dir_client_plugins'] . '/' . $request->pluginAlias . '.jar')
        		or throwBusinessLogicError(6);
    else
    	$res->pluginBytes =
      		@file_get_contents($GLOBALS['dces_dir_server_plugins'] . '/' . $request->pluginAlias . '.php')
        		or throwBusinessLogicError(6);

    return $res;
  }

?>