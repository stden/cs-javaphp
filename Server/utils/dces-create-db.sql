# --------------------------------------------------------
# Host:                         127.0.0.1
# Database:                     dces
# Server version:               5.1.49-1ubuntu8
# Server OS:                    debian-linux-gnu
# HeidiSQL version:             5.0.0.3272
# Date/time:                    2010-10-25 17:42:06
# --------------------------------------------------------

# Dumping structure for table dces.PREFIX_client_plugin
CREATE TABLE IF NOT EXISTS `PREFIX_client_plugin` (
  `alias` varchar(48) NOT NULL COMMENT 'User friendly plugin name',
  `description` text NOT NULL,
  PRIMARY KEY (`alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table dces.PREFIX_contest
CREATE TABLE IF NOT EXISTS `PREFIX_contest` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `settings` blob NOT NULL COMMENT 'serialized settings',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table dces.PREFIX_problem
CREATE TABLE IF NOT EXISTS `PREFIX_problem` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contest_settings` blob NOT NULL COMMENT 'serialized array of contest specific settings',
  `contest_id` int(10) unsigned NOT NULL,
  `contest_pos` int(10) unsigned NOT NULL,
  `checker_columns` blob NOT NULL COMMENT 'serialized array with plugin checker columns',
  `result_columns` blob NOT NULL COMMENT 'serialized array with result table columns',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Problems';

# Data exporting was unselected.


# Dumping structure for table dces.PREFIX_problem_status
CREATE TABLE IF NOT EXISTS `PREFIX_problem_status` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `problem_id` int(10) unsigned NOT NULL COMMENT 'Problem of the result',
  `user_id` int(10) unsigned NOT NULL COMMENT 'User of the result',
  `status` blob NOT NULL COMMENT 'serialized problem status',
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_result_id` (`id`),
  KEY `task_result_id_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table dces.PREFIX_server_plugin
CREATE TABLE IF NOT EXISTS `PREFIX_server_plugin` (
  `alias` varchar(48) NOT NULL COMMENT 'User friendly plugin name',
  `description` text NOT NULL,
  PRIMARY KEY (`alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table dces.PREFIX_session
CREATE TABLE IF NOT EXISTS `PREFIX_session` (
  `session_id` varchar(48) NOT NULL,
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`session_id`),
  UNIQUE KEY `user_id` (`user_id`,`session_id`),
  UNIQUE KEY `Session` (`session_id`),
  KEY `user_id_2` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table dces.PREFIX_submission_history
CREATE TABLE IF NOT EXISTS `PREFIX_submission_history` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `problem_id` int(10) unsigned NOT NULL COMMENT 'Problem of the result',
  `user_id` int(10) unsigned NOT NULL COMMENT 'User of the result',
  `submission` blob NOT NULL COMMENT 'serialized submition data',
  `result` blob NOT NULL COMMENT 'serialized submition result',
  `submission_time` datetime NOT NULL COMMENT 'submition time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_result_id` (`id`),
  KEY `task_result_id_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table dces.PREFIX_user
CREATE TABLE IF NOT EXISTS `PREFIX_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `contest_id` int(10) unsigned NOT NULL COMMENT 'User''s contest',
  `login` varchar(24) NOT NULL,
  `password` varchar(24) NOT NULL,
  `user_type` enum('Participant','ContestAdmin','SuperAdmin') NOT NULL DEFAULT 'Participant',
  `user_data` blob NOT NULL COMMENT 'Serialized array with user data',
  `results` blob NOT NULL COMMENT 'Serialized array with results',
  `contest_start` datetime DEFAULT NULL COMMENT 'moment of the first login',
  `contest_finish` datetime DEFAULT NULL COMMENT 'moment of contest fiinish',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
