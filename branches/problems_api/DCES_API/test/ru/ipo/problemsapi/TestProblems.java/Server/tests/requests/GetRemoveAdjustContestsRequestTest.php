<?php

class AvailableContestsRemoveContestTestCase extends DCESWithSuperAdminTestCase {
    
    protected $CDs = array();
    
    public function setUp() 
    {
        parent::setUp();

        //create contests and store testdata to check against later
        for($i = 0; $i < TestData::RANDOM_TESTS_NUMBER; $i++) {
            
            $cd = TestData::genContestDecription($i);          
            
            //create contest with cd data
            $res = $this->apiCreateContest(array('contest'=>$cd));
            
            $cd->contestID = $res->createdContestID;
            //copy object to array
            $this->CDs[] = unserialize(serialize($cd));
        }
    }
    
    public function testCreatedContestsArePresent()
    {
        $req = new AvailableContestsRequest();
        
        $res = RequestSender::send($req);
        
        $this->assertEquals(array_values($this->CDs), $res->contests);
    }
    
    public function testContestsListChangesAfterRemove()
    {
        $num_of_deleted = rand(1, sizeof($this->CDs));
        
        for($i = 0; $i < $num_of_deleted; $i++) {
         
            $id = rand(0, sizeof($this->CDs) - 1);
         
            $this->apiRemoveContest(array('contestID'=>$this->CDs[$id]->contestID));
         
            unset($this->CDs[$id]);
            $this->CDs = array_values($this->CDs); //re-index array
        }
            
        $req = new AvailableContestsRequest();
        
        $res = RequestSender::send($req);
        
        $this->assertEquals(array_values($this->CDs), $res->contests);
    }
    
    /**
    * @dataProvider sessionContestProvider 
    */ 
    public function testBadContextForRemoveContestRequest($sessionID, $contestID) {
       
       $sessionID = ($sessionID == 'session_ok') ? $this->sessionID : $sessionID;
       $contestID = ($contestID == 'contest_ok') ? $this->CDs[rand(0, sizeof($this->CDs) - 1)]->contestID : $contestID;

       $req = TestData::fillRequest('RemoveContestRequest', array('contestID'=>$contestID, 'sessionID'=>$sessionID));
       
       $this->assertClassEquals(new RequestFailedResponse(), RequestSender::send($req));
    }    
    
    public function sessionContestProvider() 
    {
        $bad = array(null, '', 0, TestData::genASCIIStr(24), 'session_ok', 'contest_ok');
        
        for($i = 0; $i < sizeof($bad); $i++)
            for($k = 0; $k < sizeof($bad); $k++)
                if($bad[$i] != 'session_ok' || $bad[$k] != 'contest_ok')
                    $res[] = array($bad[$i], $bad[$k]);
                
        return $res;    
    }
}

?>
