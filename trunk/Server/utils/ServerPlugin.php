<?php
  
  class ServerPlugin {

    protected $problem;    

    public function __construct($problem) {
      //may be overriden, but must call parent constructor      
      $this->problem = $problem;
    }
    
    /** saves state */
    protected function saveState($state) {
    	//TODO implement
    }

    /** Must be overriden.     
     * @param $solution
     * @param $submission 
     * @return hash map string->string with columns data
     */
    public function checkSolution($solution, $submission) {
      
    }

    /** Must be overriden
     * array of the form array('rt'=>arrayRT, 'ad'=>arrayAD);
     * where arrayRT is a list of column names that are in results table
     * and arrayAD list of additional column names 
     * @return array of described type
     */
    public function getColumnNames() {      
      return array('rt'=>array(), 'ad'=>array());
    }

  }
?>