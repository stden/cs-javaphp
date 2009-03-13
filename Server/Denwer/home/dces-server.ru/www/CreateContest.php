<?php

function processCreateContestRequest($request) {
  //open db connection
  $con = connectToDB();

  //get user_id or die, if session is invalid
  $user_row = testSession($con, $request->sessionID);
  $user_id = $user_row['id'];

  //authorize user for this operation
  $user_type = $user_row['user_type'];
  if ($user_type !== 'SuperAdmin')
    throwBusinessLogicError(0);

  //get contest
  $c = $request->contest;

  $col_value = array();
  $col_value['name'] = $c->name;
  $col_value['start_time'] = DatePHPToMySQL($c->start);
  $col_value['finish_time'] = DatePHPToMySQL($c->finish);
  $col_value['description'] = $c->description;
  $col_value['reg_type'] = $c->registrationType;
  $col_value['user_data'] = serialize($c->data);
  $col_value['user_data_compulsory'] = serialize($c->compulsory);

  mysql_query(composeInsertQuery('contest', $col_value)) or throwServerProblem(5, mysql_error());

  return new AcceptedResponse();
}

?>