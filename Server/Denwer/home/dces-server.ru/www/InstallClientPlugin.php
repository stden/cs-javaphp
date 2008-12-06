<?php

  function processInstallClientPluginRequest($request) {
    //no connection even needed

    //test file name to be a pure file name
    //TODO implement it
    //if ($request->clientPluginAlias)

    $res = new InstallClientPluginResponse();
    $res->pluginInstaller =
      file_get_contents($GLOBALS['dces_dir_client_plugins'] . '/' . $request->clientPluginAlias . '.jar')
        or throwError("Failed to send plugin with alias '" . $request->clientPluginAlias . "' not found");

    return $res;
  }

?>