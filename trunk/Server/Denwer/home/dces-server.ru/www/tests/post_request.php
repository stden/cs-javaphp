<?php

class PostRequest {
	
public static function send($url, $data, $optional_headers = null)
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

}

?>
