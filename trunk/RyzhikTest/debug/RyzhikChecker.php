<?php

class RyzhikChecker extends ServerPlugin {

  private function setCols($res, $ans) {
    return array(
            $res{0} . $ans[0],
            $res{1} . $ans[1],
            $res{2} . $ans[2],
            $res{3} . $ans[3],
            $res{4} . $ans[4]
          );
  }

  //$current_result: string of answers (null means not initialized)
  //$answer_data = array[0..4] of 'x xxxxx'-like strings
  public function checkSolution($solution, $user_id, $answer_data, &$current_result, &$table_cols) {

    if (is_null($current_result)) {
        $current_result = '00000'; //5 dont know answers
        $table_cols = $this->setCols($current_result, $answer_data);
    }

    if ($solution['action'] === 'init') {
        //everything is already done
    }
    elseif ($solution['action'] === 'answer') {
        $q = $solution['question'] - 1;
        if (!is_numeric($q)) return array();
        if ($q < 0 || $q > 4) return array();
        $current_result{$q} = $solution['answer'];

        $table_cols = $this->setCols($current_result, $answer_data);
    }
    elseif ($solution['action'] === 'answers') {
        $ans = $solution['answers'];
        if (!is_string($ans)) return;
        if (strlen($ans) != 5) return;
        $current_result = $ans;

        $table_cols = $this->setCols($current_result, $answer_data);
    }
    elseif ($solution['action'] === 'get answers') {
        return array('answers' => $current_result);
    }

    return array();
  }

  //accepts one gif file and does nothing with it
  public function updateStatementData($statement_zip) {
    return true;
  }

  //accepts
  public function updateAnswerData($answer_zip) {
    //$folder - содержит путь к каталогу с данными
    //сохранить файл из архива в каталог folder
    $p = $answer_zip->getFromIndex(0);
    if (! $p) return false;

    $tmp_file = $this->folder . '/ans.txt';
    @file_put_contents($tmp_file, $p);
    $res = @file($tmp_file);

    if (count($res) != 5) return false;

    for ($i = 0; $i < 5; $i++)
        $res[$i] = trim($res[$i]);

    return $res;
  }

  //return column names
  public function getColumnNames($statementData) {
    return array("question1", "question2", "question3", "question4", "question5");
  }


}