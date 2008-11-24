<?php

function connectToDB() {
  $con = mysql_connect("localhost:3306","root","zYD4N4rZ");
  if (!$con) die('Could not connect: ' . mysql_error());
  mysql_select_db("dces", $con);
  
  return $con;
}

?>