<?php

  require("phpsvnclient.php");

  // ������ � ���� ������
  function SaveToFile( $fileName, $data ){
    $fd = fopen($fileName, "w");
    fwrite($fd,$data);
    fclose($fd);
  }

  // ������ ����� � ���� ������
  function ReadFromFile( $fileName ){
    $fd = fopen($fileName, "r");
    $res = fread($fd, filesize($fileName)); 
    fclose($fd);
    return $res;
  }

  // ������� �� $time1, ��� $time2?
  function isNew( $time1, $time2 ){
    return strtotime($time1) > strtotime($time2);
  }

  // ������ SVN-������ � ������������ � �����������
  $svn = new phpsvnclient;
  $svn->setRespository("http://cs-javaphp.googlecode.com/svn/");
  echo "Revision: ".$svn->getVersion();

  // ������������� ���������� � �������� ������ ������
  $subDir = "trunk/Server/SVN";
  $files_now = $svn->getDirectoryFiles("/$subDir");
  //echo "<pre>"; print_r($files_now); echo "</pre>"; // �������� ��� �������

  // ��������������� ���������� ������ (���� ��� ����)
  $SVN_State = "_svn_.txt";
  $x = is_file($SVN_State) ? unserialize(ReadFromFile($SVN_State)) : array();    

  echo "<table border=1><tr><td><b>SVN-����<td><b>Save to:<td><b>last-mod<td><b>file-mod<td><b>Actions";
  foreach($files_now as $k=>$v){
    $path = $v['path'];
    $fileName = str_replace("$subDir/","",$path);
    // ��������� ���������� ���������
    $prev_svn = $x[$path]['svn'];
    $prev_file = $x[$path]['file'];
    // ��������� ���������
    $cur_svn = $v['last-mod'];
    $cur_file = filemtime($fileName);

    echo "<tr><td>$path<td>$fileName<td>$cur_svn - $prev_svn<td>$cur_file - $prev_file";


    $action = ""; // ������ �� ������
    if(isNew($cur_svn,$prev_svn))
      $action = "update";
    if(!isset($prev_file))
      $prev_file = filemtime($fileName);
    if($cur_file > $prev_file)
      $action = "modified";
    if($action == "update"){
      $action = "updated!";
      SaveToFile( $fileName, $svn->getFile($path) );
      $cur_file = filemtime($fileName);
      // ��������� ����� ���������
      $x[$path] = array( 
        'svn' => $cur_svn,
        'file' => $cur_file 
      );
    } 
    echo "<td>$action";
  }
  echo "</table>";
  SaveToFile( $SVN_State, serialize($x) );

  echo 'Get all logs between 2 version until the last';
  $logs = $svn->getRepositoryLogs(2);
  print_r($logs);

  echo 'Get all logs between 2 version until 5 version.';
  $logs = $svn->getRepositoryLogs(2,5);
  print_r($logs);

?>
