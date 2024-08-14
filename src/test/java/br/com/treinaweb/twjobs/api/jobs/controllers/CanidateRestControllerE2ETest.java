package br.com.treinaweb.twjobs.api.jobs.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.treinaweb.twjobs.api.E2ETestCommon;
import br.com.treinaweb.twjobs.api.auth.dtos.LoginRequest;
import br.com.treinaweb.twjobs.api.auth.dtos.TokenResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/skills/skills-insert.sql", "/sql/users/users-insert.sql", "/sql/jobs/jobs-insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/jobs/jobs-delete.sql", "/sql/skills/skills-delete.sql", "/sql/users/users-delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CanidateRestControllerE2ETest extends E2ETestCommon {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void apply_shouldReturnStatus204() {
        var tokenBody = LoginRequest.builder()
            .email("joao@mail.com")
            .password("senha@123")
            .build();
        
        var tokenResponse = webTestClient.post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(tokenBody)
            .exchange()
            .expectStatus().isOk()
            .expectBody(TokenResponse.class)
            .returnResult()
            .getResponseBody();
        var accessToken = tokenResponse.getAccessToken();

        webTestClient.post()
            .uri("/api/jobs/1/apply")
            .header("Authorization", "Bearer " + accessToken)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    void apply_whenJobDoesNotExist_shouldReturnStatus404() {
        var tokenBody = LoginRequest.builder()
            .email("joao@mail.com")
            .password("senha@123")
            .build();
        
        var tokenResponse = webTestClient.post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(tokenBody)
            .exchange()
            .expectStatus().isOk()
            .expectBody(TokenResponse.class)
            .returnResult()
            .getResponseBody();
        var accessToken = tokenResponse.getAccessToken();

        webTestClient.post()
            .uri("/api/jobs/100/apply")
            .header("Authorization", "Bearer " + accessToken)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void apply_whenUserIsNotAuthenticated_shouldReturnStatus401() {
        webTestClient.post()
            .uri("/api/jobs/1/apply")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void apply_whenUserIsNotAuthorized_shouldReturnStatus403() {
        var tokenBody = LoginRequest.builder()
            .email("contato@avmakers.com.br")
            .password("senha@123")
            .build();
        
        var tokenResponse = webTestClient.post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(tokenBody)
            .exchange()
            .expectStatus().isOk()
            .expectBody(TokenResponse.class)
            .returnResult()
            .getResponseBody();
        var accessToken = tokenResponse.getAccessToken();

        webTestClient.post()
            .uri("/api/jobs/1/apply")
            .header("Authorization", "Bearer " + accessToken)
            .exchange()
            .expectStatus().isForbidden();
    }
    
}
