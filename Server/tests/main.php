<?php

$preIP = dirname(__FILE__);
require_once("$preIP/utils/RequestSender.php");
require_once("$preIP/utils/test_helper.php");
require_once("$preIP/DCESBaseTestCase.php");
require_once("$preIP/../mocks/all_mocks.php");
//TODO invent something that makes $preIP not overridable in required phps
$preIP = dirname(__FILE__);
require_once("$preIP/data/test_data.php");
require_once("PHPUnit/Framework.php");
?>