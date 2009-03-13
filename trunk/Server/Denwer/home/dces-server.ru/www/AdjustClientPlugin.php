<?php

  function processAdjustClientPluginRequest($request) {
    $prfx = $GLOBALS['dces_mysql_prefix'];

    $con = connectToDB();
    $user_row = testSession($con, $request->sessionID);

    //authorize
    if ($user_row['user_type'] !== 'SuperAdmin')
      throwBusinessLogicError(0);

    //test if there already is such plugin
    $where_clause = sprintf("alias=%s", quote_smart($request->pluginAlias));
    $find_rows = mysql_query("SELECT * FROM ${prfx}client_plugin WHERE $where_clause", $con)
                   or throwServerProblem(39, mysql_error());

    if (mysql_fetch_array($find_rows))
      $modify = true;
    else
      $modify = false;

    if (!$modify && (is_null($request->pluginData) || is_null($request->pluginData)))
      throwBusinessLogicError(1);

    //TODO test pluginAlias to be secure
    //set file data                                    
    if (!is_null($request->pluginData)) {
      file_put_contents(
        $GLOBALS['dces_dir_client_plugins'] . '/' . $request->pluginAlias . '.jar',
        $request->pluginData
      );
    }

    //prepare set description plugin
    $col_value = array();
    if (!is_null($request->description))
      $col_value['description'] = $request->description;

    if ($modify)
      $query = composeUpdateQuery(
                 'client_plugin',
                 $col_value,
                 $where_clause
               );
    else {
      $col_value['alias'] = $request->pluginAlias;
      $query = composeInsertQuery('client_plugin', $col_value);
    }

    mysql_query($query, $con) or throwServerProblem(51, mysql_error());

    return new AcceptedResponse();
  }

?>