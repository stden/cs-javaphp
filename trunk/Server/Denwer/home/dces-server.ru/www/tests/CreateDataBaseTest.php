<?php

require_once ('DCESTestCase.php');

class CreateDataBaseTestCase extends DCESTestCase
{
    public function testCreateDatabase()
    {
        $c = Constructor::instance($this);
        $w = $c->construct('CreateDataBaseRequest');
        
        $w->set('login', 'admin');
        $w->set('password', 'superpassword');
        $w->send();
        $w->assertNotError();
    }
}

?>