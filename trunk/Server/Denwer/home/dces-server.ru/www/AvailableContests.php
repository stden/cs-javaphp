<?php

require("Messages.php");

function processAvailableContestRequest($request) {
  $res->contests = array();

	$c1 = new ContestDescription();
	$c1->name = "Example contest #1";
	$res->contests[] = $c1;

	$c2 = new ContestDescription();
	$c2->name = "Example contest #2";
	$res->contests[] = $c2;

  return $res;
}

?>