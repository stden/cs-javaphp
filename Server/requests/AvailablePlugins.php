<?php
function processAvailablePluginsRequest($request) {
	
	//Uncomment to check permissions	
	$user_row = RequestUtils::testSession($request->sessionID);
	
	if ($user_row['user_type'] !== 'SuperAdmin' && $user_row['user_type'] !== 'ContestAdmin')
		throwBusinessLogicError(0);

    $prfx = $GLOBALS['dces_mysql_prefix'];
    if ($request->pluginSide === 'Client')
    	$table_name = $prfx . "client_plugin";
    else
    	$table_name = $prfx . "server_plugin";          
    
    $rows = Data::getRows("SELECT * FROM $table_name");
    $res = new AvailablePluginsResponse();
    $res->aliases = array();
    $res->descriptions = array();    
    while ($row = Data::getNextRow($rows)) {
		$res->aliases[] = $row['alias'];
		$res->descriptions[] = $row['description'];    	
    }           
    
    return $res;
}