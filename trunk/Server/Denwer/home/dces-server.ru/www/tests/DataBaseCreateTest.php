<?php

require_once('constructor.php');
require_once 'PHPUnit/Framework.php';

class DCESTestCase extends PHPUnit_Framework_TestCase
{
    protected $c;
    
    protected function setUp(){
        $this->c = new Constructor();
    }
    
    public function testCreateDatabaseBB()
    {
        $this->assertEquals($this->c->getAcceptedResponse(), $this->c->createDatabase());
    }
    
    /*public function testCreateDatabaseWB()
    {
        
    }*/
}

?>