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
        
        //connecting to admin with superadmin login/pass
        $req = new ConnectToContestRequest();
        
        $req->login = 'admin';
        $req->password = 'superpassword';
        $req->contestID = 0;
        
        $this->connect = RequestSender::send($req);
        $this->superadmin = $this->connect->user;
    }
}
 
?>