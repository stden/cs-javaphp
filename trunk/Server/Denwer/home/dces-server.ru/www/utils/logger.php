<?php

//sometimes need to include like this: require_once(dirname(__FILE__).'/../utils/lgger.php');

class Logger 
{
    protected static $logger;
    protected $logpath;
    protected $stream;
    protected $clear = false;
    
    private function __construct($logpath = '')
    {
        if($logpath == '') //create a log file like 09.09.09 12:34:34 
        {
            $path = dirname(__FILE__)."/../logs/";
            
            $h = @opendir($path);
            
            if(!$h)
                if(!mkdir($path)) throw new Exception("Couldn't create directory 'logs' in root directory");
            else
                closedir($h);
                    
            $logpath = $path;            
            
            $logpath .= date('d.m.Y').'.txt';
        }
        
        $this->stream = fopen($logpath, "a+t");
        
        if($this->clear)
            ftruncate($this->stream, 0);
    }
    
    public static function L($logpath = '', $clear = false)
    {
         if(self::$logger === null)
            self::$logger = new Logger($logpath, $clear);
         
         self::$logger->setClear($clear);
         
         return self::$logger;
    }
    
    public function log($msg)
    {
        fwrite($this->stream, date('[H:i:s]: ').$msg."\n");
    }
    
    public function getClear()
    {
        return $this->clear;
    }
    
    public function setClear($clear)
    {
        $this->clear = ($clear == true) ? true : false;
    }
    
    public function __destruct()
    {
        fclose($this->stream);
    }
} 
?>