<?php
  $a = str_replace('\\0','\0',$a);
  $len = strlen($a);
  $b = "";
  for($i=$len-1;$i>=0;$i--){
    // ���������� ������ �������� �����
    if ($a[$i] == '\\') $i--;
    $b .= $a[$i];
  }
  echo $b;
?>
