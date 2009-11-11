
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `dces` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `dces`;

CREATE TABLE /*!32312 IF NOT EXISTS*/ `PREFIX_client_plugin` (
  `alias` varchar(48) NOT NULL COMMENT 'User friendly plugin name',
  `description` text NOT NULL,
  PRIMARY KEY  (`alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE /*!32312 IF NOT EXISTS*/ `PREFIX_contest` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `settings` blob NOT NULL COMMENT 'serialized settings',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE /*!32312 IF NOT EXISTS*/ `PREFIX_problem` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(64) NOT NULL COMMENT 'User friendly task name',
  `statement` blob NOT NULL COMMENT 'Serialized problem statement data, controlled by plugin',
  `answer` blob NOT NULL COMMENT 'Serialized problem answer data, controlled by plugin',
  `client_plugin_alias` varchar(48) NOT NULL,
  `server_plugin_alias` varchar(48) NOT NULL,
  `contest_id` int(10) unsigned NOT NULL,
  `contest_pos` int(10) unsigned NOT NULL,
  `column_names` blob NOT NULL COMMENT 'Serialized array with column names',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8 COMMENT='??????';


CREATE TABLE /*!32312 IF NOT EXISTS*/ `PREFIX_problem_status` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `problem_id` int(10) unsigned NOT NULL COMMENT 'Problem of the result',
  `user_id` int(10) unsigned NOT NULL COMMENT 'User of the result',
  `status` blob NOT NULL COMMENT 'serialized problem status',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `task_result_id` (`id`),
  KEY `task_result_id_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE /*!32312 IF NOT EXISTS*/ `PREFIX_server_plugin` (
  `alias` varchar(48) NOT NULL COMMENT 'User friendly plugin name',
  `description` text NOT NULL,
  PRIMARY KEY  (`alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE /*!32312 IF NOT EXISTS*/ `PREFIX_session` (
  `session_id` varchar(48) NOT NULL,
  `user_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`session_id`),
  UNIQUE KEY `user_id` (`user_id`,`session_id`),
  UNIQUE KEY `Session` (`session_id`),
  KEY `user_id_2` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE /*!32312 IF NOT EXISTS*/ `PREFIX_submission_history` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `problem_id` int(10) unsigned NOT NULL COMMENT 'Problem of the result',
  `user_id` int(10) unsigned NOT NULL COMMENT 'User of the result',
  `submission` blob NOT NULL COMMENT 'serialized submition data',
  `result` blob NOT NULL COMMENT 'serialized submition result',
  `submission_time` datetime NOT NULL COMMENT 'submition time',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `task_result_id` (`id`),
  KEY `task_result_id_2` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;


CREATE TABLE /*!32312 IF NOT EXISTS*/ `PREFIX_user` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `contest_id` int(10) unsigned NOT NULL COMMENT 'User''s contest',
  `login` varchar(24) NOT NULL,
  `password` varchar(24) NOT NULL,
  `user_type` enum('Participant','ContestAdmin','SuperAdmin') NOT NULL default 'Participant',
  `user_data` blob NOT NULL COMMENT 'Serialized array with user data',
  `results` blob COMMENT 'Serialized array with results',
  `contest_start` datetime default NULL COMMENT 'moment of the first login',
  `contest_finish` datetime default NULL COMMENT 'moment of contest fiinish',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;