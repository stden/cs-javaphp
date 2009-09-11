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
        return PostRequest::doPostRequest($this->serverURL, 'x='.serialize($reqObj));
    }
    
    function send()
    {
        
    }
}

?>
