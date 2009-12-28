<?php

class CreateContestRequestTestCase extends DCESWithSuperAdminTestCase {
    
    public function setUp() {
        parent::setUp();
    }
    
    public function testResultAccessPolicyAdjustedContest() {
        
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
    public function testGoodContestTimingAdjustedContest($selfStart, $s, $f, $eS, $eF, $maxDur) {
        $this->cTAdjContest(TRUE, $selfStart, $s, $f, $eS, $eF, $maxDur);
    }

    /**
     * @dataProvider badContestTimingDataProvider
     */
    public function testBadContestTimingAdjustedContest($selfStart, $s, $f, $eS, $eF, $maxDur) {
        $this->cTAdjContest(FALSE, $selfStart, $s, $f, $eS, $eF, $maxDur);
    }
    
    private function cTAdjContest($isGood, $selfStart, $s, $f, $eS, $eF, $maxDur) {
        $req = new CreateContestRequest();
        
        $descr = new ContestDescription();
        $descr->start = $s;
        $descr->finish = $f;
        
        $ct = new ContestTiming();
        $ct->selfContestStart = $selfStart;
        $ct->maxContestDuration = $maxDur;
        $ct->contestEndingStart = $eS;
        $ct->contestEndingFinish = $eF;
        
        $req->sessionID = $this->connect->sessionID;
        $req->contest = $descr;
        
        $res = RequestSender::send($req);
        
        if($isGood)
            $this->assertNotEquals($res->createdContestID, null);
        else
            $this->assertEquals(createFailRes(15, 'contest may not start after its finish'), $res);
    }
    
    /* Data providers */
    
    public function goodContestTimingDataProvider() {
        
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
    
    public function badContestTimingDataProvider() {
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
}

?>
