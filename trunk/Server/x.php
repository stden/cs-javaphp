<?php
  function xx( $a ){
  	$a = str_replace('\0',chr(0),$a);
  	$a = str_replace('\"','"',$a);
  	$a = str_replace("\'","'",$a);
  	$a = str_replace("\\\\","\\",$a);  	
	return $a;
  }
  
  // Зарегистрированные типы сообщений
  class Sum{};
  
  if(isset($sum)){
    $s = unserialize(xx($sum));	
	$res = $s->a + $s->b;
	print serialize( $res );
  }
?>
