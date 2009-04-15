# HeidiSQL Dump 
#
# --------------------------------------------------------
# Host:                 127.0.0.1
# Database:             ___DCES___INITIAL___DB___
# Server version:       5.0.45-community-nt
# Server OS:            Win32
# Target-Compatibility: Standard ANSI SQL
# HeidiSQL version:     3.2 Revision: 1129
# --------------------------------------------------------

/*!40100 SET CHARACTER SET cp1251;*/
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ANSI';*/
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;*/


#
# Table structure for table 'PREFIX_client_plugin'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "PREFIX_client_plugin" (
  "alias" varchar(48) NOT NULL COMMENT 'User friendly plugin name',
  "description" text NOT NULL,
  PRIMARY KEY  ("alias")
) /*!40100 DEFAULT CHARSET=utf8*/;



#
# Table structure for table 'PREFIX_contest'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "PREFIX_contest" (
  "id" int(10) unsigned NOT NULL auto_increment,
  "settings" blob NOT NULL COMMENT 'serialized settings',
  PRIMARY KEY  ("id"),
  UNIQUE KEY "id" ("id"),
  KEY "id_2" ("id")
) /*!40100 DEFAULT CHARSET=utf8*/;



#
# Table structure for table 'PREFIX_problem'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "PREFIX_problem" (
  "id" int(10) unsigned NOT NULL auto_increment,
  "name" varchar(64) NOT NULL COMMENT 'User friendly task name',
  "statement" blob NOT NULL COMMENT 'Serialized problem statement data, controlled by plugin',
  "answer" blob NOT NULL COMMENT 'Serialized problem answer data, controlled by plugin',
  "client_plugin_alias" varchar(48) NOT NULL,
  "server_plugin_alias" varchar(48) NOT NULL,
  "contest_id" int(10) unsigned NOT NULL,
  "contest_pos" int(10) unsigned NOT NULL,
  "column_names" blob NOT NULL COMMENT 'Serialized array with column names',
  PRIMARY KEY  ("id"),
  UNIQUE KEY "id" ("id"),
  KEY "id_2" ("id")
) AUTO_INCREMENT=153 /*!40100 DEFAULT CHARSET=utf8 COMMENT='Задача'*/;



#
# Table structure for table 'PREFIX_problem_status'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "PREFIX_problem_status" (
  "id" int(10) unsigned NOT NULL auto_increment,
  "problem_id" int(10) unsigned NOT NULL COMMENT 'Problem of the result',
  "user_id" int(10) unsigned NOT NULL COMMENT 'User of the result',
  "status" blob NOT NULL COMMENT 'serialized problem status',
  PRIMARY KEY  ("id"),
  UNIQUE KEY "task_result_id" ("id"),
  KEY "task_result_id_2" ("id")
) /*!40100 DEFAULT CHARSET=utf8*/;



#
# Table structure for table 'PREFIX_server_plugin'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "PREFIX_server_plugin" (
  "alias" varchar(48) NOT NULL COMMENT 'User friendly plugin name',
  "description" text NOT NULL,
  PRIMARY KEY  ("alias")
) /*!40100 DEFAULT CHARSET=utf8*/;



#
# Table structure for table 'PREFIX_session'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "PREFIX_session" (
  "session_id" varchar(48) NOT NULL,
  "user_id" int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  ("session_id"),
  UNIQUE KEY "user_id" ("user_id","session_id"),
  UNIQUE KEY "Session" ("session_id"),
  KEY "user_id_2" ("user_id")
) /*!40100 DEFAULT CHARSET=utf8*/;



#
# Table structure for table 'PREFIX_submission_history'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "PREFIX_submission_history" (
  "id" int(10) unsigned NOT NULL auto_increment,
  "problem_id" int(10) unsigned NOT NULL COMMENT 'Problem of the result',
  "user_id" int(10) unsigned NOT NULL COMMENT 'User of the result',
  "submission" blob NOT NULL COMMENT 'serialized submition data',
  "result" blob NOT NULL COMMENT 'serialized submition result',
  "submission_time" datetime NOT NULL COMMENT 'submition time',
  PRIMARY KEY  ("id"),
  UNIQUE KEY "task_result_id" ("id"),
  KEY "task_result_id_2" ("id")
) AUTO_INCREMENT=39 /*!40100 DEFAULT CHARSET=utf8*/;



#
# Table structure for table 'PREFIX_user'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "PREFIX_user" (
  "id" int(10) unsigned NOT NULL auto_increment,
  "contest_id" int(10) unsigned NOT NULL COMMENT 'User''s contest',
  "login" varchar(24) NOT NULL,
  "password" varchar(24) NOT NULL,
  "user_type" enum('Participant','ContestAdmin','SuperAdmin') NOT NULL default 'Participant',
  "user_data" blob NOT NULL COMMENT 'Serialized array with user data',
  "results" blob NOT NULL COMMENT 'Serialized array with results',
  "contest_start" datetime default NULL COMMENT 'moment of the first login',
  PRIMARY KEY  ("id"),
  UNIQUE KEY "id" ("id"),
  KEY "id_2" ("id")
) AUTO_INCREMENT=26 /*!40100 DEFAULT CHARSET=utf8*/;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE;*/
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;*/
