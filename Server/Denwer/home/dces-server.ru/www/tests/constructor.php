<?php

require_once('../utils/Messages.php');
require_once('../requests/CreateDataBase.php');
require_once('post_request.php');
require_once('../utils/logger.php');
require_once('data/mocks.php');

// Accepted response is 'N;'.serialize(new AcceptedResponse()); 

class Constructor
{
    private $serverURL = "http://localhost/dces/dces.php"; //TODO: refactor to load from config file
    private static $inst; //array<test cases, contsructor instances>
    private $test;

    protected function __construct($test)
    {
        $this->test = $test;
    }
   
    public static function instance($test){
        
        $className = get_class($test);
        
        if(!isset(Constructor::$inst[$className]))
            Constructor::$inst[$className] = new Constructor($test);
        
        return Constructor::$inst[$className];
    }
    
    /**
     * @return 
     * @param string $name A DTO name to construct
     */
    public function construct($name)
    {
        $obj = new $name();
        $this->_construct($obj, $name);
        
        return $obj;
    }
    
    private function _construct(&$obj, $name)
    {
        $val = Mocks::m()->expand($name);
        
        if(!$val) return;
        
        foreach($val[1] as $key => $value)
            if(is_array($value)){
                
                $obj->$key = new $value[0]();
                $this->_construct($obj->$key, $value[0]);
            } 
            else
                $obj->$key = $value;
    }
    
    
    //FOR REFERENCE ONLY   
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