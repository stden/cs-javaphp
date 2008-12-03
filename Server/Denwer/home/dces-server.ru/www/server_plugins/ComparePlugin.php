<?php

  //this plugin doesn't use a plugin folder to store any info

  class ComparePlugin extends ServerPlugin {
    public function checkSolution($solution, $user_id, $answer_data) {
      return $solution === $answer_data;
    }

    public function updateStatementData($statement_zip) {      
      $p = $statement_zip->getFromName('statement.txt');
      if (! $p) return false;
           else return $p;
    }

    public function updateAnswerData($answer_zip) {
      $p = $answer_zip->getFromName('answer.txt');
      if (! $p) return false;
           else return $p;
    }
  }

?>