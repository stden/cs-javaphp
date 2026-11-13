<?php

  //this plugin doesn't use a plugin folder to store any info

  class ACMLiteCheckerPlugin extends ServerPlugin {

	 	
    public function checkSolution($solution, $user_id, $answer_data, &$current_result, &$table_cols) {

      if (is_null($current_result))
        $current_result = 1;
      else
        $current_result ++;

      $res = array();
	  
		
		
		$arrayans = explode("\n", $solution['answer']);
		
	   $fp = fopen ($this->folder."/"."outuser.txt", "w");

    for ($i=0; $i<(count($arrayans)); $i++)
	   {
		if (trim($arrayans[$i])>0) fwrite($fp, trim($arrayans[$i])."\r\n"); 	
		}

	fclose($fp);
      
	   
      $ok = file_get_contents($this->folder."/"."outuser.txt") === file_get_contents($this->folder."/"."01.a")."\r\n";
      $res['result'] = $ok ? "yes" : "no";
	  if ($ok) {
        $table_cols = array("+");
      } else {
        $table_cols = array("-");
      }

      return $res;
    }
	
	


    public function updateStatementData($statement_zip) {
      return true;
    }

	
	    public function updateAnswerData($answer_zip) {
      //$folder - содержит путь к каталогу с данными
      //сохранить файл из архива в каталог folder
	 
      $p = $answer_zip->getFromName('01.a');
	   $fp = fopen ($this->folder."/"."01.a", "w");
	   fwrite($fp, $p);
	   fclose($fp);
	  
      if (! $p) return false;
           else return $p;
    }
    
  }

?>