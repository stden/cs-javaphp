<?php

require_once('../utils/Messages.php');
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

    public function connectToContest($login, $password, $contestID = 0)
    {
        $request = new ConnectToContestRequest();

        $request->login = $login;
        $request->password = $password;
        $request->contestID = $contestID;

        return $this->sendRequestObject($request);
    }

    public function createContest($rap = 0, $ct = 0, $name = 'name', $descr = 'description', $start = 0, $finish = 0, $regType = 'Self', $userData = array())
    {
        $request = new CreateContestRequest(); 

        /*
          public int              contestID;
          public String           name;
          public String           description;
          public Date             start;
          public Date             finish;
          public RegistrationType registrationType;
          public UserDataField[]  data;
          public ResultsAccessPolicy resultsAccessPolicy;
          public ContestTiming contestTiming;
         */

        $d = new ContestDescription();

        $d->contestID = -1;
        $d->name = $name;
        $d->diption = $descr;
        $d->start = !$start ? time() : $start;
        $d->finish = !$finish ? $start + 1*60*60: $finish; //start + 1 hour
        $d->registrationType = $regType;
        $d->data = $userData;

        if(!$ct)
        {
            $ct = new ContestTiming();
            $ct->selfContestStart = false;
            $ct->contestEndingStart = 0;
            $ct->contestEndingFinish = 0;
        }

        $d->contestTiming = $ct;

        if(!$rap)
        {
            $rap = new ResultsAccessPolicy();
            $rap->contestPermission = 'FullAccess';
            $rap->contestEndingPermission = 'FullAccess';
            $rap->afterContestPermission= 'FullAccess';
        }

        $d->resultsAccessPolicy = $rap;

        $request->sessionID = $sID; //TODO: add parametr;
        $request->contest = $d; //TODO: add parametr;

        return $this->sendRequestObject($request);
    }


}

?>