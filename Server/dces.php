<?php

require("dces-settings.php");
require("utils/Debug.php");
require("utils/ReturnError.php");
require("utils/Directories.php");
require("utils/DataBase.php");
require("utils/RequestUtils.php");
require("mocks/all_mocks.php");
require("utils/DcesDateTime.php");

$x = @file_get_contents('php://input');
if (strpos($x, 'x=') === 0)
    $s_request = substr($x, 2);
else
{
    echo "<strong>DCES</strong> Здесь был какой-то текст, который больше не прочитать из-за испорченной кодировки\n";
    exit();
}

//$s_request = prepare($_REQUEST['x']);

//open log file
if (SYS_LOG_ENABLED) {
    $log = fopen("messages.log", "a");
    fwrite($log, "request = $s_request\n");
}

$request = @unserialize($s_request) or throwBusinessLogicError(15);

switch (get_class($request)) {
    case 'AvailableContestsRequest':
        require("requests/AvailableContests.php");
        $result = processAvailableContestsRequest($request);
        break;

    case 'CreateContestRequest':
        require("requests/CreateContest.php");
        $result = processCreateContestRequest($request);
        break;

    case 'ConnectToContestRequest':
        require("requests/ConnectToContest.php");
        $result = processConnectToContestRequest($request);
        break;

    case 'DisconnectRequest':
        require("requests/Disconnect.php");
        $result = processDisconnectRequest($request);
        break;

    case 'AdjustContestRequest':
        require("requests/AdjustContest.php");
        $result = processAdjstContestRequest($request);
        break;

    case 'GetContestDataRequest':
        require("requests/GetContestData.php");
        $result = processGetContestDataRequest($request);
        break;

    case 'SubmitSolutionRequest':
        require("requests/SubmitSolution.php");
        $result = processSubmitSolutionRequest($request);
        break;

    case 'RegisterToContestRequest':
        require("requests/RegisterToContest.php");
        $result = processRegisterToContestRequest($request);
        break;

    case 'DownloadPluginRequest':
        require("requests/DownloadPlugin.php");
        $result = processDownloadPluginRequest($request);
        break;

    case 'CreateDataBaseRequest':
        require("requests/CreateDataBase.php");
        $result = processCreateDataBaseRequest($request);
        break;

    case 'GetUsersRequest':
        require("requests/GetUsers.php");
        $result = processGetUsersRequest($request);
        break;

    case 'RemoveUserRequest':
        require("requests/RemoveUser.php");
        $result = processRemoveUserRequest($request);
        break;

    case 'RemoveContestRequest':
        require("requests/RemoveContest.php");
        $result = processRemoveContestRequest($request);
        break;

    case 'AdjustPluginRequest':
        require("requests/AdjustPlugin.php");
        $result = processAdjustPluginRequest($request);
        break;

    case 'AdjustUserDataRequest':
        require("requests/AdjustUserData.php");
        $result = processAdjustUserDataRequest($request);
        break;

    case 'RemovePluginRequest':
        require("requests/RemovePlugin.php");
        $result = processRemovePluginRequest($request);
        break;

    case 'GetContestResultsRequest':
        require("requests/GetContestResults.php");
        $result = processGetContestResultsRequest($request);
        break;

    case 'StopContestRequest':
        require("requests/StopContest.php");
        $result = processStopContestRequest($request);
        break;

    case 'KillDBRequest':
        require("requests/KillDB.php");
        $result = processKillDBRequest($request);
        break;

    case 'AvailablePluginsRequest':
        require("requests/AvailablePlugins.php");
        $result = processAvailablePluginsRequest($request);
        break;

    case 'CheckerRequest':
        require("requests/Checker.php");
        $result = processCheckerRequest($request);
        break;

    default:
        throwBusinessLogicError(15, get_class($request));
}
;

Data::execPendingQueries();

$nil = serialize(null);
$s_result = serialize($result);
//echo $magic;
echo $nil; //means no error
echo $s_result;

if (SYS_LOG_ENABLED) {
    fwrite($log, "answer = $s_result\n\n");
    fclose($log);
}
?>