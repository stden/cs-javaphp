<?php

class ConnectToContestTestCase extends DCESWithAllRolesTestCase
{
    public function testWrongContestTypeRegisterForContestAdmin()
    {
        $req = new RegisterToContestRequest();
        
        $req->sessionID = $this->connect->sessionID;
        $req->contestID = 0;
        
        $req->user = createUser('admin', 'superpassword');
        $this->assertEquals(createFailRes(16), RequestSender::send($req));
    }
    
    /**
    * @dataProvider loginPassProvider 
    */    
    public function testLoginPassword($isGood, $login, $pass, $type) 
    {
        $req = new ConnectToContestRequest();
        
        $req->login = $login;
        $req->password = $pass;
        $req->contestID = $type != 'SuperAdmin' ? $this->contestID : 0;
        
        $res = RequestSender::send($req);    
        
        if($isGood) {
            //TODO: Encapsulate this assertion pack into a method
            $this->assertNotEquals($res->sessionID, '');
            $this->assertNotEquals($res->sessionID, null);
            $this->assertEquals($res->user->login, $login);
            $this->assertEquals($res->user->userType, $type);
        } else
            $this->assertEquals(createFailRes(12), $res);
    }
    
    public function testStartBeforeContestStarted()
    {
        $this->adjustContest(array('start'=> time() + 3600, 'finish' => time() + 7200));
        
        $td = TestData::getData('userTestData');
        
        $req = new ConnectToContestRequest();
        $req->login = $td['Participant'][0];
        $req->password = $td['Participant'][1];
        $req->contestID = $this->contestID;
        
        $res = RequestSender::send($req);
        
        //participant can't connect to not yet started contest
        $this->assertEquals(createFailRes(19), $res);
                
        $req->login = $td['ContestAdmin'][0];
        $req->password = $td['ContestAdmin'][1];
        $res = RequestSender::send($req);
        $req->contestID = $this->contestID;
        
        //while contest admin can
        $this->assertNotEquals($res->sessionID, '');
        $this->assertNotEquals($res->sessionID, null);
        $this->assertEquals($res->user->login, $td['ContestAdmin'][0]);
        $this->assertEquals($res->user->userType, 'ContestAdmin');
    }
    
    /* ======================== Data providers ===================== */
    
    public function loginPassProvider()
    {
        $res = array();
        
        $bad_input = array(null, '', 42, TestData::genUnicodeStr(rand(0, 1) ? 
                                                                     rand(1, TestData::MIN_LP_LENGTH) : 
                                                                     rand(TestData::MAX_LP_LENGTH, 2*TestData::MAX_LP_LENGTH)));
                    
        $roles = array('SuperAdmin', 'ContestAdmin', 'Participant');

        //bad data for all roles        
        for($i = 0; $i < sizeof($roles); $i++)
            $res[] = array(BAD_DATA, TestData::getRandomValue($bad_input), TestData::getRandomValue($bad_input), $roles[$i]);
        
        //bad data for super admin but good for everyone else
        $lp_len = rand(TestData::MIN_LP_LENGTH, TestData::MAX_LP_LENGTH);
        $res[] = array(BAD_DATA, TestData::genUnicodeStr($lp_len), TestData::genUnicodeStr($lp_len), 'SuperAdmin');
        
        return $res;
    }
}

?>
