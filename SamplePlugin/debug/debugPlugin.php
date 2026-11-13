<?php

  //this plugin doesn't use a plugin folder to store any info

  class FirstDebugPlugin extends ServerPlugin {
    
    public function checkSolution($solution, $user_id, $answer_data, &$current_result, &$table_cols) {

      if (is_null($current_result))
        $current_result = 1;
      else
        $current_result ++;

      $res = array();
      $ok = $solution['answer'] === $answer_data;
      $res['result'] = $ok ? "yes" : "no";
      if ($ok) {
        $table_cols = array("+", $current_result);
      } else {
        $table_cols = array("-", 0);
      }

      return $res;
    }

    public function updateStatementData($statement_zip) {
      return true;
    }

    public function updateAnswerData($answer_zip) {
      //$folder - содержит путь к каталогу с данными
      //сохранить файл из архива в каталог folder
      $p = $answer_zip->getFromName('answer.txt');
      if (! $p) return false;
           else return $p;
    }

    //return column names
    public function getColumnNames($statementData) {      
      return array("solved", "tries");
    }
  }

?>