<?php

  /*abstract*/ class ServerPlugin {

    //plugin folder
    protected $folder;

    public function __construct($folder) {
      //may be overriden, but must call parent constructor      
      $this->folder = $folder;
    }

    //returns submission result
    public function checkSolution($solution, $user_id, $answer_data, &$current_result, &$table_cols) {
      //Needs to be overriden
    }

    public function getStatement($user_id, $statement_data) {
      //May be overriden
      return $this->getStatementData(basename($this->folder));
    }

    //returns string to configure db statement settings
    //params:
    // $statement_zip - already open zip file
    public function updateStatementData($statement_zip) {
      //May be overriden
      return null;
    }

    //returns string to configure db answer settings
    //params:
    // $answer_zip - already open zip file
    public function updateAnswerData($answer_zip) {
      //May be overriden
      return null;
    }

    public function getStatementData($problem_id) {
      //May NOT be overriden
      return @file_get_contents($GLOBALS['dces_dir_problems'] . '/' . $problem_id . "_statement.zip");
    }

    public function getAnswerData($problem_id) {
      //May NOT be overriden
      return @file_get_contents($GLOBALS['dces_dir_problems'] . '/' . $problem_id . "_answer.zip");
    }

    //return column names
    public function getColumnNames($statementData) {
      //May and usually should be overriden
      return array("");
    }

  }

  //gets contents of $s, treats it as a zip, opens zip and returns it.
  //remove_handle is a value to be passed to closeZip() function
  function openZip($s, $zip_file) {    
    if (!file_put_contents($zip_file, $s)) return false;
    $zip = new ZipArchive();
    $res = $zip->open($zip_file);
    if ($res === true)
      return $zip;
    else
      return false;
  }
?>