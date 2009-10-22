<?php

class Mocks 
{
    private $req_mocks;
    private $req_bindings;
    private static $inst;
    
    private function __construct()
    {
        $this->req_mocks = array();
        
        $this->req_mocks['ContestTiming'] = array('selfContestStart' => false,
                                                  'contestEndingStart' => 0,
                                                  'contestEndingFinish' => 0);
                                                                   
        $this->req_mocks['ResultsAccessPolicy'] = array('contestPermission' => 'FullAccess',
                                                  'contestEndingPermission' => 'FullAccess',
                                                  'afterContestPermission' => 'FullAccess');

        //Additional data structures
        $this->req_mocks['ContestDescription'] = array('contestID' => -1,
                                                       'name' => 'name',
                                                       'description' => 'description',
                                                       'start' => time(),
                                                       'finish' => time() + 1*60*60,
                                                       'registrationType' => 'Self',
                                                       'data' => 'boo',
                                                       'contestTiming' => $this->expand('ContestTiming'),
                                                       'resultsAccessPolicy' => $this->expand('ResultsAccessPolicy'));
                                                         
                                                  
        $this->req_mocks['CreateDataBaseRequest'] = array('login' => 'admin', 'password' => 'superpassword');
        $this->req_mocks['CreateContestRequest'] = array('sessionID' => -1, 'contest' => $this->expand('ContestDescription'));
    }
    
    public static function m()
    {
        if(!isset(Mocks::$inst))
            return Mocks::$inst = new Mocks();
        else
            return Mocks::$inst;
    }
    
    public function expand($name)
    {
        return isset($this->req_mocks[$name]) ? array($name, $this->req_mocks[$name]) : false;
    }
}

/*

$ct->selfContestStart = false;
$ct->contestEndingStart = 0;
$ct->contestEndingFinish = 0;

$rap->contestPermission = 'FullAccess';
$rap->contestEndingPermission = 'FullAccess';
$rap->afterContestPermission= 'FullAccess';

<object type="ContestDescription">
    <param name="contestID" type="int" value="-1"></param>
    <param name="name" value="name"></param>
    <param name="description" value="description" ></param>
    <param name="start" type="script" value="time()" ></param>
    <param name="finish" type="script" value="time() + 1*60*60" ></param>
    <param name="registrationType" value="Self"></param>
    <param name="data" type="script" value="array()" ></param>
    <param name="contestTiming" type="object" value="ContestTiming" ></param>
</object>
*/

?>
