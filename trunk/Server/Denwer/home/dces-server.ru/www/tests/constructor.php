<?php

require_once('mock_ups.php');
require_once('../requests/CreateDataBase.php');
require_once('post_request.php');

class Constructor
{
    //private $mirrorCommands = array(); //storing SQL anti-commands here
    //private $currentErrCode = ''; //store current error msg from server here
    
    private $serverURL = "http://localhost/dces/dces.php";

    public function createDatabase()
    {
        $request = new CreateDataBaseRequest();
        $request->login = "admin";
        $request->password = "superpassword";
        
        return $this->sendRequestObject($request);
    }
    
    public function getAcceptedResponse()
    {
        return 'N;'.serialize(new AcceptedResponse());
    } 
    
    private function sendRequestObject($reqObj)
    {
        return PostRequest::doPostRequest($this->serverURL, 'x='.serialize($reqObj));
    }
}

?>