INSERT INTO `job` (`id`, `description`, `level`, `location`, `salary`, `title`, `type`, `company_id`) VALUES (1,'Vaga para um desenvolvedor Java e Spring','MID_LEVEL','São Paulo',7500.00,'Desenvolvedor Java e Spring','FULL_TIME',1);
INSERT INTO `job_skills` (`job_id`, `skills_id`) VALUES (1,1),(1,2);
INSERT INTO `job` (`id`, `description`, `level`, `location`, `salary`, `title`, `type`, `company_id`) VALUES (2,'Vaga para um desenvolvedor Python e Django','JUNIOR','Rio de Janeiro',4500.00,'Desenvolvedor Python e Django','FULL_TIME',1);
INSERT INTO `job_skills` (`job_id`, `skills_id`) VALUES (2,9),(2,10);
INSERT INTO `job` (`id`, `description`, `level`, `location`, `salary`, `title`, `type`, `company_id`) VALUES (3,'Vaga para um desenvolvedor Java e Spring','SENIOR','São Paulo',9500.00,'Desenvolvedor Java e Spring','FULL_TIME',1);
INSERT INTO `job_skills` (`job_id`, `skills_id`) VALUES (3,1),(3,2);