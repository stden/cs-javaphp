<?php

function processAvailableContestsRequest($request) {
  $prfx = $GLOBALS['dces_mysql_prefix'];
  $con = connectToDB();

  $res = new AvailableContestsResponse();
  $res->contests = array();

  $contest_rows = mysql_query("SELECT * FROM ${prfx}contest") or throwServerProblem(27, mysql_error());

  while($row = mysql_fetch_array($contest_rows))
  {
    //echo $row['FirstName'] . " " . $row['LastName'];
    $c = new ContestDescription();

    $c->contestID = (int)$row['id'];
    $c->name = $row['name'];
    $c->description = $row['description'];
    $c->start = DateMySQLToPHP($row['start_time']);
    $c->finish = DateMySQLToPHP($row['finish_time']);
    $c->registrationType = $row['reg_type'];   
    $c->data = unserialize($row['user_data']) or $c->data = array();
    $c->compulsory = unserialize($row['user_data_compulsory']) or $c->compulsory = array();

    $res->contests[] = $c;
  }

  return $res;
}

?>