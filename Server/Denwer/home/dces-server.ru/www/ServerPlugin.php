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
    // $statement_folder - already open zip file
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

  //gets contents of $s, treats it as a zip, opens zip and returns it.
  //remove_handle is a value to be passed to closeZip() function
  function openZip($s, &$remove_handle) {
    $remove_handle = random_str(10);
    if (!file_put_contents("temp/$remove_handle.zip", $s))
      return false;
    $zip = new ZipArchive;
    $res = $zip->open('temp/$remove_handle.zip');
          /*
    echo "O:";
    var_dump($res);
    var_dump(ZIPARCHIVE::ER_EXISTS);
    var_dump(ZIPARCHIVE::ER_INCONS);
    var_dump(ZIPARCHIVE::ER_INVAL);
    var_dump(ZIPARCHIVE::ER_MEMORY);
    var_dump(ZIPARCHIVE::ER_NOENT);
    var_dump(ZIPARCHIVE::ER_NOZIP);
    var_dump(ZIPARCHIVE::ER_OPEN);
    var_dump(ZIPARCHIVE::ER_READ);
    var_dump(ZIPARCHIVE::ER_SEEK);
    if ($res !== true) return false;
    */
    return $zip;
  }

  function closeZip($handle) {
    //if (! is_null($handle)) unlink("temp/$handle");
  }

?>