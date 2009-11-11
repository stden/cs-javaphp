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

?>