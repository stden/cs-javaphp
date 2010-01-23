<?php

class AvailableContestsRemoveContestTestCase extends DCESWithSuperAdminTestCase {
    
    protected $CDs = array();
    
    public function setUp() 
    {
        parent::setUp();

        //create contests and store testdata to check against later
        for($i = 0; $i < rand(TestData::RANDOM_TESTS_NUMBER/2, TestData::RANDOM_TESTS_NUMBER); $i++) {
            
            $cd = new ContestDescription();
            
            //create contest descr
            
            $cd->name = TestData::genUnicodeStr(TestData::MAX_DATA_LENGTH);
            $cd->description = TestData::genUnicodeStr(TestData::MAX_DATA_LENGTH);
            
            $int = time()/TestData::TIME_SCALE;
            
            $cd->start = time() + rand(-$int, $int);
            $cd->finish = $cd->start + rand(1, $int);
            $cd->registrationType = TestData::getRandomValue(TestData::getData('registrationType'));
            
            $ct = new ContestTiming();
            $ct->contestEndingStart = rand(1, ($cd->finish - $cd->start)/60);
            $ct->contestEndingFinish = rand(1, $int);
            $ct->maxContestDuration = rand(1, ($i % 2 == 1) ? $cd->finish - $cd->start : $int);
            $ct->selfContestStart = $i % 2 == 1 ? true : false;
            
            $cd->contestTiming = $ct;
            
            $udfs = array();
            for($j = 0; $j < rand(1, TestData::MAX_USER_DATA_FIELDS); $j++)
            {
                $udf = new UserDataField();
                
                $udf->compulsory = rand(0, 1) ? true : false;
                $udf->data = TestData::genUnicodeStr(TestData::MAX_DATA_LENGTH);
                $udf->data = TestData::genUnicodeStr(5);
                $udf->showInResult = rand(0, 1) ? true: false;
                
                $udfs[] = $udf;
            }
            
            $cd->data = $udfs;
            
            $ar = TestData::getData('accessPermission');

            $rap = new ResultsAccessPolicy();
            $rap->afterContestPermission = TestData::getRandomValue($ar);
            $rap->contestEndingPermission = TestData::getRandomValue($ar);
            $rap->contestPermission = TestData::getRandomValue($ar);
  
            $cd->resultsAccessPolicy = $rap;          
            
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
        $del_num = rand(1, sizeof($this->CDs));
        
        for($i = 0; $i < $del_num; $i++) {
         
            $id = rand(0, sizeof($this->CDs) - 1);
         
            $this->apiRemoveContest(array('contestID'=>$this->CDs[$id]->contestID));
         
            unset($this->CDs[$id]);
            $this->CDs = array_values($this->CDs);
        }
            
        $req = new AvailableContestsRequest();
        
        $res = RequestSender::send($req);
        
        $this->assertEquals(array_values($this->CDs), $res->contests);
    }    
}

?>
