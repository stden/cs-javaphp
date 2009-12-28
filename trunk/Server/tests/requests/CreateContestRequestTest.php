<?php

class CreateContestRequestTestCase extends DCESWithSuperAdminTestCase {
    
    public function setUp() {
        parent::setUp();
    }
    
    /**
     * @dataProvider badResultAccessPolicyDataProvider
     */
    public function testBadResultAccessPolicyAdjustedContest($after, $during, $ending) {
        $this->RAPAdjustedContest(FALSE, $after, $during, $ending);
    }
    
    /**
     * @dataProvider goodResultAccessPolicyDataProvider
     */
    public function testGoodResultAccessPolicyAdjustedContest($after, $during, $ending) {
        $this->RAPAdjustedContest(TRUE, $after, $during, $ending);
    }
    
    public function testRegistrationTypeAdjustedContest() {
    }
    
    public function testUserDataFieldAdjustedContest() {
    }
    
    public function testGeneralContestParameters() {
    }
    
    /**
     * @dataProvider goodContestTimingDataProvider
     */
    public function testGoodContestTimingAdjustedContest($selfStart, $s, $f, $eS, $eF, $maxDur) 
    {
        $this->CTAdjContest(TRUE, $selfStart, $s, $f, $eS, $eF, $maxDur);
    }

    /**
     * @dataProvider badContestTimingDataProvider
     */
    public function testBadContestTimingAdjustedContest($selfStart, $s, $f, $eS, $eF, $maxDur) 
    {
        $this->CTAdjContest(FALSE, $selfStart, $s, $f, $eS, $eF, $maxDur);
    }
    
    private function CTAdjContest($isGood, $selfStart, $s, $f, $eS, $eF, $maxDur) 
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
    
    private function RAPAdjustedContest($isGood, $after, $during, $ending) 
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
    
    public function goodContestTimingDataProvider() 
    {
        
        $res = array();
        
        for($i = 0; $i < TestData::RANDOM_TESTS_NUMBER; $i++) {
            
            $now = time();
            $int = $now/TestData::TIME_SCALE;
            
            $start = $now + rand(-$int, $int);
            $finish = $start + rand(1, $int);
            $contestEndingStart = rand(1, ($finish - $start)/60);
            $contestEndingFinish = rand(1, $int);
            
            $maxContestDuration = rand(1, ($i % 2 == 1) ? $finish - $start : $int);
            
            $res[] = array($i % 2 == 1 ? TRUE : false, $start, $finish, $contestEndingStart, $contestEndingFinish, $maxContestDuration);
        }
        
        return $res;
    }
    
    public function badContestTimingDataProvider() 
    {
        $res = array();
        
        $now = time();
        $int = $now/TestData::TIME_SCALE;
        
        for($i = 0; $i < TestData::RANDOM_TESTS_NUMBER; $i++) {

            $start = $now + rand(-$int, $int);
            $finish = $start - rand(1, $int); // start > finish
            
            $l = $start - $finish;
            
            $contestEndingStart = rand(0, 1) ? rand($l, 2*$l)/60 : rand(-2*$l,-$l)/60; // 
            $contestEndingFinish = rand(-2*$l/60, 0);
            
            $maxContestDuration = rand(-$l, 0);
            
            $res[] = array($i % 2 == 1 ? TRUE : FALSE, $start, $finish, $contestEndingStart, $contestEndingFinish, $maxContestDuration);
        }
        
        $res[] = array(FALSE, $now, $now, $now, $now, 0);

        $res[] = array(FALSE, null, null, null, null, null); // TODO: is it good to return 15 error on this?
        
        //TODO: test for overloading max integer values;
        
        return $res;
    }
    
    public function goodResultAccessPolicyDataProvider()
    {
        $input = array ('FullAccess', 'NoAccess', 'OnlySelfResults');
        $res = array();
        
        for($i = 0; $i < 3; $i++)
            for($j = 0; $j < 3; $j++)
                for($k = 0; $k < 3; $k++)
                    $res[] = array($input[$i], $input[$j], $input[$k]);
                    
        return $res;
    }

    public function badResultAccessPolicyDataProvider()
    {
        return TestData::getData('badResultsAccessPolicy');
    }
}

?>
