<?php

require_once('../../dces/utils/logger.php');

class LoggerTestCase extends DCESTestCase
{
    public function testCreateLogger()
    {
       /* $l = Logger::L($this->log_path.'/logg.txt', true);
        
        $this->assertNotNull($l);
        
        //$filename = dirname(__FILE__).'/../../logs/'.date('d.m.Y').'.txt';
        
        $filename = $this->log_path.'/logg.txt';
        
        $this->assertFileExists($filename);
        
        $test_msg = '40';        
        $file_stat = @stat($filename);
        
        $l->log($test_msg);
        
        $this->assertEquals(strlen('[hh:mm:ss]: '.$test_msg.'\r\n'), $file_stat['size']);*/
    }
}

?>
