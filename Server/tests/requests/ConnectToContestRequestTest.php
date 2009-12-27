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
        $req = new ConnectToContestRequest();
        
        $req->login = $login;
        $req->password = $pass;
        $req->contestID = 0;
        
        $this->assertEquals(createFailRes(12), RequestSender::send($req));
    }
    
    /**
     * @dataProvider goodSALoginPassProvider 
     */
    public function testGoodLoginPasswordForSuperAdmin($login, $pass)
    {
        $req = new ConnectToContestRequest();
        
        $req->login = $login;
        $req->password = $pass;
        $req->contestID = 0;
        
        $res = RequestSender::send($req);
        
        $this->assertNotEquals($res->sessionID, '');
        $this->assertNotEquals($res->sessionID, null);
        $this->assertEquals($res->user->login, 'admin');
        $this->assertEquals($res->user->userType, 'SuperAdmin');
    }
    
    /**
     * @dataProvider goodCALoginPassProvider 
     */
    public function testGoodLoginPasswordForContestAdmin($login, $pass)
    {
        $req = new ConnectToContestRequest();
        
        $req->login = $login;
        $req->password = $pass;
        $req->contestID = $this->contestID;
        
        $res = RequestSender::send($req);
        
        $this->assertEquals($res->user->login, $login);
        $this->assertNotEquals($res->sessionID, '');
        $this->assertNotEquals($res->sessionID, null);
        $this->assertEquals($res->user->userType, 'ContestAdmin');
    }
    
    /**
     * @dataProvider badLoginPassProvider 
     */
    public function testBadLoginPasswordForContestAdminAndParticipant($login, $pass)
    {
        $req = new ConnectToContestRequest();
        
        $req->login = $login;
        $req->password = $pass;
        $req->contestID = $this->contestID;
        
        $res = RequestSender::send($req);
        
        $this->assertEquals(createFailRes(12), $res);
    }
    
    /**
     * @dataProvider goodLoginPassProvider 
     */
    public function testGoodLoginPasswordForParticipant($login, $pass)
    {
        $req = new ConnectToContestRequest();
        
        $req->login = $login;
        $req->password = $pass;
        $req->contestID = $this->contestID;
        
        $res = RequestSender::send($req);
        
        $this->assertEquals($res->user->login, $login);
        $this->assertNotEquals($res->sessionID, '');
        $this->assertNotEquals($res->sessionID, null);
        $this->assertEquals($res->user->userType, 'Participant');
    }

    public function testWrongContestTypeRegisterForContestAdmin()
    {
        $req = new RegisterToContestRequest();
        
        $req->sessionID = $this->connect->sessionID;
        $req->contestID = 0;
        
        $req->user = createUser('admin', 'superpassword');
        $this->assertEquals(createFailRes(16), RequestSender::send($req));
    }

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
