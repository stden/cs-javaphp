<?php

  /*abstract*/ class ServerPlugin {

    //sql connection, is to be set before calls to plugin
    protected $con;

    //plugin folder
    protected $folder;

    public function __construct($con, $folder) {
      //may be overriden, but must call parent constructor
      $this->con = $con;
      $this->folder = $folder;
    }

    //returns cheking result
    public function checkSolution($solution, $user_id, $answer_data, $previous_result) {
      //Needs to be overriden
    }

    public function getStatement($user_id, $statement_data) {
      //May be overriden
      return $this->getStatementData(basename($this->folder));
    }

    //returns string to configure db statement settings
    //params:
    // $statement_folder - already open zip file
    public function updateStatementData($statement_zip) {
      //May be overriden
      return null;
    }

    //returns string to configure db answer settings
    //params:
    // $folder - FileFolder object
    public function updateAnswerData($answer_zip) {
      //May be overriden
      return null;
    }

    public function getStatementData($problem_id) {
      //May NOT be overriden
      return file_get_contents($GLOBALS['dces_dir_problems'] . '/' . $problem_id . "_statement.zip");
    }

    public function getAnswerData($problem_id) {
      //May NOT be overriden
      return file_get_contents($GLOBALS['dces_dir_problems'] . '/' . $problem_id . "_answer.zip");
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