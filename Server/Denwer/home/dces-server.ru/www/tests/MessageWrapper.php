<?php

require_once('post_request.php');

class MessageWrapper
 {
    protected $obj;
    protected $test;
    
	function __construct($obj, $test) 
	{
	    $this->obj = $obj;
	    $this->test = $test;
	}
    
    private function sendRequestObject($reqObj)
    {
        return PostRequest::send($this->serverURL, 'x='.serialize($reqObj));
    }
    
    function send()
    {
        return $this;        
    }
    
    function get($key)
    {
        return $this;
    }
    
    function set($key, $value)
    {
        return $this;
    }
    
    function assertNotError()
    {
        return $this;
    }
}

?>
