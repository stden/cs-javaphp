<?php

  class A {

    function __construct() {
       echo "A called\n";
    }

  }

  class B extends A {
///*
    function __construct() {
//       parent::__construct();
       echo "B called\n";
    }
//*/
  }

  $a = "B";
  $b = new $a();

?>
