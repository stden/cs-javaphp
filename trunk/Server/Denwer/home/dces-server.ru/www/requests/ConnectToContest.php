<?php

function processConnectToContestRequest($request) {
  $prfx = $GLOBALS['dces_mysql_prefix'];

  //find user in table
  $row = Data::getRow(
                    sprintf("SELECT * FROM ${prfx}user WHERE login=%s AND contest_id=%s",
                            Data::quote_smart($request->login),
                            Data::quote_smart($request->contestID)
                           )
                  );

  //test if there is at least one user
  if ( !$row )
      throwBusinessLogicError(12);

  //test password
  if ($row['password'] !== $request->password)
      throwBusinessLogicError(12);

  //TODO implement the test: if user $row['id'] is a participant, then connection is allowed only after the beginning of the contest

  //start new session
  $session_id = RequestUtils::createSession($row['id']);

  $res = new ConnectToContestResponse();
  $res->sessionID = $session_id;
  $res->user = new UserDescription();
  $res->user->userID = (int)$row['id'];
  $res->user->login = $request->login;
  $res->user->dataValue = Data::_unserialize($row['user_data'], array());
  $res->user->userType = $row['user_type'];

  return $res;
}

?>