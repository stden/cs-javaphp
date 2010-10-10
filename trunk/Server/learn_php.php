<?php

  $a = array('a','b','c');
  echo $a[1];  

  die();
  
  class A{};
  
  $a = new A;
  $a->a = array();  
  $b = $a;
  $a->a[42] = 239;
    
  /*
  $a->f1 = new A();
  $a->f2 = new A();
  
  $a->f1->v = 'q';
  
  $b = $a;
  */
  
  var_dump($a);
  echo "<br>";
  var_dump($b);
  
  
  die();

  var_dump(null);
  //var_dump($a);

  die();

	require_once 'utils/Problems.php';

	$p = new Problem('/home/ilya/tmp/probs/prob.zip');
	var_dump($p->getClientPlugin());
	var_dump($p->getServerPlugin());
	//var_dump($p->getProblemBytes());
	var_dump($p->getResource('ANSWER'));	
	var_dump($p->isTeacher('EXE'));
	var_dump($p->isTeacher('ANSWER'));
	var_dump($p->getResource('STATEMENT'));

	die();

  echo $_SERVER['DOCUMENT_ROOT'];

  die();

  $s = 'asdf';
  $i = $s.asdf();
  var_dump($i);
  die();

  class A{};
  $a = new A();
  $a->b = new A();
  $a->b->c = "42";

  var_dump($a);
  var_dump($a->b);
  var_dump($a->b->c);

  die();

  require("utils/DataBase.php");
  $dces_mysql_host = "localhost:3306";
  $dces_mysql_user = "root";
  $dces_mysql_password = "";
  $dces_mysql_db = "dcestest";
  $dces_mysql_prefix = "pr_";
  $dces_logging = true;

  $prfx = DB_PREFIX;

  //find user in table
  //test if there is at least one such user
  $row = Data::getRow(
  "SELECT ${prfx}user.*, ${prfx}contest.settings
   FROM ${prfx}session
   INNER JOIN ${prfx}user
   ON ${prfx}session.user_id=${prfx}user.id
   INNER JOIN ${prfx}contest
   ON ${prfx}user.contest_id=${prfx}contest.id
   WHERE session_id='$session_id'"
  );

  //test if there is at least one user

  var_dump($row);
  die();


  class D {
    private static $c = 42;
    static function f() {
      return D::$c;
    }
  }

//var_dump(D::$c);
var_dump(D::f());

$s = false;
if ($s === false) die('false'); else die('true'); 
die();

    $a = array();
    var_dump($a[1]);

    var_dump(ZIPARCHIVE::ER_EXISTS);
    var_dump(ZIPARCHIVE::ER_INCONS);
    var_dump(ZIPARCHIVE::ER_INVAL);
    var_dump(ZIPARCHIVE::ER_MEMORY);
    var_dump(ZIPARCHIVE::ER_NOENT);
    var_dump(ZIPARCHIVE::ER_NOZIP);
    var_dump(ZIPARCHIVE::ER_OPEN);
    var_dump(ZIPARCHIVE::ER_READ);
    var_dump(ZIPARCHIVE::ER_SEEK);
die();

$line = '/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;*/';
$line .= "  \n";
var_dump(ereg(".*;\*/[[:space:]]*$", $line));

var_dump(ereg_replace(";\*/[[:space:]]*$", "*/", $line));
die();
$is_last_line = ereg(".*;[[:space:]]*$", $line) || ereg(".*;\*/[[:space:]]*$", $line);
var_dump(ereg(".*;[[:space:]]*$", $line));
var_dump(ereg(".*;\*/[[:space:]]*$", $line));
var_dump($is_last_line);
die();

var_dump(ereg(".*;$", ";asdf;"));
var_dump(ereg(".*;$", ";asdf"));
var_dump(ereg(".*;$", "asdf;"));
var_dump(ereg(".*;$", "asdf"));

var_dump(ereg(".*;\*/$", ";asdf;*/"));
var_dump(ereg(".*;\*/$", ";asdf"));
var_dump(ereg(".*;\*/$", "asdf;"));
var_dump(ereg(".*;\*/$", "asdf"));

//$is_last_line = ereg(".*;$", $line) || ereg(".*;\*/$", $line);

die();

function f($a) {
 return array("asdf"=>$a);
}

$a = f("42");
echo $a["asdf"];

exit;
  $zip = new ZipArchive();
  $res = $zip->open('.\temp/statement.zip');
  if ($res === TRUE) {
    echo 'ok';
    //$zip->extractTo('test');
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

/*
    echo "O:";
    var_dump($res);
    var_dump(ZIPARCHIVE::ER_EXISTS);
    var_dump(ZIPARCHIVE::ER_INCONS);
    var_dump(ZIPARCHIVE::ER_INVAL);
    var_dump(ZIPARCHIVE::ER_MEMORY);
    var_dump(ZIPARCHIVE::ER_NOENT);
    var_dump(ZIPARCHIVE::ER_NOZIP);
    var_dump(ZIPARCHIVE::ER_OPEN);
    var_dump(ZIPARCHIVE::ER_READ);
    var_dump(ZIPARCHIVE::ER_SEEK);
    if ($res !== true) return false;
    */
?>
