CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('COMPANY','CANDIDATE') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`email`)
);