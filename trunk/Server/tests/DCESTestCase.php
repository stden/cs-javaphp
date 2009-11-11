<?php

abstract class DCESBaseTestCase extends PHPUnit_Framework_TestCase
{
    public function setUp()
    {
        RequestSender::send(new KillDBRequest());
    }    
} 

?>