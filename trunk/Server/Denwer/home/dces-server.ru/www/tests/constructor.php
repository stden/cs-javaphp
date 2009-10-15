<?php

require_once('../utils/Messages.php');
require_once('../requests/CreateDataBase.php');
require_once('post_request.php');
require_once('MessageWrapper.php');
require_once('../utils/logger.php');

class Constructor
{
    private $serverURL = "http://localhost/dces/dces.php";
    
    private static $inst; //array<test cases, contsructor instances>
    
    private $test;
    private $xml_mocks;

    /*private function getAcceptedResponse()
    {
        return 'N;'.serialize(new AcceptedResponse());
    }*/
   
    protected function __construct($test)
    {
        $this->test = $test;
        $this->xml_mocks = './data/mocks.xml';
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
     * @param array $params Additional parameters
     */
    public function construct($name, $params = array())
    {
        $obj = null;
        
        if(!file_exists($this->xml_mocks))
            throw new Exception("Couldn't load XML models");
        
        $xml = simplexml_load_file($this->xml_mocks);
        
        $obj_type = $xml->object->type;
        
        Logger::L("C:\DCES_log.txt")->log((int)$xml);
        
        //$obj = new $obj_type();
        
        for($i = 0; $i < count($xml->param); $i++)
        {
            foreach($xml->param[$i] as $name => $value)
            {
                switch($name)
                {
                    case '': echo 'foo: '.$value; break;
                }
            }
        }
        
        if(isset($params[$name]) && $params[$name] != null)
        {
            foreach($params[$name] as $paramName => $paramValue)
                $obj->$paramName = $paramValue;
        }
        
        
       /* switch($name)
        {
            case 'ContestDescription':
                $obj->contestID = -1;
                $obj->name = $name;
                $obj->description = $obj;
                $obj->start = !$start ? time() : $start;
                $obj->finish = !$finish ? $start + 1*60*60: $finish; //start + 1 hour
                $obj->registrationType = $regType;
                $obj->data = $userData;
                $obj->contestTiming = $ct;
                break;
            
            case 'ContestTiming':
                $obj->selfContestStart = false;
                $obj->contestEndingStart = 0;
                $obj->contestEndingFinish = 0;
                break;
            case 'ResultsAccessPolicy':
                $obj = simplexml_load_file();
        }
        */
       
        $wrapper = new MessageWrapper($obj, $this->test);
        
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