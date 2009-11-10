<?php

abstract class DCESBaseTestCase extends PHPUnit_Framework_TestCase
{
}

abstract class DCESCreateDBTestCase extends DCESBaseTestCase
{
    public function setUp()
    {
        RequestSender::send(new KillDBRequest());
        
        $res = RequestSender::send(new CreateDataBaseRequest());
        
        $this->assertEquals($res, new AcceptedResponse()); 
    }
} 

?>