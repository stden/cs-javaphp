<?php

require_once ('DCESTestCase.php');

class CreateDataBaseTestCase extends DCESTestCase
{
    public function testCreateDatabase()
    {
        Constructor::instance($this)->
        construct('CreateDataBaseRequest')->
        set('login', 'admin')->
        set('password', 'superpassword')->
        send()->
        assertNotError();
    }
}

?>