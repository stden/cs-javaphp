<?php

function throwError($msg) {

  $err = new RequestFailedResponse();
  $err->message = $msg;

  $magic = chr(4) . chr(2) . chr(3) . chr(9);
  echo $magic;
  echo serialize($err);
  exit();
}

?>