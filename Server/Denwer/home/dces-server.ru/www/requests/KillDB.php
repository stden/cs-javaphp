<?php
/**
 * Created by IntelliJ IDEA.
 * User: ����������
 * Date: 17.04.2009
 * Time: 15:57:17
 * To change this template use File | Settings | File Templates.
 */

function processKillDBRequest($request) {
    $tables = array(
        'client_plugin',
        'contest',
        'problem',
        'problem_status',
        'server_plugin',
        'session',
        'submission_history',
        'user'
    );

    $prfx = $GLOBALS['dces_mysql_prefix'];
    
    foreach ($tables as $t)  
        Data::submitModificationQuery("DROP TABLE $prfx" . $t);
        
    return new AcceptedResponse();
}