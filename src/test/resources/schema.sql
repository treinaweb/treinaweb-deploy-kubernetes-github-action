CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('COMPANY','CANDIDATE') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`email`)
);

CREATE TABLE `skill` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
);

CREATE TABLE `job` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `level` enum('JUNIOR','MID_LEVEL','SENIOR') NOT NULL,
  `location` varchar(100) NOT NULL,
  `salary` decimal(38,2) NOT NULL,
  `title` varchar(100) NOT NULL,
  `type` enum('FULL_TIME','PART_TIME','FREELANCE','INTERSHIP','TEMPORARY') NOT NULL,
  `company_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`company_id`),
  CONSTRAINT FOREIGN KEY (`company_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `job_skills` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_id` bigint NOT NULL,
  `skills_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`skills_id`),
  KEY (`job_id`),
  CONSTRAINT FOREIGN KEY (`skills_id`) REFERENCES `skill` (`id`),
  CONSTRAINT FOREIGN KEY (`job_id`) REFERENCES `job` (`id`)
);

CREATE TABLE `job_candidates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_id` bigint NOT NULL,
  `candidates_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`candidates_id`),
  KEY (`job_id`),
  CONSTRAINT FOREIGN KEY (`candidates_id`) REFERENCES `user` (`id`),
  CONSTRAINT FOREIGN KEY (`job_id`) REFERENCES `job` (`id`)
);