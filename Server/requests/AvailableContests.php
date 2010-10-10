<?php

function processAvailableContestsRequest($request) {
  $prfx = $GLOBALS['dces_mysql_prefix'];

  $res = new AvailableContestsResponse();
  $res->contests = array();

  $contest_rows = Data::getRows("SELECT * FROM ${prfx}contest");

  while($row = Data::getNextRow($contest_rows))
  {
    $c = Data::_unserialize($row['settings']);

    $c->contestID = (int)$row['id'];    

    $res->contests[] = $c;
  }

  return $res;
}

?>