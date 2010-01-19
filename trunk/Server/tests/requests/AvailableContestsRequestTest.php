<?php

class AvailableContestsTestCase extends DCESWithDBTestCase {
    
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
            $cd->finish = $start + rand(1, $int);
            $cd->registrationType = TestData::getRandomValue(TestData::getData('registrationType'));
            
            $ct = new ContestTiming();
            $ct->contestEndingStart = rand(1, ($finish - $start)/60);
            $ct->contestEndingFinish = rand(1, $int);
            $ct->maxContestDuration = rand(1, ($i % 2 == 1) ? $finish - $start : $int);
            $ct->selfContestStart = $i % 2 == 1 ? true : false;
            
            $cd->contestTiming = $ct;
            
            $udfs = array();
            for($i = 0; $i < rand(1, TestData::MAX_USER_DATA_FIELDS); $i++)
            {
                $udf = new UserDataField();
                
                $udf->compulsory = rand(0, 1) ? true : false;
                $udf->data = TestData::genUnicodeStr(TestData::MAX_DATA_LENGTH);
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
            
            //copy object to array
            $this->CDs[] = unserialize(serialize($cd));
                                
            //create contest with cd data
            $this->apiCreateContest(array('contestDescription'=>$cd));
        }
    }        
}

?>
