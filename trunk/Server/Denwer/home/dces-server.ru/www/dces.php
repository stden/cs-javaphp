<?php

require("Debug.php");

require("dces-settings.php");
require("Directories.php");
require("DataBase.php");
require("Authorization.php");
require("Messages.php");
require("SQLDateTime.php");

require("ReturnError.php");

function prepare( $a ){
  $a = str_replace('\0',chr(0),$a);
  $a = str_replace('\"','"',$a);
  $a = str_replace("\'","'",$a);
  $a = str_replace("\\\\","\\",$a);  	
  return $a;
}

$x = @file_get_contents('php://input');
if (strpos($x, 'x=') === 0)
 $s_request = substr($x, 2);
else
{
  echo "DCES ������ 0.1, ����� ����������\n";
  echo "��� ��������� � DCES-������� ���� �� ������������";
  exit();
}

//$s_request = prepare($_REQUEST['x']);

//open log file
$log = fopen("messages.log", "a");
fwrite($log, "request = $s_request\n");

$request = unserialize($s_request) or throwError('Failed to understand the request');

switch(get_class($request)){
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

  default:
	  $result = 'Unknown message type "'.get_class($s).'"';
};

$s_result = serialize($result);
echo $s_result;

fwrite($log, "answer = $s_result\n\n");
fclose($log);
?>