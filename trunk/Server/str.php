<?php
  $a = $_REQUEST['a'];
  $code = $_REQUEST['code'];
  if(isset($a)){
  	$a = str_replace('\0',chr(0),$a);
  	$a = str_replace('\"','"',$a);
  	$a = str_replace("\'","'",$a);
  	$a = str_replace("\\\\","\\",$a);
    echo $a;
  } elseif(isset($code)){
  	echo ord($code);
  }
?>
