<?php
  function DateMySQLToPHP($mysqldate) {
    return strtotime( $mysqldate );
  }

  function DatePHPToMySQL($phpdate) {
    return date( 'Y-m-d H:i:s', $phpdate );
  }
?>