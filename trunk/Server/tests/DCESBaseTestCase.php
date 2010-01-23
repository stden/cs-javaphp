<?php

abstract class DCESBaseTestCase extends PHPUnit_Framework_TestCase
{
    public function setUp()
    {
        RequestSender::send(new KillDBRequest());
    }
    
    public function assertClassEquals($golden, $real)
    {
        $this->assertEquals(get_class($golden), get_class($real));
    }    
    
    public function fillRequest($name, $params = array())
    {
        $req = new $name();
        
        foreach($params as $field => $value)
            $req->$field = $value;
        
        return $req;
    }
}

abstract class DCESWithDBTestCase extends DCESBaseTestCase
{
    public function setUp()
    {
        parent::setUp();
        $this->assertEquals(new AcceptedResponse(), RequestSender::send(new CreateDataBaseRequest()));
    }
}

abstract class DCESWithSuperAdminTestCase extends DCESWithDBTestCase {
    protected $connect;
    protected $superadmin;
    
    public function setUp() {
        parent::setUp();
        
        $td = TestData::getData('userTestData');
        
        //connecting to admin with superadmin login/pass
        $req = new ConnectToContestRequest();
        
        $req->login = $td['SuperAdmin'][0];
        $req->password = $td['SuperAdmin'][1];
        $req->contestID = 0;
        
        $this->connect = RequestSender::send($req);
        
        $this->sessionID = $this->connect->sessionID;
        $this->superadmin = $this->connect->user;
    }
    
    public function apiCreateContest($params = array()) {
        $req = $this->fillRequest('CreateContestRequest', $params);
        $req->sessionID = $this->sessionID;
        
        $res = RequestSender::send($req);
        
        $this->assertClassEquals(new CreateContestResponse(), $res);
        $this->assertNotEquals(null, $res->createdContestID);
        $this->assertNotEquals('0', $res->createdContestID);
        
        return $res;
    }
    
    public function apiRemoveContest($params) {
        $req = $this->fillRequest('RemoveContestRequest', $params);
        $req->sessionID = $this->sessionID;
        
        $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
    }
}

abstract class DCESWithAllRolesTestCase extends DCESWithSuperAdminTestCase {
    
    protected $contestID;
    
    protected $caConnect;
    protected $pConnect;
    
    public function setUp() 
    {
        parent::setUp();
        
        $td = TestData::getData('userTestData');
        
        //creating sample contest
        $req = new CreateContestRequest();
        $req->sessionID = $this->sessionID;
        $req->contest = new ContestDescription();
        
        $this->contestID = RequestSender::send($req)->createdContestID;
        
        //creating sample contest admin
        
        $req = new RegisterToContestRequest();
        $req->sessionID = $this->connect->sessionID;
        $req->contestID = $this->contestID;
        
        $req->user = createUser($td['ContestAdmin'][0], $td['ContestAdmin'][1], 'ContestAdmin');
        
        $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
            
        //create sample user with type Participant
    
        $req = new RegisterToContestRequest();
        $req->sessionID = $this->connect->sessionID;
        $req->contestID = $this->contestID;
        
        $req->user = createUser($td['Participant'][0], $td['Participant'][1]); 

        $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
        
        //create contest admin connect
        
        $ca_req = new ConnectToContestRequest();
        
        $ca_req->login = $td['ContestAdmin'][0];
        $ca_req->password = $td['ContestAdmin'][1];
        $ca_req->contestID = $this->contestID;
        
        $this->caConnect = RequestSender::send($ca_req);
        
        //create participant connect
         
        $p_req = new ConnectToContestRequest();

        $p_req->login = $td['Participant'][0];
        $p_req->password = $td['Participant'][1];
        $p_req->contestID = $this->contestID;
        
        $this->pConnect = RequestSender::send($p_req);
    }
    
    protected function adjustContest($params) {
        //adjust contest
        $req = new AdjustContestRequest();
        $req->sessionID = $this->sessionID;
        
        foreach($params as $param => $value)
            $req->contest->$param = $value;

        $req->contest->contestID = $this->contestID;
        
        $res = RequestSender::send($req);
        $this->assertEquals('AdjustContestResponse', get_class($res));
    }
}

abstract class DCESTwoContestsWithAllRoles extends DCESWithAllRolesTestCase {
    
    protected $contestID2;
    
    public function setUp() {
        parent::setUp();
        
        //creating another sample contest with registration type = 'Self'
        $req = new CreateContestRequest();
        $req->sessionID = $this->connect->sessionID;
        
        $cd = new ContestDescription();
        $cd->registrationType = 'Self'; //TODO: big refactor
        
        $req->contest = $cd;
        
        $this->contestID2 = RequestSender::send($req)->createdContestID;
    }
}
 
?>