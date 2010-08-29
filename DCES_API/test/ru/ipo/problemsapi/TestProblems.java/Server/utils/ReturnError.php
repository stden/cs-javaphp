<?php

function throwError($type, $errNo, $msg) {
  $err = new RequestFailedResponse();

  $err->failReason = $type;
  $err->failErrNo = $errNo;
  $err->extendedInfo = $msg;

  //$magic = chr(4) . chr(2) . chr(3) . chr(9);
  //echo $magic;
  echo serialize($err);
  exit();
}

function throwServerProblem($errNo, $msg = null) {
  throwError("BrokenServerError", $errNo, $msg);
}

function throwServerPluginProblem($errNo, $msg = null) {
  throwError("BrokenServerPluginError", $errNo, $msg);
}

function throwBusinessLogicError($errNo, $msg = null) {
  throwError("BusinessLogicError", $errNo, $msg);
}

?>