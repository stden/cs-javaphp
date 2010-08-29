<?php

  function processAdjustPluginRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    $con = connectToDB();
    $user_row = RequestUtils::testSession($request->sessionID);       

    //authorize
    if ($user_row['user_type'] !== 'SuperAdmin')
      throwBusinessLogicError(0);
      
    //test plugin alias
    if (preg_match('/^[\p{L}0-9 ]+$/', $request->pluginAlias) === 0)
        throwBusinessLogicError(238);
      
    $plugin_type = $request->side === 'Client' ? 'client' : 'server';

    //test if there already is such plugin
    $where_clause = sprintf("alias=%s", Data::quote_smart($request->pluginAlias));
    $find_rows = mysql_query("SELECT * FROM ${prfx}${plugin_type}_plugin WHERE $where_clause", $con)
                   or throwServerProblem(39, mysql_error());

    if (mysql_fetch_array($find_rows))
      $modify = true;
    else
      $modify = false;

    //test all parameters specified
    if (!$modify && is_null($request->description))
      $request->description = "";
    if (!$modify && (is_null($request->pluginData) || is_null($request->description)))
      throwBusinessLogicError(1);

    //TODO test pluginAlias to be secure      

    if ($plugin_type === 'client')
        $ext = '.jar';
    else
        $ext = '.php';

    //set file data                                    
    if (!is_null($request->pluginData)) {
      file_put_contents(
        $GLOBALS["dces_dir_${plugin_type}_plugins"] . '/' . $request->pluginAlias . $ext,
        $request->pluginData
      );
    }

    //prepare set plugin description
    $col_value = array();
    if (!is_null($request->description))
      $col_value['description'] = $request->description;

    if ($modify)
      $query = composeUpdateQuery(
                 "${plugin_type}_plugin",
                 $col_value,
                 $where_clause
               );
    else {
      $col_value['alias'] = $request->pluginAlias;
      $query = composeInsertQuery("${plugin_type}_plugin", $col_value);
    }

    mysql_query($query, $con) or throwServerProblem(51, mysql_error());

    return new AcceptedResponse();
  }

?>