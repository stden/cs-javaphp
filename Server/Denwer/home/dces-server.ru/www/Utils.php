<?php

  function getRequestedContest($requested_contest_id, $user_contest_id, $user_type) {

    $contest_id = -1;
    if ($requested_contest_id < 0 && $user_contest_id != 0)
      $contest_id = $user_contest_id;
    elseif ($requested_contest_id == $user_contest_id && $user_contest_id != 0)
      $contest_id = $user_contest_id;
    elseif ($requested_contest_id != $user_contest_id && $user_type === "SuperAdmin")
      $contest_id = $requested_contest_id;

    return $contest_id;
  }

  // ������� ������������� ����������
  function quote_smart($value)
  {
    /*
    // ���� magic_quotes_gpc �������� - ���������� stripslashes
    if (get_magic_quotes_gpc()) {
        $value = stripslashes($value);
    }
    */
    // ���� ���������� - �����, �� ������������ � �� �����
    // ���� ��� - �� ������� � ���������, � ����������
    if (!is_numeric($value)) {
        $value = "'" . mysql_real_escape_string($value) . "'";
    }
    return $value;
  }

?>