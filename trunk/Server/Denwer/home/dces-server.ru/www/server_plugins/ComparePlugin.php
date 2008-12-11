<?php

  //this plugin doesn't use a plugin folder to store any info

  class ComparePlugin extends ServerPlugin {

    public function checkSolution($solution, $user_id, $answer_data, $previous_result) {
      $res = array();
      $res['result'] = $solution['answer'] === $answer_data ? "yes" : "no";
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