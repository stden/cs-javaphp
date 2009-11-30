<?php

  class ComparePlugin extends ServerPlugin {

    private function getLineForUser($user_id, $lines_count) {
      return $user_id % $lines_count;
    }

    public function checkSolution($solution, $user_id, $answer_data, $previous_result) {
      $res = array();
      $res['result'] = $solution['answer'] === $answer_data ? "yes" : "no";
      return $res;
    }

    //returns cheking result
    public function checkSolution($solution, $user_id, $answer_data, $previous_result) {
      $lines = file($this->folder . '/problem.answer');
      $no = $this->getLineForUser($user_id, count($lines));

      //return $this->
    }    

    public function getStatement($user_id, $statement_data) {
      $lines = file($this->folder . '/problem.data');
      $no = $this->getLineForUser($user_id, count($lines));

      $data = 'problem.prm;' . $lines[$no];

      if (! @file_put_contents($this->folder . '/result/data.tex', $data))
        return false;             

      //TODO use appropriate dirs
      system('latex ' . $this->folder . '/idz2');

      system('dvips -E ' . $this->folder . '/idz2');

      system('convert -quality 100 -density 120 ' . $this->folder . '/idz2 idz2.png');

      $zip = new ZipArchive();
      $zip->save(file_get_contents());

      return $zip;
    }

    public function updateStatementData($statement_zip) {
      //test stepanov dir is already created
      if (!file_exists($this->folder . '/idz2.tex')) {
        copy('stepanov/idz2.cls', $this->folder . '/idz2.cls');
        copy('stepanov/idz2.def', $this->folder . '/idz2.def');
        copy('stepanov/idz2.tex', $this->folder . '/idz2.tex');
        copy('stepanov/usercfg.prm', $this->folder . '/usercfg.prm');
        copy('stepanov/usercfg.sln', $this->folder . '/usercfg.sln');
        mkdir($this->folder . '/result');
        copy('stepanov/result/macros.tex', $this->folder . '/result/macros.tex');
        copy('stepanov/result/tuning.tex', $this->folder . '/result/tuning.tex');
        mkdir($this->folder . '/template');
      }

      if (! @file_put_contents($this->folder . '/template/problem.prm', $statement_zip->getFromName('problem.prm')))
        return false;

      if (! @file_put_contents($this->folder . '/problem.data', $statement_zip->getFromName('problem.data')))
        return false;

      return null;
    }

    //returns string to configure db answer settings
    //params:
    // $folder - FileFolder object
    public function updateAnswerData($answer_zip) {
      if (! @file_put_contents($this->folder . '/problem.answer', $answer_zip->getFromName('problem.answer')))
        return false;

      return null;
    }
  }

?>