<?php

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
