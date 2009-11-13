<?php

class ConnectToContestTestCase extends DCESWithDBTestCase
{
    /**
     * @dataProvider badLoginPassProvider 
     */
    public function testBadLoginPasswordToAdminTheServer($login, $pass)
    {
        $this->assertEquals(createFailRes(12), RequestSender::send(new ConnectToContestRequest()));
    }
    
    public function badLoginPassProvider()
    {
        return TestData::getData('badLoginPass');
    }
}

?>
