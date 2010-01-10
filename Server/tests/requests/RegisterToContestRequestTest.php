<?php
    //TODO: for all fail responses related to 0-contest use code '16'
    
class RegisterToContestTestCase extends DCESWithAllRolesTestCase 
{
    public function setUp()
    {
        parent::setUp();
    }
    
    public function testAddContestAdminOrParticipant($isGood, $login, $pass, $userType)
    {
        
    }
    
    public function testCannotAddSuperAdminToAnotherContest() 
    {

    }

    public function userDataProvider()
    {
        
    }    
    
}
    
?>
