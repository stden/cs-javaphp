<?php

class TestData 
{
    private static $data = array(
            
            'badLoginPass' =>        array (
                                                array(null, null),
                                                array('', ''),
                                                array(42, 42),
                                                
                                                /* length:(275, 275) */
                                                array('thisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25chars', 
                                                      'thisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25charsthisstringoccupies25chars')
                                                
                                                /*TODO: try to add SQL Injection test code here*/
                                           ),
            'goodLoginPass' =>       array (
                                                array('samplelogin', 'samplepass')
                                           ), 
                                             
            'goodSALoginPass' =>     array (
                                                array('admin', 'superpassword')
                                           ),
            );
                                
            
                                      
    public static function getData($name)
    {
        return TestData::$data[$name];
    }                                      
}

?>
