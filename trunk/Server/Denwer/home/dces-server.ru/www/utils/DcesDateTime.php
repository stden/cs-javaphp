<?php
  function DateMySQLToPHP($mysqldate) {
    if (is_null($mysqldate)) return null;
    return strtotime( $mysqldate );
  }

  function DatePHPToMySQL($phpdate) {
    if (is_null($phpdate)) return null;
    return date( 'Y-m-d H:i:s', $phpdate );
  }

  //input - contest settings, time of contest start (or null if it is null)
  //returns
  //array:['interval'] = 'before' 'contest' 'after'
  //      ['is_ending'] = true false
  function getCurrentContestTime($contest_settings, $user_start_time, $user_finish_time) {
    if (is_null($contest_settings))
        return array('interval'=>'before', 'is_ending'=>false);

    $now = getdate();
    $now = $now[0];

    //get $start and $finish, $ending_start and $ending_finish
    if ($contest_settings->contestTiming->selfContestStart)
    {
      if (is_null($user_start_time)) return array('interval'=>'before', 'is_ending'=>false);

      $start = $user_start_time;
      if ($start < $contest_settings->start)
        $start = $contest_settings->start;
        
      if (!is_null($user_finish_time) /*&& $user_finish_time <= $now*/)
        $finish = $user_finish_time;
      else
        $finish = $start + $contest_settings->contestTiming->maxContestDuration * 60;        
      if ($finish > $contest_settings->finish)
        $finish = $contest_settings->finish;

      $ending_start = $finish;
      $ending_finish = $finish;
    }
    else
    {
      $start = $contest_settings->start;
      $finish = $contest_settings->finish;

      $ending_start = $finish - $contest_settings->contestTiming->contestEndingStart * 60;
      $ending_finish = $finish + $contest_settings->contestTiming->contestEndingFinish * 60;
    }

    if ($now < $start)
        return array('interval'=>'before', 'is_ending'=>false);
    else if ($now < $ending_start)
        return array('interval'=>'contest', 'is_ending'=>false);
    else if ($now < $finish)
        return array('interval'=>'contest', 'is_ending'=>true);
    else if ($now < $ending_finish)
        return array('interval'=>'after', 'is_ending'=>true);
    else
        return array('interval'=>'after', 'is_ending'=>false);
  }
?>