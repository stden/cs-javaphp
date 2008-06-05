<?php

  require("phpsvnclient.php");

  $now = strtotime("Thu, 29 Nov 2007 06:37:00 GMT"); 
  print_r(strtotime("Thu, 29 Nov 2007 06:38:01 GMT")-$now);

  $svn = new phpsvnclient;
  $svn->setRespository("http://cs-javaphp.googlecode.com/svn/");
  $subDir = "trunk/Server/SVN";
  $files_now = $svn->getDirectoryFiles("/$subDir");
  echo "<pre>"; print_r($files_now); echo "</pre>";

  foreach($files_now as $k=>$v){
    $path = $v['path'];
    echo "<hr>Get \"$path\"  contents from the last repository version<br>";
    $f = $svn->getFile($path);
    $fileName = str_replace("$subDir/","",$path);
    echo "Save to: $fileName<br>";
    $fd = fopen($fileName, "w");
    fwrite($fd,$f);
    fclose($fd);
  }

  echo 'Get all logs of /trunk/phpajax/phpajax.org from  between 2 version until the last';
  $logs = $svn->getRepositoryLogs(2);
  print_r($logs);

  echo 'Get all logs of /trunk/phpajax/phpajax.org from  between 2 version until 5 version.';
  $logs = $svn->getRepositoryLogs(2,5);
  print_r($logs);

?>
