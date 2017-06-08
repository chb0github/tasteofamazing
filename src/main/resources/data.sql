
INSERT INTO users (username, password, enabled) VALUES ('root', '$2a$13$qmJchqN/zb2y92AVTszXVubm90VD/c70FDUVhYJ/MqwV6wsZntJhG', TRUE);
INSERT INTO users (username, password, enabled) VALUES ('gail', '$2a$13$f7Ae/ObDGSyBO7vJ3EtR3eV8vKDd53MotXvkEIlm2kUed4FyD05wi', TRUE);
INSERT INTO users (username, password, enabled) VALUES ('christian', '$2a$13$clw85Yj43S0.PatAoW/Xp.opCUS9xDP0DdCK5AYQPDsoEPDEjW9pW', TRUE);

INSERT INTO Roles (name) VALUES ('ROLE_ROOT'),('ROLE_ADMIN'),('ROLE_USER'),('ROLE_VISITOR');

INSERT INTO authorities (username, role) VALUE ('root', 'ROLE_ROOT');
INSERT INTO authorities (username, role) VALUE ('christian', 'ROLE_ADMIN');
INSERT INTO authorities (username, role) VALUE ('gail', 'ROLE_ADMIN');

INSERT INTO `acl_sid` (`id`, `principal`, `sid`) VALUES
  (1, 1, 'christian'),
  (2, 1, 'pj'),
  (3, 1, 'mike');

--
-- Dumping data for table `acl_class`
--

INSERT INTO `acl_class` (`id`, `class`) VALUES
  (1, 'org.bongiorno.sdrss.domain.resources.Candidate'),
  (2, 'org.bongiorno.sdrss.domain.resources.Form'),
  (3, 'org.bongiorno.sdrss.domain.resources.Report');

--
-- Dumping data for table `acl_object_identity`
--

INSERT INTO `acl_object_identity` (`id`, `object_id_class`, `object_id_identity`, `parent_object`, `owner_sid`, `entries_inheriting`) VALUES
  (1, 1, 1, NULL, 1, 0),
  (2, 1, 2, NULL, 1, 0),
  (3, 1, 3, NULL, 1, 0),
  (4, 2, 1, NULL, 1, 0),
  (5, 2, 2, NULL, 1, 0),
  (6, 2, 3, NULL, 1, 0),
  (7, 3, 1, NULL, 1, 0),
  (8, 3, 2, NULL, 1, 0),
  (9, 3, 3, NULL, 1, 0);

--
-- Dumping data for table `acl_entry`
--

INSERT INTO `acl_entry` (`id`, `acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`, `audit_success`, `audit_failure`) VALUES
  (1, 1, 1, 1, 1, 1, 1, 1),
  (2, 2, 1, 1, 1, 1, 1, 1),
  (3, 3, 1, 1, 1, 1, 1, 1),
  (4, 1, 2, 1, 2, 1, 1, 1),
  (5, 2, 2, 1, 2, 1, 1, 1),
  (6, 3, 2, 1, 2, 1, 1, 1),
  (7, 4, 1, 1, 1, 1, 1, 1),
  (8, 5, 1, 1, 1, 1, 1, 1),
  (9, 6, 1, 1, 1, 1, 1, 1),
  (10, 7, 1, 1, 1, 1, 1, 1),
  (11, 8, 1, 1, 1, 1, 1, 1),
  (12, 9, 1, 1, 1, 1, 1, 1),
  (13, 7, 2, 1, 2, 1, 1, 1),
  (14, 8, 2, 1, 2, 1, 1, 1),
  (15, 9, 2, 1, 2, 1, 1, 1),
  (28, 4, 3, 2, 1, 1, 1, 1),
  (29, 5, 3, 2, 1, 1, 1, 1),
  (30, 6, 3, 2, 1, 1, 1, 1),
  (31, 4, 4, 2, 2, 1, 1, 1),
  (32, 5, 4, 2, 2, 1, 1, 1),
  (33, 6, 4, 2, 2, 1, 1, 1),
  (34, 7, 3, 2, 1, 1, 1, 1),
  (35, 8, 3, 2, 1, 1, 1, 1),
  (36, 9, 3, 2, 1, 1, 1, 1),
  (37, 7, 4, 2, 2, 1, 1, 1),
  (38, 8, 4, 2, 2, 1, 1, 1),
  (39, 9, 4, 2, 2, 1, 1, 1),
  (40, 7, 5, 3, 1, 1, 1, 1),
  (41, 8, 5, 3, 1, 1, 1, 1);


--
-- Table structure for table `admin_post`
--

CREATE TABLE IF NOT EXISTS candidate (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `admin_post`
--

INSERT INTO candidate (`id`, `date`, `name`) VALUES
  (1, '2011-01-03 21:37:58', 'Custom post #1 from Candidate'),
  (2, '2011-01-04 21:38:39', 'Custom post #2 from Candidate'),
  (3, '2011-01-05 21:39:37', 'Custom post #3 from Candidate');

-- --------------------------------------------------------

--
-- Table structure for table `personal_post`
--

CREATE TABLE IF NOT EXISTS `form` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `personal_post`
--

INSERT INTO `form` (`id`, `date`, `name`) VALUES
  (1, '2011-01-06 21:40:02', 'Custom post #1 from Form'),
  (2, '2011-01-07 21:40:13', 'Custom post #2 from Form'),
  (3, '2011-01-08 21:40:34', 'Custom post #3 from Form');

-- --------------------------------------------------------

--
-- Table structure for table `public_post`
--

CREATE TABLE IF NOT EXISTS `report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `public_post`
--

INSERT INTO `report` (`id`, `date`, `name`) VALUES
  (1, '2011-01-10 21:40:44', 'Custom post #1 from Report'),
  (2, '2011-01-11 21:40:48', 'Custom post #2 from Report'),
  (3, '2011-01-12 21:41:08', 'Custom post #3 from Report');
