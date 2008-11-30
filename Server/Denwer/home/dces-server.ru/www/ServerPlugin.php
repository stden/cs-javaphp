<?php

  /*abstract*/ class ServerPlugin {

    //sql connection, is to be set before calls to plugin
    public $con;

    //plugin folder
    public $folder;

    public function __construct($con, $folder) {
      $this->con = $con;
      $this->folder = $folder;
    }

    //returns cheking result
    public function checkSolution($solution, $user_id, $answer_data) {
      //Needs overriding
    }

    //returns string to configure db statement settings
    //params:
    // $statement_folder - FileFolder object
    public function updateStatementData($statement_folder) {
      //Needs overriding
    }

    //returns string to configure db answer settings
    //params:
    // $folder - FileFolder object
    public function updateAnswerData($answer_folder) {
      //Needs overriding    
    }

  }
?>