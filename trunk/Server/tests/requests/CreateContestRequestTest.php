<?php

class CreateContestRequestTestCase extends DCESWithSuperAdminTestCase {
    
    public function setUp() {
        parent::setUp();
    }
    
    /**
     * @dataProvider goodContestTimingDataProvider
     */
    public function testContestTimingCreation($selfStart, $s, $f, $eS, $eF, $maxDur) {
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
        $this->assertNotEquals($res->createdContestID, null);
    }
    
    public function goodContestTimingDataProvider() {
        
        $res = array();
        
        for($i = 0; $i < 20; $i++) {
            
            $now = time();
            
            $start = $now + rand(-$now/100, $now/100);
            $finish = $start + rand(1, $now/100);
            $contestEndingStart = rand(1, ($finish - $start)/60);
            $contestEndingFinish = rand(1, $now/100);
            
            $maxContestDuration = rand(1, ($i % 2 == 1) ? $finish - $start : $now/100);
            
            $res[] = array($i % 2 == 1 ? true : false, $start, $finish, $contestEndingStart, $contestEndingFinish, $maxContestDuration);
        }
        
        return $res;
    }
    
}

?>
