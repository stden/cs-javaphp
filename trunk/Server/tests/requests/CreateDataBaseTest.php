<?php

class CreateDataBaseTestCase extends DCESBaseTestCase
{
    public function testCreateDatabase()
    {
        $req = new CreateDataBaseRequest();
        
        $gold = new AcceptedResponse();
        
        $this->assertEquals($gold, RequestSender::send($req));
    }
};

?>