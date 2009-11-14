<?php

require("dces-settings.php");
require("utils/Debug.php");
require("utils/ReturnError.php");
require("utils/Directories.php");
require("utils/DataBase.php");
require("utils/RequestUtils.php");
require("utils/Messages.php");
require("utils/DcesDateTime.php");

$x = @file_get_contents('php://input');
if (strpos($x, 'x=') === 0)
 $s_request = substr($x, 2);
else
{
  echo "<b>DCES</b> Здесь был какой-то текст, который к сожалению, больше не прочитать из-за испорченной кодировки\n";  
  exit();  
}

//$s_request = prepare($_REQUEST['x']);

//open log file
if ($GLOBALS['dces_logging']) {
  $log = fopen("messages.log", "a");
  fwrite($log, "request = $s_request\n");
}

$request = @unserialize($s_request) or throwBusinessLogicError(15);

switch(get_class($request)) {
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

	case 'InstallClientPluginRequest':
	  require("requests/InstallClientPlugin.php");
	  $result = processInstallClientPluginRequest($request);
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

	case 'AdjustClientPluginRequest':
	  require("requests/AdjustPlugin.php");
	  $result = processAdjustPluginRequest($request, 'client');
	  break;

	case 'AdjustServerPluginRequest':
	  require("requests/AdjustPlugin.php");
	  $result = processAdjustPluginRequest($request, 'server');
	  break;

	case 'RemoveClientPluginRequest':
	  require("requests/RemoveClientPlugin.php");
	  $result = processRemoveClientPluginRequest($request);
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

  default:
      throwBusinessLogicError(15, get_class($request));
};

Data::execPendingQueries();

//$magic = chr(4) . chr(2) . chr(3) . chr(9);
$nil = serialize(null);
$s_result = serialize($result);
//echo $magic;
echo $nil; //means no error
echo $s_result;

if ($GLOBALS['dces_logging']) {

  fwrite($log, "answer = $s_result\n\n");
  fclose($log);
}
?>