<?php

function processAvailableContestsRequest($request) {
  $prfx = $GLOBALS['dces_mysql_prefix'];
  $con = connectToDB();

  $res = new AvailableContestsResponse();
  $res->contests = array();

  $contest_rows = mysql_query("SELECT * FROM ${prfx}contest") or throwServerProblem(27, mysql_error());

  while($row = mysql_fetch_array($contest_rows))
  {
    $c = Data::_unserialize($row['settings']);

    $c->contestID = (int)$row['id'];    

    $res->contests[] = $c;
  }

  return $res;
}

?>