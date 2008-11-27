<?php

  function queryForContestDescription($c, $contest_id) {
    //TODO prevent sql injection    
    $set = 'SET';

    //adjust name
    if (!is_null($c->name))
      $set .= ' ' . "name='$c->name'" . ',';

    //adjust description
    if (!is_null($c->description))
      $set .= ' ' . "description='$c->description'" . ',';

    //adjust start
    if (!is_null($c->start)) {
      $date = DatePHPToMySQL($c->start);
      $set .= ' ' . "start_time='$date'" . ',';
    }

    //adjust finish
    if (!is_null($c->finish)) {
      $date = DatePHPToMySQL($c->finish);
      $set .= ' ' . "finish_time='$date'" . ',';
    }

    //adjust registration type
    if (!is_null($c->registrationType))
      $set .= ' ' . "reg_type='$c->registrationType'" . ',';

    //launch query
    if ($set != 'SET') {
      $set = rtrim($set, ',');
      return "UPDATE contest $set WHERE id=$contest_id";
    }
    else
      return "";
  }

  function queriesToAdjustProblems($problems, $contest_id) {
    $changed_probs = array();
    $queries = array();
                      /*
    mysql_query("SELECT * FROM problem WHERE contest_id = $contest_id");

    foreach ($problems as $p) {
      if ($p->id == -1)
      {
        //create new task

      }
      else
      {
        //adjust a task

      }
    }
                    */
    return $queries;
  }

  function processAdjstContestRequest($request) {
    //get db connection
    $con = connectToDB();

    //get user_id or die, if session is invalid
    $user_id = testSession($con, $request->sessionID);

    //authorize user for this operation
    // get contest ID
    $userRow = getUserRow($con, $user_id);
    $user_type = $userRow['user_type'];
        
    $requested_contest_id = $request->contest->contestID;
    $user_contest_id = $userRow['contest_id'];

    $contest_id = -1;
    if ($requested_contest_id < 0 && $user_contest_id != 0)
      $contest_id = $user_contest_id;
    elseif ($requested_contest_id == $user_contest_id && $user_contest_id != 0)
      $contest_id = $user_contest_id;
    elseif ($requested_contest_id != $user_contest_id && $user_type == "SuperAdmin")
      $contest_id = $requested_contest_id;

    if ($user_type == "Participant") $contest_id = -1;            

    if ($contest_id < 0) throwError("You don't have permissions to adjust this contest");

    //get elements to adjust
    $queries = array();

    //adjust contest description
    $query_1 = queryForContestDescription($request->contest, $contest_id);
    if ($query_1 != "")
        $queries[] = $query_1;

    //now adjust problems
    if (!is_null($request->problems))
      $queries += queriesToAdjustProblems($request->problems, $contest_id);

    //run transaction
    if (count($queries) != 0) {
      if (!transaction($con, $queries))
        throwError("Failed to make update, db error or incorrect data");
    }
    else
      throwError("Nothing updated by request");

    return new AcceptedResponse();
  }
?>