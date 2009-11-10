<?php

class CreateDataBaseTestCase extends DCESBaseTestCase
{
    public function testCreateDatabase()
    {
        $this->assertEquals(new AcceptedResponse(), RequestSender::send(new CreateDataBaseRequest()));
    }
};

class ReCreateDataBaseTestCase extends DCESCreateDBTestCase
{
    public function testReCreateDatabaseFails()
    {
        $this->assertEquals(createFailedResponse('13'), RequestSender::send(new CreateDataBaseRequest())); 
    }
}

?>