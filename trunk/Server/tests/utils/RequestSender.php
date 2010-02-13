<?php

class RequestSender {
    
    private static $serverURL = "http://localhost/dces/dces.php";
	
    private static function _send($url, $data, $optional_headers = null)
    {
        $params = array ('http'=> array ('method' => 'post', 'content' => $data));
        
        if ($optional_headers !== null)
        {
            $params['http']['header'] = $optional_headers;
        }
        
        $ctx = stream_context_create($params);
        $fp = @fopen($url, 'rb', false, $ctx);
        
        if (!$fp) {
            throw new Exception("PostRequest exception: couldn't open stream to $url");
        }
        
        $response = @stream_get_contents($fp);
        
        if ($response === false) {
            throw new Exception("PostRequest exception: couldn't get response from $url");
        }
        
        return $response;
    }
    
    public static function send($reqObj)
    {
        $res = RequestSender::_send(RequestSender::$serverURL, 'x='.serialize($reqObj));
        
        if(strpos($res, serialize(null)) === 0)
            $res = substr($res, 2);
            
        $obj = @unserialize($res);
        
        if (!$obj){
            throw new Exception('Captain Obvious reports: deserialization epicly failed: '. $res);
            //TODO fail a test instead of calling exception
        }
        return $obj;
    }
}

?>
