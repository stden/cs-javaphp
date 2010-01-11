<?php

define('GOOD_DATA', 1);
define('BAD_DATA', 0);

class TestData 
{
    const TIME_SCALE = 100;
    const RANDOM_TESTS_NUMBER = 20;
    const MAX_USER_DATA_FIELDS = 10;
    const MAX_DATA_LENGTH = 255;
    const MIN_LP_LENGTH = 5;
    const MAX_LP_LENGTH = 24;
    
    private static $data = array(
        'userTestData' =>           array('SuperAdmin' => array('admin', 'superpassword'),
                                          'ContestAdmin' => array('contestAdmin', 'pass'),
                                          'Participant' => array('participant', 'pass')),
        
        'accessPermission' =>       array ('FullAccess', 'NoAccess', 'OnlySelfResults'),
        
        'resultsAccessPolicy' =>    array(
                                        array (BAD_DATA, null, null, null),
                                        array (BAD_DATA, 42, 42, 42),
                                        array (BAD_DATA, '', '', ''),
                                        array (BAD_DATA, 42, '', null),
                                         ),
                                         
         'registrationType' =>      array(
                                        array(BAD_DATA, null), 
                                        array(BAD_DATA, 42), 
                                        array(BAD_DATA, ''),
                                        array(GOOD_DATA, 'Self'), 
                                        array(GOOD_DATA, 'ByAdmins')
                                         ),
        );
                                      
    public static function getData($name)
    {
        return TestData::$data[$name];
    }
    
    public static function getRandomValue($ar) {
         return $ar[rand(0, sizeof($ar) - 1)];
    }
    
    public static function genUnicodeStr($length)
    {
        $res = '';
        
        for($i = 0; $i < $length; $i++)
            $res .= mb_convert_encoding('&#' . rand(32, 65535) . ';', 'UTF-8', 'HTML-ENTITIES');
        
        return $res;
    }                        
    
    public function genASCIIStr($length)
    {
        $res = '';
        
        for($i = 0; $i < $length; $i++)
            $res .= chr(rand(32, 127));
        
        return $res;
    }
    
    public static function gB()
    {
        return rand(0,1) ? TRUE : FALSE;
    }              
}

?>
