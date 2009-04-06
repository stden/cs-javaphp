<?php

require("Debug.php");

require("ReturnError.php");

require("dces-settings.php");
require("Directories.php");
require("DataBase.php");
require("Authorization.php");
require("Messages.php");
require("SQLDateTime.php");

$x = @file_get_contents('php://input');
if (strpos($x, 'x=') === 0)
 $s_request = substr($x, 2);
else
{
  echo "<b>DCES</b> версии 0.1, добро пожаловать\n";
  echo "Веб интерфейс к DCES-серверу пока не предусмотрен";
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
      require("AvailableContests.php");
      $result = processAvailableContestsRequest($request);
	  break;

	case 'CreateContestRequest':
	  require("CreateContest.php");
      $result = processCreateContestRequest($request);
	  break;

	case 'ConnectToContestRequest':
	  require("ConnectToContest.php");
      $result = processConnectToContestRequest($request);
	  break;

	case 'DisconnectRequest':
	  require("Disconnect.php");
	  $result = processDisconnectRequest($request);
	  break;

	case 'AdjustContestRequest':
	  require("AdjustContest.php");
	  $result = processAdjstContestRequest($request);
	  break;

	case 'GetContestDataRequest':
	  require("GetContestData.php");
	  $result = processGetContestDataRequest($request);
	  break;

	case 'SubmitSolutionRequest':
	  require("SubmitSolution.php");
	  $result = processSubmitSolutionRequest($request);
	  break;

	case 'RegisterToContestRequest':
	  require("RegisterToContest.php");
	  $result = processRegisterToContestRequest($request);
	  break;

	case 'InstallClientPluginRequest':
	  require("InstallClientPlugin.php");
	  $result = processInstallClientPluginRequest($request);
	  break;

	case 'CreateDataBaseRequest':
	  require("CreateDataBase.php");
	  $result = processCreateDataBaseRequest($request);
	  break;

	case 'GetUsersRequest':
	  require("GetUsers.php");
	  $result = processGetUsersRequest($request);
	  break;

	case 'RemoveUserRequest':
	  require("RemoveUser.php");
	  $result = processRemoveUserRequest($request);
	  break;

	case 'RemoveContestRequest':
	  require("RemoveContest.php");
	  $result = processRemoveContestRequest($request);
	  break;

	case 'AdjustClientPluginRequest':
	  require("AdjustClientPlugin.php");
	  $result = processAdjustClientPluginRequest($request);
	  break;

	case 'RemoveClientPluginRequest':
	  require("RemoveClientPlugin.php");
	  $result = processRemoveClientPluginRequest($request);
	  break;

   case 'GetContestResultsRequest':
	  require("GetContestResults.php");
	  $result = processGetContestResultsRequest($request);
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