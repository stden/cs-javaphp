<?php
  $zip = new ZipArchive;
  $res = $zip->open('test.zip');
  if ($res === TRUE) {
    echo 'ok';
    $zip->extractTo('test');
    $zip->close();
  } else {
    echo 'failed, code:' . $res;
  }

  exit;

/*  $a = false;
  $b = 0;
  $c = 1;

  if ($a) echo "a";
  if ($b) echo "b";
  if ($c) echo "c";

  exit;

  function f(&$a = null) {
    var_dump($a);
    //$a[42] = 239;
  }

  $a = array();
  f($a);
  f();
  exit;

  require("dces-settings.php");
  require("DataBase.php");

  $con = connectToDB();

  mysql_query("INSERT INTO problem (name, client_plugin_id, server_plugin_id, contest_id) VALUES ('n1', '1', '2', '3')");

  var_dump(mysql_insert_id());

  mysql_query("UPDATE problem SET name = 'n2'");

  var_dump(mysql_insert_id());
*/
?>
