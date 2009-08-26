<?php

//sometimes need to include like this: require_once(dirname(__FILE__).'/../utils/lgger.php');

class Logger 
{
    protected static $logger;
    protected $logpath;
    protected $stream;
    
    public function __construct($logpath, $clear)
    {
        $this->stream = fopen($logpath, "a+t");
        
        if($clear)
            ftruncate($this->stream, 0);
    }
    
    public static function L($logpath, $clear = false)
    {
         if(self::$logger === null)
            self::$logger = new Logger($logpath, $clear);
            
         return self::$logger;
    }
    
    public function log($msg)
    {
        fwrite($this->stream, $msg."\n");
    }
    
    public function __destruct()
    {
        fclose($this->stream);
    }
} 
?>