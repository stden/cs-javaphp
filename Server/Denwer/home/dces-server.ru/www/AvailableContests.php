<?php

function processAvailableContestsRequest($request) {
  $con = connectToDB();

  $res = new AvailableContestsResponse();
  $res->contests = array();

  $contest_rows = mysql_query("SELECT * FROM contest");  

  while($row = mysql_fetch_array($contest_rows))
  {
    //echo $row['FirstName'] . " " . $row['LastName'];
    $c = new ContestDescription();

    $c->contestID = (int)$row['id'];
    $c->name = $row['name'];
    $c->description = $row['description'];
    //$c->start = $row['start_time'];
    //$c->finish = $row['finish_time'];
    $c->registrationType = $row['reg_type'];
    //$c->data = unserialize($row['user_data']);
    //$c->compulsory = unserialize($row['user_data_compulsory']);
    $c->data = array("school", "name");
    $c->compulsory = array(true, false);

    $res->contests[] = $c;
  }

  return $res;
}

?>