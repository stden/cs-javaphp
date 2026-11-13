<?php

$preIP = dirname(__FILE__);
require_once "$preIP/../main.php";

class StopContestTestCase extends DCESWithAllRolesTestCase
{
    public function testContestAndSuperAdminFailsToStopContest()
    {
        $req = new StopContestRequest();
        
        $req->sessionID = $this->sessionID;
        
        //TODO: add new description and code for 'This operation can be done by participant only'
        $this->assertEquals(createFailRes(42), RequestSender::send($req)); 
        
        $req->sessionID = $this->caConnect->sessionID;
        $this->assertEquals(createFailRes(42), RequestSender::send($req));
        
        $req->sessionID = $this->pConnect->sessionID; //this contest is a 'ByAdmins' contest
        $this->assertEquals(createFailRes(42), RequestSender::send($req));
    }
    
    
    public function testParticipantCanStopContest()
    {
        $ct = new ContestTiming(); 
        $ct->selfContestStart = true;
         
        $this->apiAdjustContest(array('contestTiming' => $ct));
        
        $req = new StopContestRequest();
        $req->sessionID = $this->pConnect->sessionID;
        
        $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
    }
        
}

?>
