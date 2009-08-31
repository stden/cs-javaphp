<?php

require_once ('DCESTestCase.php');

class CreateDataBaseTestCase extends DCESTestCase
{
    public function testCreateDatabaseBB()
    {
        $this->assertEquals($this->c->getAcceptedResponse(), $this->c->createDatabase());
    }
    
    /*public function testCreateDatabaseWB()
    {
        
    }*/
}

?>