<?php

require("Debug.php");

require("dces-settings.php");
require("DataBase.php");
require("Messages.php");
require("Sessions.php");
require("SQLDateTime.php");

require("ReturnError.php");

require("AvailableContests.php");
require("CreateContest.php");
require("ConnectToContest.php");

function prepare( $a ){
  $a = str_replace('\0',chr(0),$a);
  $a = str_replace('\"','"',$a);
  $a = str_replace("\'","'",$a);
  $a = str_replace("\\\\","\\",$a);  	
  return $a;
}

if(!isset($_REQUEST['x'])){
/*
  echo "DCES ������ 0.1, ����� ����������\n";
  echo "��� ��������� � DCES-������� ���� �� ������������";
  exit();
*/

//test requests

$con = connectToDB();
echo createSession($con, 1);
exit();
}

$s_request = prepare($_REQUEST['x']);

//open log file
$log = fopen("messages.log", "a");
fwrite($log, "request = $s_request\n");

$request = unserialize($s_request);

switch(get_class($request)){
  case 'AvailableContestsRequest': 
    $result = processAvailableContestsRequest($request);
	  break;

	case 'CreateContestRequest': 
    $result = processCreateContestRequest($request);
	  break;

	case 'ConnectToContestRequest':
    $result = processConnectToContestRequest($request);
	  break;

  default:
	  $result = 'Unknown message type "'.get_class($s).'"';
};

$s_result = serialize($result);
echo $s_result;

fwrite($log, "answer = $s_result\n\n");
fclose($log);
?>