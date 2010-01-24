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
}

?>
