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