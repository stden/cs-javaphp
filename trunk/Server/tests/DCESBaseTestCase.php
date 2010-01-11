<?php

abstract class DCESBaseTestCase extends PHPUnit_Framework_TestCase
{
    public function setUp()
    {
        RequestSender::send(new KillDBRequest());
    }    
    
    public function fillRequest($name, $params)
    {
        $req = new $name();
        
        foreach($params as $field =>$value)
            $req->$field = $value;
        
        return $req;
    }
    
    /*public function testEverythingFails($name, $params, $code)
    {
        $this->assertEquals(createFailRes($code), $this->fillRequest($name, $params));
    }*/
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
        $req->sessionID = $this->connect->sessionID;
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
}
 
?>