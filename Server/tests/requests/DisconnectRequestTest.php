<?php
class RegisterToContestTestCase extends DCESWithAllRolesTestCase
{
    /*
    public function setUp()
    {
        parent::setUp();
    }
    */
   
   /**
    * @dataProvider disconnectionDataProvider
    */   
    public function testDisconectFromContest($isGood, $who, $session_id = null)
    {                
        switch ($who) {
            case 'Participant':
                $session_id = $this->pConnect->sessionID;
                break;
            case 'SuperAdmin':
                $session_id = $this->sessionID;
                break;
            case 'ContestAdmin':
                $session_id = $this->caConnect->sessionID;
                break;
        }
        
        $req = new DisconnectRequest();
        $req->sessionID = $session_id;
        
        $res = RequestSender::send($req);
        
        if ($isGood) {
            $this->assertEquals(new AcceptedResponse(), $res);
        
            $res = RequestSender::send($req);
        }
        
        $this->assertEquals(createFailRes(3), $res);
    }

    public function disconnectionDataProvider() {
        return TestData::getData('disconnectContest');
    }
}
?>
