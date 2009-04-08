<?php
  function DateMySQLToPHP($mysqldate) {
    return strtotime( $mysqldate );
  }

  function DatePHPToMySQL($phpdate) {
    return date( 'Y-m-d H:i:s', $phpdate );
  }

  //returns
  //array:['interval'] = 'before' 'contest' 'after'
  //      ['is_ending'] = true false
  function getCurrentContestTime($contest_settings) {
    $now = getdate();
    $now = $now[0];

    $start = $contest_settings->start;
    $finish = $contest_settings->finish;
    if ($contest_settings->contestTiming->selfContestStart) {
        $ending_start = $finish;
        $ending_finish = $finish;
    } else {
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