<?php

  //doesn't use plugin folder to store info

  class ComparePlugin extends ServerPlugin {
    public function checkSolution($solution, $user_id, $answer_data) {
      return $solution === $answer_data;
    }

    public function updateStatementData($statement_folder) {
      $p = $statement_folder->getFromName('statement.txt');
      if (! $p) return false;
           else return $p;
    }

    public function updateAnswerData($answer_folder) {
      $p = $statement_folder->getFromName('answer.txt');
      if (! $p) return false;
           else return $p;
    }
  }

?>