<?php

function processCreateContestRequest($request) {
  //open db connection
  $con = connectToDB();

  //get user_id or die, if session is invalid
  $user_row = RequestUtils::testSession($request->sessionID);
  $user_id = $user_row['id'];

  //authorize user for this operation
  $user_type = $user_row['user_type'];
  if ($user_type !== 'SuperAdmin')
    throwBusinessLogicError(0);
  
  unset($request->contest->contest_id);
  $col_value = array('settings' => serialize($request->contest));

  mysql_query(composeInsertQuery('contest', $col_value)) or throwServerProblem(5, mysql_error());

  return new AcceptedResponse();
}

?>