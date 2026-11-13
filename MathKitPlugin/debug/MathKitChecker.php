<?php

class MathKitChecker extends ServerPlugin {
  public function checkSolution($solution, $user_id, $answer_data, &$current_result, &$table_cols) {

    if (is_null($current_result)) {
        $current_result = array('solved' => false, 'submission' => 0);
    }

    $current_result['submission'] = $current_result['submission'] + 1;

    $current_result['solved'] = $solution['answer'] === 'accepted' || $current_result['solved'];

    if ($current_result['solved'])
        $sol = '+';
    else
        $sol = '-';

    $table_cols = array(
        $sol,
        $current_result['submission'],
        "" . $this->getTime(),
        $solution['textanswer']
    );

    return array();
  }

  //accepts one gif file and does nothing with it
  public function updateStatementData($statement_zip) {
    return true;
  }

  //accepts
  public function updateAnswerData($answer_zip) {
    return true;
  }

  //return column names
  public function getColumnNames($statementData) {
    return array("solved", "submissions", "time", "explanations");
  }


}