<?php

class CreateContestRequestTestCase extends DCESWithAllRolesTestCase {
    
    public function setUp() 
    {
        parent::setUp();
    }
    
    private function createContest($descr, $isGood, $info = '') 
    {
        $req = new CreateContestRequest();
        
        $req->sessionID = $this->connect->sessionID;
        $req->contest = $descr;
        
        $res = RequestSender::send($req);
        
        if($isGood)
            $this->assertNotEquals($res->createdContestID, null);
        else
            $this->assertEquals(createFailRes(15, $info), $res);
    }
    
    /**
    * @dataProvider userDataFieldDataProvider
    */
    public function testUserDataFieldAdjustedContest($isGood, $fields) 
    {
        $cd = new ContestDescription();
        $cd->data = $fields;

        $this->createContest($cd, $isGood);
    }
    
    /**
    * @dataProvider generalParametersDataProvider
    */
    public function testGeneralContestParameters($isGood, $name, $description) 
    {
        $cd = new ContestDescription();
        $cd->name = $name;
        $cd->description = $description;
        
        $this->createContest($cd, $isGood);
    }
    
    /**
    * @dataProvider registrationTypeDataProvider
    */
    public function testRegistrationTypeAdjustedContest($isGood, $regType) 
    {
        $cd = new ContestDescription();
        $cd->registrationType = $regType;
        
        $this->createContest($cd, $isGood);
    }
    
    /**
     * @dataProvider contestTimingDataProvider
     */
    public function testContestTimingAdjustedContest($isGood, $selfStart, $s, $f, $eS, $eF, $maxDur) 
    {
        $ct = new ContestTiming();
        $ct->selfContestStart = $selfStart;
        $ct->maxContestDuration = $maxDur;
        $ct->contestEndingStart = $eS;
        $ct->contestEndingFinish = $eF;
        
        $cd = new ContestDescription();
        $cd->start = $s;
        $cd->finish = $f;
        $cd->contestTiming = $ct;
        
        $this->createContest($cd, $isGood, 'contest may not start after its finish');
    }
    
    /**
     * @dataProvider resultsAccessPolicyDataProvider
     */
    public function testResultsAccessPolicyAdjustedContest($isGood, $after, $during, $ending) 
    {
        $rap = new ResultsAccessPolicy();
        $rap->afterContestPermission = $after;
        $rap->contestPermission = $during;
        $rap->contestEndingPermission = $ending;
        
        $cd = new ContestDescription();
        $cd->resultsAccessPolicy = $rap;
        
        $this->createContest($cd, $isGood);
    }
    
    public function testInvalidSessionIDForZeroContest()
    {
        $req = new CreateContestRequest();
        $cd = new ContestDescription();
        
        $req->sessionID = TestData::genUnicodeStr(rand(1, 48));
        $req->contest = $cd;
        
        $res = RequestSender::send($req);
        
        $this->assertEquals(createFailRes(3), $res);
    }
    
    public function testCreateContestByOtherRolesFailure($sessionID)
    {
        $req = new CreateContestRequest();
        $cd = new ContestDescription();
        
        //contest admin failure
        $req->sessionID = $this->caConnect->sessionID;
        $req->contest = $cd;
        $this->assertEquals(createFailRes(0), RequestSender::send($req));

        //participant failure
        $req->sessionID = $this->pConnect->sessionID;
        $req->contest = $cd;
        $this->assertEquals(createFailRes(0), RequestSender::send($req));
        
        //null failure
        $req->sessionID = null;
        $req->contest = $cd;
        $this->assertEquals(createFailRes(0), RequestSender::send($req));
    }
        
    /* ================= Data providers ================ */
    
    public function contestTimingDataProvider() 
    {
        $res = array();
        
        $now = time();
        $int = $now/TestData::TIME_SCALE;
        
        //generate 'good' data
        for($i = 0; $i < TestData::RANDOM_TESTS_NUMBER; $i++) {
            
            $start = $now + rand(-$int, $int);
            $finish = $start + rand(1, $int);
            $contestEndingStart = rand(1, ($finish - $start)/60);
            $contestEndingFinish = rand(1, $int);
            
            $maxContestDuration = rand(1, ($i % 2 == 1) ? $finish - $start : $int);
            
            $res[] = array(GOOD_DATA, $i % 2 == 1 ? TRUE : false, $start, $finish, $contestEndingStart, $contestEndingFinish, $maxContestDuration);
        }
        
        //generate 'bad' data
        for($i = 0; $i < TestData::RANDOM_TESTS_NUMBER; $i++) {

            $start = $now + rand(-$int, $int);
            $finish = $start - rand(1, $int); // start > finish
            
            $l = $start - $finish;
            
            $contestEndingStart = rand(0, 1) ? rand($l, 2*$l)/60 : rand(-2*$l,-$l)/60; // 
            $contestEndingFinish = rand(-2*$l/60, 0);
            
            $maxContestDuration = rand(-$l, 0);
            
            $res[] = array(BAD_DATA, $i % 2 == 1 ? TRUE : FALSE, $start, $finish, $contestEndingStart, $contestEndingFinish, $maxContestDuration);
        }
        
        $res[] = array(BAD_DATA, FALSE, $now, $now, $now, $now, 0);

        $res[] = array(BAD_DATA, FALSE, null, null, null, null, null);
        
        //TODO: test for overloading max integer values;
        
        return $res;
    }
    
    public function resultsAccessPolicyDataProvider()
    {
        $res = TestData::getData('resultsAccessPolicy');
        
        $input = TestData::getData('accessPermission');

        for($i = 0; $i < 3; $i++)
            for($j = 0; $j < 3; $j++)
                for($k = 0; $k < 3; $k++)
                    $res[] = array(GOOD_DATA, $input[$i], $input[$j], $input[$k]);
                    
        return $res;
    }
    
    public function registrationTypeDataProvider()
    {
        return TestData::getData('registrationType');    
    }
    
    public function userDataFieldDataProvider()
    {
        $res = array();
        
        for($i = 0; $i < 2 * TestData::RANDOM_TESTS_NUMBER; $i++) {
            
            $cols = array();
            
            $isGood = TestData::gB();
            
            for($j = 1; $j <= rand(1, TestData::MAX_USER_DATA_FIELDS); $j++)
                if($isGood)
                    $cols[] = array(TestData::genUnicodeStr(rand(1, TestData::MAX_DATA_LENGTH)), TestData::gB(), TestData::gB());
                else
                    $cols[] = array(TestData::getRandomValue(array(null, '', 42)), TestData::gB(), TestData::gB());
            
            $res[] = array($isGood, $cols);
        }
        
        return $res;
    }
    
    public function generalParametersDataProvider() 
    {
        $res = array();
        
        $input = array(null, 42, '');
        
        for($i = 0; $i < 3; $i++)
            for($j = 0; $j < 3; $j++)
                $res[] = array(BAD_DATA, $input[$i], $input[$j]);
        
        $res[] = array(GOOD_DATA, TestData::genUnicodeStr(rand(1, TestData::MAX_DATA_LENGTH)), TestData::genUnicodeStr(rand(1, TestData::MAX_DATA_LENGTH)));
                
        return $res;      
        
    }
}

?>
