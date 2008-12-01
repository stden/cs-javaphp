<?php

require("Debug.php");

require("dces-settings.php");
require("DataBase.php");
require("Messages.php");
require("Authorization.php");
require("SQLDateTime.php");

require("ReturnError.php");

function prepare( $a ){
  $a = str_replace('\0',chr(0),$a);
  $a = str_replace('\"','"',$a);
  $a = str_replace("\'","'",$a);
  $a = str_replace("\\\\","\\",$a);  	
  return $a;
}

if(!isset($_REQUEST['x'])){

  echo "DCES версии 0.1, добро пожаловать\n";
  echo "Веб интерфейс к DCES-серверу пока не предусмотрен";
  exit();

//test requests

//$con = connectToDB();
//echo createSession($con, 1);
/*
$r = new DisconnectRequest();
$r->sessionID = "RehH0lAw3eaQzHXSK08FwAvW";
require("Disconnect.php");
processDisconnectRequest($r);
*/
//$con = connectToDB();
//exit();
}

$s_request = prepare($_REQUEST['x']);

//open log file
$log = fopen("messages.log", "a");
fwrite($log, "request = $s_request\n");

$request = unserialize($s_request);

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

  default:
	  $result = 'Unknown message type "'.get_class($s).'"';
};

$s_result = serialize($result);
echo $s_result;

fwrite($log, "answer = $s_result\n\n");
fclose($log);
?>