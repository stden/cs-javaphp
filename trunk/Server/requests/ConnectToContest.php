<?php

function processConnectToContestRequest($request) {
  $prfx = $GLOBALS['dces_mysql_prefix'];

  //find user in table
  $row = Data::getRow(
                    sprintf("SELECT ${prfx}user.*, ${prfx}contest.settings
                             FROM ${prfx}user
                             LEFT JOIN ${prfx}contest
                             ON ${prfx}user.contest_id = ${prfx}contest.id
                             WHERE login=%s AND contest_id=%s",
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

  //get contest settings and contest time
  $settings = Data::_unserialize($row['settings'], null);

  if (is_null($row['contest_start'])) {
    $now = getdate();
    $now = $now[0];

    $q = composeUpdateQuery(
            'user',
            array('contest_start'=>DatePHPToMySQL($now)),
            "id=${row['id']}"
         );
    Data::submitModificationQuery($q);

    $row['contest_start'] = $now;
  }

  $contest_time = getCurrentContestTime(
    $settings,
    DateMySQLToPHP($row['contest_start']),
    DateMySQLToPHP($row['contest_finish'])
  );

  if ($contest_time['interval'] === 'before' && $row['user_type'] === 'Participant')
    throwBusinessLogicError(19);

  //start new session
  $session_id = RequestUtils::createSession($row['id']);

  //get finish time
  if ($settings->contestTiming->selfContestStart)
    $finish_time = $row['contest_start'] + 60 * $settings->contestTiming->maxContestDuration;
  else
    $finish_time = $settings->finish;

  if (is_null($finish_time))
    $finish_time = 0;

  $res = new ConnectToContestResponse();
  $res->sessionID = $session_id;
  $res->finishTime = $finish_time;
  $res->user = new UserDescription();
  $res->user->userID = (int)$row['id'];
  $res->user->login = $request->login;
  $res->user->dataValue = Data::_unserialize($row['user_data'], array());
  $res->user->userType = $row['user_type'];

  return $res;
}

?>