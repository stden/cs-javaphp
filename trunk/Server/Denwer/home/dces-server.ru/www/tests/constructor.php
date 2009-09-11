<?php

require_once('../utils/Messages.php');
require_once('../requests/CreateDataBase.php');
require_once('post_request.php');

class Constructor
{
    private $serverURL = "http://localhost/dces/dces.php";
    
    private static $inst; //array<test cases, contsructor instances>
    
    private $test;

    /*private function getAcceptedResponse()
    {
        return 'N;'.serialize(new AcceptedResponse());
    }*/
   
    protected function __construct($test)
    {
        $this->test = $test;
    }
   
    public static function instance($test){
        if(!isset($this->inst[$test]))
            $this->inst[$test] = new Constructor($test);
        
        return $this->inst[$test];
    }
    
    public function construct($name)
    {
        $obj = new $name();
        
        //if (NAME==to-to) fill tak-to else if ...
        
        $wrapper = new MessageWrapper($name, $this->test);
        
        return $wrapper; 
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