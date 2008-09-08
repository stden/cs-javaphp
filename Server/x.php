<?php
  function prepare( $a ){
  	$a = str_replace('\0',chr(0),$a);
  	$a = str_replace('\"','"',$a);
  	$a = str_replace("\'","'",$a);
  	$a = str_replace("\\\\","\\",$a);  	
	return $a;
  }
  
  // Registered message types
  class Sum{};
  class AvailableContestsRequest{};
  class AvailableContestsResponse{
  	var $contests;
  };
  class ContestDescription{
  	
  };

  if(!isset($_REQUEST['x'])){
    echo "Должен быть параметр!";
	die();
  }
  $s = unserialize(prepare($_REQUEST['x']));	
  switch(get_class($s)){
	case 'Sum': $res = $s->a + $s->b; break;
	case 'AvailableContestsRequest': 
	  $res = '!!!!'; break;
	default: 
	  $res = 'Unknown message type "'.get_class($s).'"';
  };
  echo serialize($res);
?>
