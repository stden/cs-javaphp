<?php

class CreateDatabaseTestCase extends DCESBaseTestCase
{
    public function testCreateDatabase()
    {
        $this->assertEquals(new AcceptedResponse(), RequestSender::send(new CreateDataBaseRequest()));
    }
}

class CreatedDatabaseTestCase extends DCESWithDBTestCase
{
    public function testRecreateDatabaseFails()
    {
        $this->assertEquals(createFailRes(13), RequestSender::send(new CreateDataBaseRequest()));
    }
    
    /**
     * @dataProvider badLoginPassProvider 
     */
    public function testBadLoginPasswordToCreateDB($login, $pass)
    {
        $req = new CreateDataBaseRequest();
        
        $req->login = $login;
        $req->password = $pass;
        
        $this->assertEquals(createFailRes(13), RequestSender::send($req));
    }
    
    public function badLoginPassProvider()
    {
        return TestData::getData('badLoginPass');
    }
}

?>