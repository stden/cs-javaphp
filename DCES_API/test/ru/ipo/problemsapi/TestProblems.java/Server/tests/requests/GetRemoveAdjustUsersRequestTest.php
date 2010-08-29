<?php

class GetRemoveAdjustUsersTestCase extends DCESWithAllRolesTestCase {
    
    protected $users = array();
    protected $curUsers = array();
    
    public function setUp() 
    {
        parent::setUp();

        for($i = 0; $i < TestData::RANDOM_TESTS_NUMBER; $i++) {
            
            //register random users for sample contest
            $user = TestData::genUserDescription(sizeof($this->userDataColumns));
            
            $this->apiRegisterUser(array('user'=>$user));
            
            unset($user->userID);
            $this->users[] = $user;
        }
        
        $getUsersReq = $this->apiGetUsers();
        $this->curUsers = array_slice($getUsersReq, 2); //removing contest admin and first participant
    }
    
    public function testGetUsersRequest() 
    {
        foreach($this->curUsers as $user) 
            unset($user->userID);
        
        $this->assertEquals($this->users, $this->curUsers);
    }
    
    public function testRemoveUserRequest()
    {
        $delUsersNum = rand(1, sizeof($this->curUsers));
        
        for($i = 0; $i < $delUsersNum; $i++) {
         
            $randomIndex = rand(0, sizeof($this->curUsers) - 1);
            
            $user = $this->curUsers[$randomIndex];
         
            $this->apiRemoveUser(array('userID'=>$user->userID, 'sessionID'=>$this->sessionID));
         
            unset($this->curUsers[$randomIndex]);
            $this->curUsers = array_values($this->curUsers); //re-index array
        }
            
        $users = $this->apiGetUsers();
        
        $this->assertEquals($this->curUsers, array_slice($users, 2));
    }
    
    public function testAdjustUserDataRequest()
    {
        //selecting random user
        $i = rand(0, sizeof($this->curUsers) - 1);
        
        $selUserID = $this->curUsers[$i]->userID; 
     
        //generating new object and store it
        $adjUser = TestData::genUserDescription(sizeof($this->userDataColumns));
        $adjUser->userID = $selUserID;
        
        //TODO: uncomment the line below when the AdjustUserDataRequest changes
        //$req = TestData::fillRequest('AdjustUserDataRequest', array('sessionID'=>$this->sessionID, 'user'=>$adjUser));

        $req = TestData::fillRequest('AdjustUserDataRequest', 
                          array('login'=>$adjUser->login, 
                                'newType' => $adjUser->userType,
                                'password'=> $adjUser->password,
                                'sessionID'=> $this->sessionID,
                                'userData'=> $adjUser->dataValue,
                                'userID'=> $adjUser->userID));
                               
        $this->assertEquals(new AcceptedResponse(), RequestSender::send($req));
        
        $resultUsers = array_slice($this->apiGetUsers(), 2);
        
        $userToCompare = null;
        foreach($resultUsers as $user)
            if($user->userID == $selUserID) $userToCompare = $user;
        
        $this->assertEquals($adjUser, $userToCompare);
    }
    
    public function testBadAdjustUserDataRequest()
    {
        $selUserID = $this->curUsers[rand(0, sizeof($this->curUsers) - 1)]->userID; 
        
        $badData = array(null, '', 0, array(), TestData::genUnicodeStr(rand(TestData::ALLOWED_UNICODE_LP_LENGTH + 1, 
                                                                            TestData::MAX_LP_LENGTH)));
        $udProps = array('login', 'newType', 'password', 'userData');
        
        $adjUser = TestData::genUserDescription(sizeof($this->userDataColumns));
        
        $adjUser->$udProps[rand(0, sizeof($udProps) - 1)] = TestData::getRandomValue($badData);
        
        $req = TestData::fillRequest('AdjustUserDataRequest', 
                          array('login'=>$adjUser->login, 
                                'newType' => $adjUser->userType,
                                'password'=> $adjUser->password,
                                'sessionID'=> $this->sessionID,
                                'userData'=> $adjUser->dataValue,
                                'userID'=> $selUserID));
                               
        $this->assertEquals(createFailRes(22), RequestSender::send($req));
    }
    
    /**
    * @dataProvider sessionProvider 
    */   
    public function testBadContextForGetUsersRequest($sessionID = '', $contestID = '')
    {
        if($sessionID == 'session_ok') 
            $req->sessionID = $this->sessionID;
        
        $req = TestData::fillRequest('GetUsersRequest', array('sessionID'=>$sessionID, 'contestID'=>$contestID));
        
        $this->assertClassEquals(new RequestFailedResponse(), RequestSender::send($req));
    }
    
    /**
    * @dataProvider sessionUserProvider 
    */   
    public function testBadContextForAdjustUserRequest($sessionID, $userID)
    {
        $adjUser = TestData::genUserDescription(sizeof($this->userDataColumns));
        
        $adjUser->sessionID = ($sessionID == 'session_ok') ? $this->sessionID : $sessionID;
        $adjUser->userID = ($userID == 'user_ok') ? TestData::getRandomValue($this->curUsers)->userID : $userID;
        
        $req = TestData::fillRequest('AdjustUserDataRequest', 
                                      array('login'=>$adjUser->login, 
                                            'newType' => $adjUser->userType,
                                            'password'=> $adjUser->password,
                                            'sessionID'=> $adjUser->sessionID,
                                            'userData'=> $adjUser->dataValue,
                                            'userID'=> $adjUser->userID));
                                            
        $this->assertClassEquals(new RequestFailedResponse(), RequestSender::send($req));
    }
    
    /**
    * @dataProvider sessionUserProvider 
    */   
    public function testBadContextForRemoveUserRequest($sessionID, $userID)
    {
        $sessionID = ($sessionID == 'session_ok') ? $this->sessionID : $sessionID;
        $userID = ($userID == 'user_ok') ? TestData::getRandomValue($this->curUsers)->userID : $userID;
        
        $req = TestData::fillRequest('RemoveUserRequest', array('sessionID'=> $sessionID,
                                                                'userID'=> $userID));
                                            
        $this->assertClassEquals(new RequestFailedResponse(), RequestSender::send($req));
    }
    
    public function sessionProvider()
    {
        $res = array();
        
        $bad = array(null, '', 0, TestData::genASCIIStr(24), 'session_ok');
        
        for($i = 0; $i < sizeof($bad); $i++)
            for($k = 0; $k < sizeof($bad); $k++)
                $res[] = array($bad[$i], $bad[$k]);
                
        return $res;
    }
    
    public function sessionUserProvider() 
    {
        $res = array();
        
        $bad = array(null, '', 0, TestData::genASCIIStr(24), 'session_ok', 'user_ok');
        
        for($i = 0; $i < sizeof($bad); $i++)
            for($k = 0; $k < sizeof($bad); $k++)
                if($bad[$i] != 'session_ok' || $bad[$k] != 'user_ok')
                    $res[] = array($bad[$i], $bad[$k]);
                
        return $res;
    }
}

?>
