package br.com.treinaweb.twjobs.api.jobs.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.treinaweb.twjobs.api.E2ETestCommon;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/skills/skills-insert.sql", "/sql/users/users-insert.sql", "/sql/jobs/jobs-insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/jobs/jobs-delete.sql", "/sql/skills/skills-delete.sql", "/sql/users/users-delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class JobSkillsRestControllerE2ETest extends E2ETestCommon {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void findSkillsByJobId_shouldReturnSkillsAndStatus200() {
        webTestClient.get()
            .uri("/api/jobs/1/skills")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$[0].id").isEqualTo(1)
            .jsonPath("$[0].name").isEqualTo("Java")
            .jsonPath("$[1].id").isEqualTo(2)
            .jsonPath("$[1].name").isEqualTo("Spring");
    }

    @Test
    void findSkillsByJobId_whenJobDoesNotExist_shouldReturnStatus404() {
        webTestClient.get()
            .uri("/api/jobs/100/skills")
            .exchange()
            .expectStatus().isNotFound();
    }
    
}
