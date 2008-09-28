<?php

function prepare( $a ){
  $a = str_replace('\0',chr(0),$a);
  $a = str_replace('\"','"',$a);
  $a = str_replace("\'","'",$a);
  $a = str_replace("\\\\","\\",$a);  	
  return $a;
}
  
// Registered message types
class AcceptedResponse{};
class AdjustContestRequest{};
class AvailableContestsRequest{};
class AvailableContestsResponse{};
class ChangePasswordRequest{};
class ConnectToContestRequest{};
class ConnectToContestResponse{};
class ContestDescription{};
class CreateContestRequest{};
class CreateUserRequest{};
class DisconnectRequest{};
class GetContestDataRequest{};
class GetContestDataResponse{};
class GetUsersRequest{};
class GetUsersResponse{};
class InstallClientPluginRequest{};
class InstallClientPluginResponse{};
class ProblemDescription{};
class RegisterToContestRequest{};
class RemoveClientPluginRequest{};
class RemoveContestRequest{};
class RemoveUserRequest{};
class RequestFailedResponse{};
class RestorePasswordRequest{};
class SubmitSolutionRequest{};
class SubmitSolutionResponse{};
class UploadClientPluginRequest{};
class UserDescription{};
class Sum{};
 
if(!isset($_REQUEST['x'])){
  echo "Должен быть параметр!";
  die();
}
$s = unserialize(prepare($_REQUEST['x']));	
switch(get_class($s)){
  case 'Sum': $res = $s->a + $s->b; break;
  case 'AvailableContestsRequest': 
	$res = new AvailableContestsResponse(); 
    $res->contests = array();
	$c1 = new ContestDescription();
	$c1->name = "Example contest #1";
	$res->contests[] = $c1;
	$c2 = new ContestDescription();
	$c2->name = "Example contest #2";
	$res->contests[] = $c2;
	break;
  case 'CreateContestRequest':
	$res = new AcceptedResponse(); 
	break;  
  default: 
	$res = 'Unknown message type "'.get_class($s).'"';
};
echo serialize($res);
?>
