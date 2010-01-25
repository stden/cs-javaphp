<?php

class GetRemoveAdjustUsersTestCase extends DCESWithAllRolesTestCase {
    
    protected $users = array();
    
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
    }
    
    public function testGetUsersRequest() 
    {
        $req = $this->fillRequest('GetUsersRequest', array('contestID' => $this->contestID, 'sessionID' => $this->sessionID));
        
        $cur_users = array_slice(RequestSender::send($req)->users, 2);
        
        foreach($cur_users as $user) 
            unset($user->userID);
        
        $this->assertEquals($this->users, $cur_users);
    }
    
    public function testRemoveUsersRequest()
    {
        $req = $this->fillRequest('GetUsersRequest', array('contestID' => $this->contestID, 'sessionID' => $this->sessionID));
        
        $cur_users = array_slice(RequestSender::send($req)->users, 2);
        
        $num_of_deleted = rand(1, sizeof($cur_users));
        
        for($i = 0; $i < $num_of_deleted; $i++) {
         
            $id = rand(0, sizeof($cur_users) - 1);
         
            $this->apiRemoveUser($cur_users[$id]->userID);
         
            unset($cur_users[$id]);
            $cur_users = array_values($cur_users); //re-index array
        }
            
        $res = RequestSender::send($req);
        
        $this->assertEquals($cur_users, array_slice($res->users, 2));
    }
}

?>
