<?php

class CreateContestRequestTestCase extends DCESWithSuperAdminTestCase {
    
    public function setUp() 
    {
        parent::setUp();
    }
    
    /**
    * @dataProvider userDataFieldDataProvider
    */
    public function testUserDataFieldAdjustedContest($isGood, $fields) {
        $req = new CreateContestRequest();
        
        $descr = new ContestDescription();    
        $descr->data = $fields;
        
        $req->sessionID = $this->connect->sessionID;
        $req->contest = $descr;
        
        $res = RequestSender::send($req);
        
        if($isGood)
            $this->assertNotEquals($res->createdContestID, null);
        else
            $this->assertEquals(createFailRes(15), $res);
    }
    
    /**
    * @dataProvider generalParametersDataProvider
    */
    public function testGeneralContestParameters($isGood, $name, $description) 
    {
        $req = new CreateContestRequest();
        
        $descr = new ContestDescription();
        $descr->name = $name;
        $descr->description = $description;
        
        $req->sessionID = $this->connect->sessionID;
        $req->contest = $descr;
        
        $res = RequestSender::send($req);
        
        if($isGood)
            $this->assertNotEquals($res->createdContestID, null);
        else
            $this->assertEquals(createFailRes(15), $res);
    }
    
    /**
    * @dataProvider registrationTypeDataProvider
    */
    public function testRegistrationTypeAdjustedContest($isGood, $regType) 
    {
        $req = new CreateContestRequest();
        
        $descr = new ContestDescription();
        $descr->registrationType = $regType;
        
        $req->sessionID = $this->connect->sessionID;
        $req->contest = $descr;
        
        $res = RequestSender::send($req);
        
        if($isGood)
            $this->assertNotEquals($res->createdContestID, null);
        else
            $this->assertEquals(createFailRes(15), $res);
    }
    
    /**
     * @dataProvider contestTimingDataProvider
     */
    public function testContestTimingAdjustedContest($isGood, $selfStart, $s, $f, $eS, $eF, $maxDur) 
    {
        $req = new CreateContestRequest();
        
        $ct = new ContestTiming();
        $ct->selfContestStart = $selfStart;
        $ct->maxContestDuration = $maxDur;
        $ct->contestEndingStart = $eS;
        $ct->contestEndingFinish = $eF;
        
        $descr = new ContestDescription();
        $descr->start = $s;
        $descr->finish = $f;
        $descr->contestTiming = $ct;
        
        $req->sessionID = $this->connect->sessionID;
        $req->contest = $descr;
        
        $res = RequestSender::send($req);
        
        if($isGood)
            $this->assertNotEquals($res->createdContestID, null);
        else
            $this->assertEquals(createFailRes(15, 'contest may not start after its finish'), $res);
    }
    
    /**
     * @dataProvider resultsAccessPolicyDataProvider
     */
    public function testResultsAccessPolicyAdjustedContest($isGood, $after, $during, $ending) 
    {
        
        $req = new CreateContestRequest();
        
        $rap = new ResultsAccessPolicy();
        $rap->afterContestPermission = $after;
        $rap->contestPermission = $during;
        $rap->contestEndingPermission = $ending;
        
        $descr = new ContestDescription();
        $descr->resultsAccessPolicy = $rap;
        
        $req->sessionID = $this->connect->sessionID;
        $req->contest = $descr;
        
        $res = RequestSender::send($req);
        
        if($isGood) 
            $this->assertNotEquals($res->createdContestID, null);
        else
            $this->assertEquals(createFailRes(15), $res);
    }
    
    /* Data providers */
    
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
                    $cols[] = array(TestData::gS(rand(1, TestData::MAX_DATA_LENGTH)), TestData::gB(), TestData::gB());
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
        
        $res[] = array(GOOD_DATA, TestData::gS(rand(1, TestData::MAX_DATA_LENGTH)), TestData::gS(rand(1, TestData::MAX_DATA_LENGTH)));
                
        return $res;      
        
    }
}

?>
