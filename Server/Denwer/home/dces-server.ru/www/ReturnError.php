<?php

function throwError($msg) {

  $err = new RequestFailedResponse();
  $err->message = $msg;

  echo serialize($err);
  exit();
}

?>