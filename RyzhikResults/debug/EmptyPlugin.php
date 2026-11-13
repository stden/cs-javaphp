<?php
/**
 * Created by IntelliJ IDEA.
 * User: I. Posov
 * Date: 09.05.2009
 * Time: 14:50:53
 * To change this template use File | Settings | File Templates.
 */

 class EmptyPlugin extends ServerPlugin {

  public function checkSolution($solution, $user_id, $answer_data, &$current_result, &$table_cols) {
    //do nothing
  }

  //accepts
  public function updateStatementData($statement_zip) {
    return true;
  }

  //accepts
  public function updateAnswerData($answer_zip) {
    return true;
  }

  //return column names
  public function getColumnNames($statementData) {
    return array();
  }

}

?>