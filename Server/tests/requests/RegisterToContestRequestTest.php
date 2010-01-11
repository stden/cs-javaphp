<?php
    //TODO: for all fail responses related to 0-contest use code '16'
    
class RegisterToContestTestCase extends DCESWithAllRolesTestCase 
{
    public function setUp()
    {
        parent::setUp();
    }
    
    /*
    public function testSuperAdminRegisterToContest()
    {
        
    }*/
    
    /**
    * @dataProvider userDataProvider 
    */   
    public function testRegisterForContest($isGood, $who, $whom, $login, $pass)
    {
        $ourContestID = ($who == 'SuperAdmin') ? 0: $this->contestID;
        $td = TestData::getData('userTestData');
        
        $req = new RegisterToContestRequest();
        $req->user = createUser($login, $pass, $whom);
        $req->contestID = $ourContestID;     

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
        
        var_dump($req);
        $res = RequestSender::send($req);
        
        if($isGood) { 
            $this->assertEquals(new AcceptedResponse(), $res);

            $req = new ConnectToContestRequest();
        
            $req->login = $login;
            $req->password = $pass;
            $req->contestID = $ourContestID;            
            
            $res = RequestSender::send($req); 
            
            $this->assertNotEquals($res->sessionID, '');
            $this->assertNotEquals($res->sessionID, null);
            $this->assertEquals($res->user->login, $login);
            $this->assertEquals($res->user->userType, $whom);
        }
        else 
            $this->assertEquals(createFailRes(12), $res);
    }
    
    /*public function testCannotAddSuperAdminToAnotherContest() 
    {

    }*/

    public function userDataProvider()
    {
        $res = array();

        $td = TestData::getData('userTestData');
        
        //who registers, who is being registered, 
        $res[] = array(GOOD_DATA, 'SuperAdmin', 'SuperAdmin', $td['SuperAdmin'][0].'_1', $td['SuperAdmin'][1]);
        
        return $res;    
    }    
    
}
    
?>
