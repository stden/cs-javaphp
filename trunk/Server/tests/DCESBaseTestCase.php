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
 
?>