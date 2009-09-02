<?php

require_once('DCESTestCase.php');

class CreateContestTestCase extends DCESTestCase
{
    public function testEmptyContest()
    {
        //code example for constructing a request and sending it
        $request = $contructor->
            construct('CreateContestRequest')->
            set('sessionID', 'Asd2354sdfkgsl')->
            set('contestTiming->selfContestStart', true)->
            set('name', 'A contest');            
            
        $response = $request->send();
        $response->assertIsError();
        $response->get('SessionID');
        
        $r = $contructor->
            construct('RegisterToContestRequest')->           
            set('password', 'pass')->send();
        
        send()->
        assertBLError(13)->
        doit();
        
        $constructor->
        recordMacro('mymacro')->
        construct('CreateContestRequest')->
        set('sessionID', $constructor->param(1))->
        set('name', 'A contest')->        
        send()->
        assertNotBLError()->
        doit()->        
        stopRecording()->
        macro('mymacro', 'id of session');             
    }
    
    function set($key, $val) {
        $p = $key.getParamInd();
        if ($p != 0) // if it is really a parameter
            
    }
    
}

?>