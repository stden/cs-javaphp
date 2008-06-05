<?php

  require("phpsvnclient.php");

  $now = strtotime("Thu, 29 Nov 2007 06:37:00 GMT"); 
  print_r(strtotime("Thu, 29 Nov 2007 06:38:01 GMT")-$now);


  echo "Creating new phpSVNClient Object.<br>";
  $svn = new phpsvnclient;

  $svn->setRespository("http://php-ajax.googlecode.com/svn/");

  echo 'Get Files from "/trunk/phpajax/" directory from the last repository version<br>';

  $files_now = $svn->getDirectoryFiles("/trunk/phpajax/");
  echo "<pre>";
  print_r($files_now);
  echo "</pre>";

  foreach($files_now as $k=>$v){
    $path = $v['path'];
    echo "Get \"$path\"  contents from the last repository version<br>";
    $f = $svn->getFile($path);
    $fileName = str_replace("trunk/phpajax/","out/",$path);
    echo "Save to: $fileName<br>";
    $fd = fopen($fileName, "w");
    fwrite($fd,$f);
    fclose($fd);
  }

  echo 'Get all logs of /trunk/phpajax/phpajax.org from  between 2 version until the last';
  $logs = $svn->getRepositoryLogs(2);

  echo 'Get all logs of /trunk/phpajax/phpajax.org from  between 2 version until 5 version.';
  $logs = $svn->getRepositoryLogs(2,5);


?>
