<?php

//sometimes need to include like this: require_once(dirname(__FILE__).'/../utils/lgger.php');

class Logger 
{
    protected static $logger;
    protected $logpath;
    protected $stream;
    
    private function __construct($logpath = '', $clear)
    {
        if($logpath == '') //create a log file like 09.09.09 12:34:34 
        {
            $path = $_SERVER['DOCUMENT_ROOT'].'/logs/';
            
            if($h = @opendir($path))
                $logpath = $path;
            else
                $h = mkdir($path);
            
            closedir($h);
            
            $logpath .= date('d.m.Y H:i:s').'.txt';
        }
        
        $this->stream = fopen($logpath, "a+t");
        
        if($clear)
            ftruncate($this->stream, 0);
    }
    
    public static function L($logpath = '', $clear = false)
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