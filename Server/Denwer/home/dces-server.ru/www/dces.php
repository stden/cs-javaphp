<?php

require("Messages.php");

function prepare( $a ){
  $a = str_replace('\0',chr(0),$a);
  $a = str_replace('\"','"',$a);
  $a = str_replace("\'","'",$a);
  $a = str_replace("\\\\","\\",$a);  	
  return $a;
}
  

if(!isset($_REQUEST['x'])){
  echo "Должен быть параметр!";
  die();
}

$req = prepare($_REQUEST['x']);

$log = fopen("requests.log", "a");
fwrite($log, "request = $req\n");

$s = unserialize($req);	
switch(get_class($s)){
  case 'AvailableContestsRequest': 
    $res = processAvailableContestRequest($s);
	break;

  case 'CreateContestRequest':
	$res = new AcceptedResponse(); 
	break;  

  default:
	$res = 'Unknown message type "'.get_class($s).'"';
};
$ans = serialize($res);
echo $ans;

fwrite($log, "answer = $ans\n");
fclose($log);
?>