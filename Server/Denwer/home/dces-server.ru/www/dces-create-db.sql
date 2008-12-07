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

CREATE DATABASE /*!32312 IF NOT EXISTS*/ "dces" /*!40100 DEFAULT CHARACTER SET utf8 */;

USE "dces";


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
LOCK TABLES "client_plugin" WRITE;
/*!40000 ALTER TABLE "client_plugin" DISABLE KEYS;*/
INSERT INTO "client_plugin" ("alias", "description") VALUES
	('cp1','');
INSERT INTO "client_plugin" ("alias", "description") VALUES
	('cp2','');
INSERT INTO "client_plugin" ("alias", "description") VALUES
	('SamplePlugin','');
/*!40000 ALTER TABLE "client_plugin" ENABLE KEYS;*/
UNLOCK TABLES;


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
  "user_data" blob NOT NULL COMMENT 'Serialized array with user data',
  "user_data_compulsory" blob NOT NULL COMMENT 'Serialized array with compulsory booleans',
  PRIMARY KEY  ("id"),
  UNIQUE KEY "id" ("id"),
  KEY "id_2" ("id")
) AUTO_INCREMENT=82 /*!40100 DEFAULT CHARSET=utf8*/;



#
# Dumping data for table 'contest'
#

TRUNCATE TABLE "contest";
LOCK TABLES "contest" WRITE;
/*!40000 ALTER TABLE "contest" DISABLE KEYS;*/
INSERT INTO "contest" ("id", "name", "start_time", "finish_time", "description", "reg_type", "user_data", "user_data_compulsory") VALUES
	('3','Тестовый контест','2008-12-07 23:26:58','2008-12-23 00:00:00','Описание контеста, здесь написано очень много текста','Self','a:2:{i:0;s:4:"Name";i:1;s:6:"School";}','a:2:{i:0;b:1;i:1;b:0;}');
INSERT INTO "contest" ("id", "name", "start_time", "finish_time", "description", "reg_type", "user_data", "user_data_compulsory") VALUES
	('4','New Contest','2008-11-24 03:25:58','2008-12-23 00:00:00','The first contest created by a request','ByAdmins','a:2:{i:0;s:4:"Name";i:1;s:6:"School";}','a:2:{i:0;b:1;i:1;b:0;}');
INSERT INTO "contest" ("id", "name", "start_time", "finish_time", "description", "reg_type", "user_data", "user_data_compulsory") VALUES
	('5','Новый контест','2008-11-24 03:26:44','2008-12-23 00:00:00','Первый контест, созданный на русском через запрос','ByAdmins','a:2:{i:0;s:4:"Name";i:1;s:10:"РЁРєРѕР»Р°";}','a:2:{i:0;b:1;i:1;b:0;}');
INSERT INTO "contest" ("id", "name", "start_time", "finish_time", "description", "reg_type", "user_data", "user_data_compulsory") VALUES
	('6','Новый контест','2008-11-24 03:26:57','2008-12-23 00:00:00','Первый контест, созданный на русском через запрос','ByAdmins','a:2:{i:0;s:4:"Name";i:1;s:10:"РЁРєРѕР»Р°";}','a:2:{i:0;b:1;i:1;b:0;}');
INSERT INTO "contest" ("id", "name", "start_time", "finish_time", "description", "reg_type", "user_data", "user_data_compulsory") VALUES
	('80','Новый контест','2008-12-07 23:13:10','2008-12-23 00:00:00','Первый контест, созданный на русском через запрос','ByAdmins','a:2:{i:0;s:4:"Name";i:1;s:10:"Школа";}','a:2:{i:0;b:1;i:1;b:0;}');
INSERT INTO "contest" ("id", "name", "start_time", "finish_time", "description", "reg_type", "user_data", "user_data_compulsory") VALUES
	('79','Новый контест','2008-12-07 23:11:56','2008-12-23 00:00:00','Первый контест, созданный на русском через запрос','ByAdmins','a:2:{i:0;s:4:"Name";i:1;s:10:"Школа";}','a:2:{i:0;b:1;i:1;b:0;}');
INSERT INTO "contest" ("id", "name", "start_time", "finish_time", "description", "reg_type", "user_data", "user_data_compulsory") VALUES
	('81','Новый контест','2008-12-07 23:26:58','2008-12-23 00:00:00','Первый контест, созданный на русском через запрос','ByAdmins','a:2:{i:0;s:4:"Name";i:1;s:10:"Школа";}','a:2:{i:0;b:1;i:1;b:0;}');
INSERT INTO "contest" ("id", "name", "start_time", "finish_time", "description", "reg_type", "user_data", "user_data_compulsory") VALUES
	('75','Новый контест','2008-12-06 15:01:50','2008-12-23 00:00:00','Первый контест, созданный на русском через запрос','ByAdmins','a:2:{i:0;s:4:"Name";i:1;s:10:"Школа";}','a:2:{i:0;b:1;i:1;b:0;}');
INSERT INTO "contest" ("id", "name", "start_time", "finish_time", "description", "reg_type", "user_data", "user_data_compulsory") VALUES
	('74','Новый контест','2008-12-06 14:16:34','2008-12-23 00:00:00','Первый контест, созданный на русском через запрос','ByAdmins','a:2:{i:0;s:4:"Name";i:1;s:10:"Школа";}','a:2:{i:0;b:1;i:1;b:0;}');
/*!40000 ALTER TABLE "contest" ENABLE KEYS;*/
UNLOCK TABLES;


#
# Table structure for table 'problem'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "problem" (
  "id" int(10) unsigned NOT NULL auto_increment,
  "name" varchar(64) NOT NULL COMMENT 'User friendly task name',
  "statement" blob NOT NULL COMMENT 'Serialized problem statement data, controlled by plugin',
  "answer" blob NOT NULL COMMENT 'Serialized problem answer data, controlled by plugin',
  "client_plugin_alias" varchar(48) NOT NULL,
  "server_plugin_alias" varchar(48) NOT NULL,
  "contest_id" int(10) unsigned NOT NULL,
  "contest_pos" int(10) unsigned NOT NULL,
  PRIMARY KEY  ("id"),
  UNIQUE KEY "id" ("id"),
  KEY "id_2" ("id")
) AUTO_INCREMENT=153 /*!40100 DEFAULT CHARSET=utf8 COMMENT='������'*/;



#
# Dumping data for table 'problem'
#

TRUNCATE TABLE "problem";
LOCK TABLES "problem" WRITE;
/*!40000 ALTER TABLE "problem" DISABLE KEYS;*/
INSERT INTO "problem" ("id", "name", "statement", "answer", "client_plugin_alias", "server_plugin_alias", "contest_id", "contest_pos") VALUES
	('152','Задача ','s:0:"";','s:3:"239";','SamplePlugin','ComparePlugin','3','2');
INSERT INTO "problem" ("id", "name", "statement", "answer", "client_plugin_alias", "server_plugin_alias", "contest_id", "contest_pos") VALUES
	('151','Задача 2 * 3 * 9','s:0:"";','s:2:"42";','SamplePlugin','ComparePlugin','3','1');
/*!40000 ALTER TABLE "problem" ENABLE KEYS;*/
UNLOCK TABLES;


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
LOCK TABLES "server_plugin" WRITE;
/*!40000 ALTER TABLE "server_plugin" DISABLE KEYS;*/
INSERT INTO "server_plugin" ("alias", "description") VALUES
	('ComparePlugin','');
/*!40000 ALTER TABLE "server_plugin" ENABLE KEYS;*/
UNLOCK TABLES;


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
LOCK TABLES "session" WRITE;
/*!40000 ALTER TABLE "session" DISABLE KEYS;*/
INSERT INTO "session" ("session_id", "user_id") VALUES
	('TtOlBLcFNhsE1Wr4WumXuSN4','1');
INSERT INTO "session" ("session_id", "user_id") VALUES
	('DJaxLUtgVrGif2QOH4cYKYXY','21');
INSERT INTO "session" ("session_id", "user_id") VALUES
	('DGN6MP3oZIE7AO601h9fQdVt','23');
INSERT INTO "session" ("session_id", "user_id") VALUES
	('%','42');
INSERT INTO "session" ("session_id", "user_id") VALUES
	('''','42');
INSERT INTO "session" ("session_id", "user_id") VALUES
	('_','42');
INSERT INTO "session" ("session_id", "user_id") VALUES
	('_g29qiS252wxC2Y06itK3Z4i','239');
/*!40000 ALTER TABLE "session" ENABLE KEYS;*/
UNLOCK TABLES;


#
# Table structure for table 'task_result'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "task_result" (
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
# Dumping data for table 'task_result'
#

TRUNCATE TABLE "task_result";
LOCK TABLES "task_result" WRITE;
/*!40000 ALTER TABLE "task_result" DISABLE KEYS;*/
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('1','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:2:"no";}','2008-12-04 22:49:42');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('2','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:2:"no";}','2008-12-04 22:51:19');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('3','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:2:"no";}','2008-12-04 23:03:15');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('4','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:2:"no";}','2008-12-04 23:45:25');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('5','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:2:"no";}','2008-12-05 00:34:46');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('6','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:2:"no";}','2008-12-05 01:00:38');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('7','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:2:"no";}','2008-12-05 01:05:30');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('8','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-05 01:08:54');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('9','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 13:28:04');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('10','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 13:29:26');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('11','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 13:43:39');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('12','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 13:44:22');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('13','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 13:45:53');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('14','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 13:48:51');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('15','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 13:49:22');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('16','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 13:52:57');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('17','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 13:53:36');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('18','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 14:05:54');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('19','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 14:06:07');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('20','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 14:06:13');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('21','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 14:09:13');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('22','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 14:09:20');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('23','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 14:11:44');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('24','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 14:12:51');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('25','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 14:15:45');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('26','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 14:16:08');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('27','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 14:16:34');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('28','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 15:01:50');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('29','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 15:03:02');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('30','151','1','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-06 15:04:00');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('31','151','21','a:1:{s:6:"answer";s:2:"42";}','a:1:{s:6:"result";s:2:"no";}','2008-12-07 12:17:10');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('32','151','21','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-07 12:17:57');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('33','152','21','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-07 12:18:21');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('34','152','21','a:1:{s:6:"answer";s:2:"df";}','a:1:{s:6:"result";s:2:"no";}','2008-12-07 12:22:02');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('35','152','21','a:1:{s:6:"answer";s:17:"this is an answer";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-07 12:22:10');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('36','151','21','a:1:{s:6:"answer";s:2:"42";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-07 23:40:32');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('37','151','21','a:1:{s:6:"answer";s:3:"421";}','a:1:{s:6:"result";s:2:"no";}','2008-12-07 23:40:36');
INSERT INTO "task_result" ("id", "problem_id", "user_id", "submission", "result", "submission_time") VALUES
	('38','152','21','a:1:{s:6:"answer";s:3:"239";}','a:1:{s:6:"result";s:3:"yes";}','2008-12-07 23:40:56');
/*!40000 ALTER TABLE "task_result" ENABLE KEYS;*/
UNLOCK TABLES;


#
# Table structure for table 'user'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "user" (
  "id" int(10) unsigned NOT NULL auto_increment,
  "login" varchar(24) NOT NULL,
  "password" varchar(24) NOT NULL,
  "user_data" blob NOT NULL COMMENT 'Serialized array with user data',
  "contest_id" int(10) unsigned NOT NULL COMMENT 'User''s contest',
  "user_type" enum('Participant','ContestAdmin','SuperAdmin') NOT NULL default 'Participant',
  PRIMARY KEY  ("id"),
  UNIQUE KEY "id" ("id"),
  KEY "id_2" ("id")
) AUTO_INCREMENT=26 /*!40100 DEFAULT CHARSET=utf8*/;



#
# Dumping data for table 'user'
#

TRUNCATE TABLE "user";
LOCK TABLES "user" WRITE;
/*!40000 ALTER TABLE "user" DISABLE KEYS;*/
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('1','admin','pass','N;','0','SuperAdmin');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('10','newLogin','newPassword','a:2:{i:0;s:8:"Вася";i:1;s:6:"№239";}','3','Participant');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('25','','','a:2:{i:0;s:3:"234";i:1;s:6:"234234";}','3','Participant');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('12','newLogin','newPassword','a:2:{i:0;s:8:"Вася";i:1;s:6:"№239";}','3','Participant');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('19','lmjrmlcqqa','newPassword','a:2:{i:0;s:8:"Вася";i:1;s:6:"№239";}','3','Participant');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('20','dnkpgcknfd','newPassword','a:2:{i:0;s:8:"Вася";i:1;s:6:"№239";}','3','ContestAdmin');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('21','user','password','a:2:{i:0;s:8:"Вася";i:1;s:6:"№239";}','3','Participant');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('17','osfikhcmpn','newPassword','a:2:{i:0;s:8:"Вася";i:1;s:6:"№239";}','3','Participant');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('18','lfalanpjsj','newPassword','a:2:{i:0;s:8:"Вася";i:1;s:6:"№239";}','3','ContestAdmin');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('22','likleitfkg','newPassword','a:2:{i:0;s:8:"Вася";i:1;s:6:"№239";}','3','ContestAdmin');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('23','kollfgciqn','newPassword','a:2:{i:0;s:8:"Вася";i:1;s:6:"№239";}','3','Participant');
INSERT INTO "user" ("id", "login", "password", "user_data", "contest_id", "user_type") VALUES
	('24','pefhnoodhc','newPassword','a:2:{i:0;s:8:"Вася";i:1;s:6:"№239";}','3','ContestAdmin');
/*!40000 ALTER TABLE "user" ENABLE KEYS;*/
UNLOCK TABLES;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE;*/
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;*/
