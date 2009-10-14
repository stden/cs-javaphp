<?php

require_once('DCESTestCase.php');

class CreateContestTestCase extends DCESTestCase
{
    protected $sessionID;
    protected $contest;
    protected $connToContest;
    
    protected function setUp()
    {
        /*$this->contest = Constructor::instance($this)->construct('CreateContest');
        $this->connToContest = Constructor::instance($this)->construct('ConnectToContest');
        
        $this->sessionID = $connToContest->set('login', 'admin')->set('password', 'superpassword')->set('contestID', 0)->send()->
                     assertNotError()->get('sessionID');
        
        $this->contest = $this->contest->set('sessionID', $this->sessionID);*/
    }
    
    public function testEmptyContest()
    {
        $c = Constructor::instance($this);
        
        $c->construct('CreateContest');
        
        //$this->contest->set('name', 'blablabla')->send()->assertNotError();    
    }
    
}

?>