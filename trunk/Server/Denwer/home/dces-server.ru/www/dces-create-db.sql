# HeidiSQL Dump
#
# --------------------------------------------------------
# Host:                 127.0.0.1
# Database:             dces
# Server version:       5.0.45-community-nt
# Server OS:            Win32
# Target-Compatibility: Standard ANSI SQL
# HeidiSQL version:     3.2 Revision: 1129
# --------------------------------------------------------

/*!40100 SET CHARACTER SET cp1251;*/
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ANSI';*/
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;*/


#
# Database structure for database 'dces'
#

CREATE DATABASE /*!32312 IF NOT EXISTS*/ "$db" /*!40100 DEFAULT CHARACTER SET utf8 */;

USE "$db";


#
# Table structure for table 'client_plugin'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "client_plugin" (
  "alias" varchar(48) NOT NULL COMMENT 'User friendly plugin name',
  "description" text NOT NULL,
  PRIMARY KEY  ("alias")
) /*!40100 DEFAULT CHARSET=utf8*/;



#
# Dumping data for table 'client_plugin'
#

TRUNCATE TABLE "client_plugin";
# (No data found.)

#
# Table structure for table 'contest'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "contest" (
  "id" int(10) unsigned NOT NULL auto_increment,
  "name" varchar(64) NOT NULL COMMENT 'User friendly name',
  "start_time" datetime NOT NULL,
  "finish_time" datetime NOT NULL,
  "description" text NOT NULL COMMENT 'description',
  "reg_type" enum('Self','ByAdmins') NOT NULL default 'ByAdmins' COMMENT 'Registration type',
  "user_data" text NOT NULL COMMENT 'Serialized array with user data',
  "user_data_compulsory" text NOT NULL COMMENT 'Serialized array with compulsory booleans',
  PRIMARY KEY  ("id"),
  UNIQUE KEY "id" ("id"),
  KEY "id_2" ("id")
) AUTO_INCREMENT=13 /*!40100 DEFAULT CHARSET=utf8*/;



#
# Dumping data for table 'contest'
#

TRUNCATE TABLE "contest";
# (No data found.)

#
# Table structure for table 'problem'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "problem" (
  "id" int(10) unsigned NOT NULL auto_increment,
  "name" text NOT NULL COMMENT 'User friendly task name',
  "statement" text COMMENT 'Problem statement data, controlled by plugin',
  "answer" text COMMENT 'Problem answer data, controlled by plugin',
  "client_plugin_alias" varchar(48) NOT NULL,
  "server_plugin_alias" varchar(48) NOT NULL,
  "contest_id" int(10) unsigned NOT NULL,
  "contest_pos" int(10) unsigned NOT NULL,
  PRIMARY KEY  ("id"),
  UNIQUE KEY "id" ("id"),
  KEY "id_2" ("id")
) AUTO_INCREMENT=129 /*!40100 DEFAULT CHARSET=utf8 COMMENT='Задача'*/;



#
# Dumping data for table 'problem'
#

TRUNCATE TABLE "problem";
# (No data found.)

#
# Table structure for table 'server_plugin'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "server_plugin" (
  "alias" varchar(48) NOT NULL COMMENT 'User friendly plugin name',
  "description" text NOT NULL,
  PRIMARY KEY  ("alias")
) /*!40100 DEFAULT CHARSET=utf8*/;



#
# Dumping data for table 'server_plugin'
#

TRUNCATE TABLE "server_plugin";
# (No data found.)

#
# Table structure for table 'session'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "session" (
  "session_id" varchar(48) NOT NULL,
  "user_id" int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  ("session_id"),
  UNIQUE KEY "user_id" ("user_id","session_id"),
  UNIQUE KEY "Session" ("session_id"),
  KEY "user_id_2" ("user_id")
) /*!40100 DEFAULT CHARSET=utf8*/;



#
# Dumping data for table 'session'
#

TRUNCATE TABLE "session";
# (No data found.)

#
# Table structure for table 'task_result'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "task_result" (
  "task_result_id" int(10) unsigned NOT NULL auto_increment,
  "contest_id" int(10) unsigned NOT NULL COMMENT 'Contest of the result',
  "problem_id" int(10) unsigned NOT NULL COMMENT 'Problem of the result',
  "user_id" int(10) unsigned NOT NULL COMMENT 'User of the result',
  "submissoion" blob COMMENT 'submition data',
  "result" blob COMMENT 'submition result',
  "submission_time" datetime NOT NULL COMMENT 'submition time',
  PRIMARY KEY  ("task_result_id"),
  UNIQUE KEY "task_result_id" ("task_result_id"),
  KEY "task_result_id_2" ("task_result_id")
) /*!40100 DEFAULT CHARSET=utf8*/;



#
# Dumping data for table 'task_result'
#

TRUNCATE TABLE "task_result";
# (No data found.)



#
# Table structure for table 'user'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "user" (
  "id" int(10) unsigned NOT NULL auto_increment,
  "login" varchar(24) NOT NULL,
  "password" varchar(24) NOT NULL,
  "user_data" text NOT NULL COMMENT 'Serialized array with user data',
  "contest_id" int(10) unsigned NOT NULL COMMENT 'User''s contest',
  "user_type" enum('Participant','ContestAdmin','SuperAdmin') NOT NULL default 'Participant',
  PRIMARY KEY  ("id"),
  UNIQUE KEY "id" ("id"),
  KEY "id_2" ("id")
) AUTO_INCREMENT=2 /*!40100 DEFAULT CHARSET=utf8*/;



#
# Dumping data for table 'user'
#

TRUNCATE TABLE "user";
LOCK TABLES "user" WRITE;
/*!40000 ALTER TABLE "user" DISABLE KEYS;*/
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('1','admin','pass','','0','SuperAdmin');
/*!40000 ALTER TABLE "user" ENABLE KEYS;*/
UNLOCK TABLES;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE;*/
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;*/
