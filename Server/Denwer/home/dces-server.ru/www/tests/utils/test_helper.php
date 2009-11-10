<?php

function createFailedResponse($code, $failReason = 'BusinessLogicError' , $info = '')
{
    $res = new RequestFailedResponse();
    
    $res->failReason = $failReason;
    $res->failErrNo = $code;
    $res->extendedInfo = $info;
    
    return $res;
}

class Constructor
{
     //TODO: refactor to load from config file
    private static $inst; //array<test cases, contsructor instances>
    private $test;

    protected function __construct($test)
    {
        $this->test = $test;
    }
   
    public static function instance($test){
        
        $className = get_class($test);
        
        if(!isset(Constructor::$inst[$className]))
            Constructor::$inst[$className] = new Constructor($test);
        
        return Constructor::$inst[$className];
    }
    
    public function construct($name)
    {
        $obj = new $name();
        
        return $obj;
    }
};

?>