<?php

$preIP = dirname(__FILE__);
require_once "$preIP/../main.php";

class RegisterToContestTestCase extends DCESTwoContestsWithAllRoles
{
    public function setUp()
    {
        parent::setUp();
    }
    
    /**
    * @dataProvider userDataProvider 
    */   
    public function testRegisterForContest($isGood, $who, $whom, $login, $pass, $contestID = 1, $errNo = 15 )
    {     
        $td = TestData::getData('userTestData');
        
        $req = new RegisterToContestRequest();
        $req->user = createUser($login, $pass, $whom);
        $req->contestID = $contestID;     

        switch($who) {
            case 'SuperAdmin':
            	$req->sessionID = $this->sessionID;
            	break;
            case 'ContestAdmin':
            	$req->sessionID = $this->caConnect->sessionID;
            	break;
            case 'Participant':
            	$req->sessionID = $this->pConnect->sessionID;
            	break;
            default:
                $req->sessionID = null;
        }
        
        $res = RequestSender::send($req);
        
        if($isGood) { 
            $this->assertEquals(new AcceptedResponse(), $res);

            $req = new ConnectToContestRequest();
        
            $req->login = $login;
            $req->password = $pass;
            $req->contestID = $contestID;            
            
            $res = RequestSender::send($req); 
            
            $this->assertNotEquals($res->sessionID, '');
            $this->assertNotEquals($res->sessionID, null);
            $this->assertEquals($res->user->login, $login);
            $this->assertEquals($res->user->userType, $whom);
        }
        else 
            $this->assertEquals(createFailRes($errNo), $res);
    }

    public function userDataProvider()
    {
        $res = TestData::getData('regToContest');

        //TODO: add bad data generator for super-long passwords and logins

        return $res;    
    }    
    
}
    
?>
