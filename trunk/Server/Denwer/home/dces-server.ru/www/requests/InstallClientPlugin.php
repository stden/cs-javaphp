<?php

  function processInstallClientPluginRequest($request) {
    //no connection even needed

    //test file name to be a pure file name
    if (strpos($request->clientPluginAlias, '/') ||
        strpos($request->clientPluginAlias, '\\')
       ) throwBusinessLogicError(6);

    $res = new InstallClientPluginResponse();
    $res->pluginInstaller =
      @file_get_contents($GLOBALS['dces_dir_client_plugins'] . '/' . $request->clientPluginAlias . '.jar')
        or throwBusinessLogicError(6);

    return $res;
  }

?>