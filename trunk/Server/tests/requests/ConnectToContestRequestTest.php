<?php

class ConnectToContestTestCase extends DCESWithSuperAdminTestCase
{
    protected $contestID;
    protected $admin;    
    
    public function setUp() 
    {
        parent::setUp();
                
        //creating sample contest
        $req = new CreateContestRequest();
        $req->sessionID = $this->connect->sessionID;
        $req->contest = new ContestDescription();
        
        $this->contestID = RequestSender::send($req)->createdContestID;
        
        //creating sample contest admin
        
        $req = new RegisterToContestRequest();
        $req->sessionID = $this->connect->sessionID;
        $req->contestID = $this->contestID;
        
        $lp = TestData::getSingleData('goodCALoginPass');
        $req->user = createUser($lp[0], $lp[1], 'ContestAdmin');
        
        $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
            
        //TODO: create sample user with type Participant
    
        $req = new RegisterToContestRequest();
        $req->sessionID = $this->connect->sessionID;
        $req->contestID = $this->contestID;
        
        $lp = TestData::getSingleData('goodLoginPass');
        $req->user = createUser($lp[0], $lp[1]); 

        $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
    }
    
    /**
     * @dataProvider badSALoginPassProvider 
     */
    public function testBadLoginPasswordForSuperAdmin($login, $pass)
    {
        $this->LPForSA(FALSE, $login, $pass);
    }
    
    /**
     * @dataProvider goodSALoginPassProvider 
     */
    public function testGoodLoginPasswordForSuperAdmin($login, $pass)
    {
        $this->LPForSA(TRUE, $login, $pass);
    }
    
    /**
     * @dataProvider goodCALoginPassProvider 
     */
    public function testGoodLoginPasswordForContestAdmin($login, $pass)
    {
        $this->LPForCAAndParticipant(TRUE, $login, $pass, 'ContestAdmin');
    }
    
    /**
     * @dataProvider badLoginPassProvider 
     */
    public function testBadLoginPasswordForContestAdminAndParticipant($login, $pass)
    {
        $this->LPForCAAndParticipant(FALSE, $login, $pass);  
    }
    
    /**
     * @dataProvider goodLoginPassProvider 
     */
    public function testGoodLoginPasswordForParticipant($login, $pass)
    {
        $this->LPForCAAndParticipant(TRUE, $login, $pass);
    }

    public function testWrongContestTypeRegisterForContestAdmin()
    {
        $req = new RegisterToContestRequest();
        
        $req->sessionID = $this->connect->sessionID;
        $req->contestID = 0;
        
        $req->user = createUser('admin', 'superpassword');
        $this->assertEquals(createFailRes(16), RequestSender::send($req));
    }
    
    private function LPForCAAndParticipant($isGood, $login, $pass, $type = 'Participant') 
    {
        $req = new ConnectToContestRequest();
        
        $req->login = $login;
        $req->password = $pass;
        $req->contestID = $this->contestID;
        
        $res = RequestSender::send($req);    
        
        if($isGood) {
            $this->assertEquals($res->user->login, $login);
            $this->assertNotEquals($res->sessionID, '');
            $this->assertNotEquals($res->sessionID, null);
            $this->assertEquals($res->user->userType, $type);
        } else
            $this->assertEquals(createFailRes(12), $res);
    }
    
    private function LPForSA($isGood, $login, $pass)
    {
        $req = new ConnectToContestRequest();
        
        $req->login = $login;
        $req->password = $pass;
        $req->contestID = 0;
        
        $res = RequestSender::send($req);
        
        if($isGood) {
            $this->assertNotEquals($res->sessionID, '');
            $this->assertNotEquals($res->sessionID, null);
            $this->assertEquals($res->user->login, 'admin');
            $this->assertEquals($res->user->userType, 'SuperAdmin');
        } else 
             $this->assertEquals(createFailRes(12), RequestSender::send($req));    
    }
    
    
    
    /* Data providers */
    
    public function goodLoginPassProvider()
    {
        return TestData::getData('goodLoginPass');
    }
    
    public function badLoginPassProvider()
    {
        return TestData::getData('badLoginPass');
    }

    public function goodCALoginPassProvider()
    {
        return TestData::getData('goodCALoginPass');
    }
    
    public function badSALoginPassProvider()
    {
        return array_merge(TestData::getData('badLoginPass'), TestData::getData('goodLoginPass'));
    }
    
    public function goodSALoginPassProvider()
    {
        return TestData::getData('goodSALoginPass');
    }
}

?>
