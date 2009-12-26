<?php

class ConnectToContestTestCase extends DCESWithDBTestCase
{
    protected $connect;
    protected $superadmin;
    protected $contestID;
    protected $admin;    
    
    public function setUp() 
    {
        //connecting to admin with superadmin login/pass
        $req = new ConnectToContestRequest();
        
        $req->login = 'admin';
        $req->password = 'superpassword';
        $req->contestID = 0;
        
        $this->connect = RequestSender::send($req);
        $this->superadmin = $this->connect->user;
        
        //creating sample contest
        $req = new CreateContestRequest();
        $req->sessionID = $this->connect->sessionID;
        $req->contest = new ContestDescription();
        
        $this->contestID = RequestSender::send($req)->createdContestID;
        
        //creating sample contest admin
        $req = new RegisterToContestRequest();
        $req->sessionID = $this->connect->sessionID;
        $req->contestID = $this->contestID;
        
        $lp = TestData::getData('goodLoginPass'); $l = $lp[0][0]; $p = $lp[0][1];
        $req->user = createUser($l, $p, 'ContestAdmin');
        
        $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
        
        $this->admin = $req->user;
        
        //TODO: create sample user with type Participant
        $req = new RegisterToContestRequest();
        $req->sessionID = $this->connect->sessionID;
        $req->contestID = $this->contestID;
        
        $req->user = createUser('login', 'pass');
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
     * @dataProvider goodLoginPassProvider 
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
    public function testBadLoginPasswordForContestAdmin($login, $pass)
    {
        $req = new ConnectToContestRequest();
        
        $req->login = $login;
        $req->password = $pass;
        $req->contestID = $this->contestID;
        
        $res = RequestSender::send($req);
        
        $this->assertEquals(createFailRes(12), $res);
    }
    
    public function testWrongContestTypeRegisterForContestAdmin()
    {
        $req = new RegisterToContestRequest();
        
        $req->sessionID = $this->connect->sessionID;
        $req->contestID = 0;
        
        $req->user = createUser('admin', 'superpassword');
        $this->assertEquals(createFailRes(16), RequestSender::send($req));
    }
    
    public function testBadLoginPasswordForParticipant($login, $pass)
    {
        //TODO: implement
    }
    
    public function testGoodLoginPasswordForParticipant($login, $pass)
    {
        //TODO: implement
    }

    public function goodLoginPassProvider()
    {
        return TestData::getData('goodLoginPass');
    }
    
    public function badLoginPassProvider()
    {
        return TestData::getData('badLoginPass');
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
