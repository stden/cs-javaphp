<?php
  $c = array(true, false, true);
  $c[] = false;

  echo serialize($c) . "\n";

  $cc = array("First Name", "Second Name", "School", "Класс");

  echo serialize($cc) . "\n";

/*
  $d = date();

  echo serialize($d) . "\n";
  */

  echo mktime(0,0,0,14,1,2001) . "\n";

?>
