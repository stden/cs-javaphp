<?php

$dces_dir_root = dirname(__FILE__) . '/..';

$dces_dir_server_plugins = "$dces_dir_root/server_plugins";
$dces_dir_client_plugins = "$dces_dir_root/client_plugins";
$dces_dir_temp = "$dces_dir_root/temp";
$dces_dir_problems = "$dces_dir_root/problems";
$dces_server_plugin_file = "$dces_dir_root/utils/ServerPlugin.php";

function getServerPluginFile($alias = '') {
    if ($alias === '')
        return $GLOBALS['dces_server_plugin_file'];
    else {
        $res = $GLOBALS['dces_dir_server_plugins'] . '/' . $alias . '.php';
        if (!file_exists($res)) throwBusinessLogicError(5);
        return $res;
    }
}

/**
 * returns a temporary file for the problem with the specified id 
 * @param $id
 * @return unknown_type
 */
function getTemporaryProblemFile() {	
	return $GLOBALS['dces_dir_temp'] . '/' . RequestUtils::random_str(10) . '.problem';
}

/**
 * returns the file for the problem with the specified id 
 * @param $id
 * @return unknown_type
 */
function getProblemFile($id) {	
	return $GLOBALS['dces_dir_temp'] . "/$id.problem";
}

?>