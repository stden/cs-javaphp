<?php

require_once('constructor.php');

class DCESTestCase extends PHPUnit_Framework_TestCase
{
    protected $c;
    
    protected function setUp(){
        $this->c = new Constructor();
    }
    
    public function testCreateDatabase()
    {
        $this->assertEquals($this->c->getAcceptedResponse(), $this->c->createDatabase());
    }
}

?>