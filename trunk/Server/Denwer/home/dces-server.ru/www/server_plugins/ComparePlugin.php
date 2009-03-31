<?php

  //this plugin doesn't use a plugin folder to store any info

  class ComparePlugin extends ServerPlugin {

    public function checkSolution($solution, $user_id, $answer_data, &$current_result, &$table_cols) {        

      $res = array();
      $ok = $solution['answer'] === $answer_data;
      $res['result'] = $ok ? "yes" : "no";
      if ($ok) {
        $current_result = "+";
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
      $p = $answer_zip->getFromName('answer.txt');
      if (! $p) return false;
           else return $p;
    }
  }

?>