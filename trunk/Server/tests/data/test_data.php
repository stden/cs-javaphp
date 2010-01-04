<?php

define('GOOD_DATA', 1);
define('BAD_DATA', 0);

class TestData 
{
    const TIME_SCALE = 100;
    const RANDOM_TESTS_NUMBER = 20;
    
    
    private static $data = array(
        
        'badLoginPass' =>           array (
                                        array(null, null),
                                        array('', ''),
                                        array(42, 42),
                                    
                                        /* length:(275, 275) */
                                        array('thisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25chars', 
                                              'thisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25chars')
                                          ),
                                           
        'goodCALoginPass' =>        array (
                                        array('calogin', 'capass')
                                          ), 
                                          
        'goodLoginPass' =>          array (
                                        array('login', 'pass')
                                          ), 
                                         
        'goodSALoginPass' =>        array (
                                        array('admin', 'superpassword')
                                          ),
       
        'accessPermission' =>       array ('FullAccess', 'NoAccess', 'OnlySelfResults'),
        
        'resultsAccessPolicy' =>    array(
                                        array (BAD_DATA, null, null, null),
                                        array (BAD_DATA, 42, 42, 42),
                                        array (BAD_DATA, '', '', ''),
                                        array (BAD_DATA, 42, '', null),
                                         ),
         'registrationType' =>   array(
                                        array(BAD_DATA, null), 
                                        array(BAD_DATA, 42), 
                                        array(BAD_DATA, ''),
                                        array(GOOD_DATA, 'Self'), 
                                        array(GOOD_DATA, 'ByAdmins')
                                         ),
        );
            
                                      
    public static function getData($name, $is_random = false)
    {
        return TestData::$data[$name];
    }
    
    public static function getSingleData($name, $is_random = false)
    {
        if($is_random == true)
            return TestData::$data[$name][rand(0, sizeof(TestData::$data[$name]) - 1)];
        else
            return TestData::$data[$name][0];
    }                                      
}

?>
