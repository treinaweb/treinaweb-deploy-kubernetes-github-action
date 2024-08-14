package br.com.treinaweb.twjobs.api.skills.controllers;

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
import br.com.treinaweb.twjobs.api.skills.dtos.SkillRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/skills/skills-insert.sql", "/sql/users/users-insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/skills/skills-delete.sql", "/sql/users/users-delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SkillRestControllerE2ETest extends E2ETestCommon {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void findAll_shouldReturnSkillsWithPaginationAndStatus200() {
        webTestClient.get()
            .uri("/api/skills?sort=id,asc")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$._embedded.skillResponseList[0].id").isEqualTo(1)
            .jsonPath("$._embedded.skillResponseList[0].name").isEqualTo("Java")
            .jsonPath("$._embedded.skillResponseList[1].id").isEqualTo(2)
            .jsonPath("$._embedded.skillResponseList[1].name").isEqualTo("Spring")
            .jsonPath("$.page.size").isEqualTo(2)
            .jsonPath("$.page.totalElements").isEqualTo(10)
            .jsonPath("$.page.totalPages").isEqualTo(5)
            .jsonPath("$.page.number").isEqualTo(1);
    }

    @Test
    void findById_shouldReturnSkillAndStatus200() {
        webTestClient.get()
            .uri("/api/skills/1")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.name").isEqualTo("Java");
    }

    @Test
    void findById_shouldReturnStatus404_whenSkillNotFound() {
        webTestClient.get()
            .uri("/api/skills/100")
            .exchange()
            .expectStatus().isNotFound()
            .expectBody()
            .jsonPath("$.message").isEqualTo("Skill not found")
            .jsonPath("$.timestamp").isNotEmpty();
    }

    @Test
    void create_withValidSkillRequest_shouldReturnSkillAndStatus201() {
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

        var body = SkillRequest.builder()
            .name("React")
            .build();

        webTestClient.post()
            .uri("/api/skills")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.id").isNumber()
            .jsonPath("$.name").isEqualTo(body.getName());
    }

    @Test
    void create_withInvalidSkillRequest_shouldReturnStatus400() {
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

        var body = SkillRequest.builder()
            .name(null)
            .build();

        webTestClient.post()
            .uri("/api/skills")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isEqualTo("Validation error")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.errors").isNotEmpty()
            .jsonPath("$.errors.name").isNotEmpty();
    }

    @Test
    void create_withUnauthenticatedUser_shouldReturnStatus401() {
        var body = SkillRequest.builder()
            .name("React")
            .build();

        webTestClient.post()
            .uri("/api/skills")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void create_withUnauthorizedUser_shouldReturnStatus403() {
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

        var body = SkillRequest.builder()
            .name("React")
            .build();

        webTestClient.post()
            .uri("/api/skills")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    void update_withValidSkillRequest_shouldReturnSkillAndStatus200() {
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

        var body = SkillRequest.builder()
            .name("React")
            .build();

        webTestClient.put()
            .uri("/api/skills/1")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.name").isEqualTo(body.getName());
    }

    @Test
    void update_withInvalidSkillRequest_shouldReturnStatus400() {
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

        var body = SkillRequest.builder()
            .name("")
            .build();

        webTestClient.put()
            .uri("/api/skills/1")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isEqualTo("Validation error")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.errors").isNotEmpty()
            .jsonPath("$.errors.name").isNotEmpty();
    }

    @Test
    void update_withUnauthenticatedUser_shouldReturnStatus401() {
        var body = SkillRequest.builder()
            .name("React")
            .build();

        webTestClient.put()
            .uri("/api/skills/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void update_withUnauthorizedUser_shouldReturnStatus403() {
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

        var body = SkillRequest.builder()
            .name("React")
            .build();

        webTestClient.put()
            .uri("/api/skills/1")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    void delete_withValidId_shouldReturnStatus204() {
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

        webTestClient.delete()
            .uri("/api/skills/1")
            .header("Authorization", "Bearer " + accessToken)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    void delete_withUnauthenticatedUser_shouldReturnStatus401() {
        webTestClient.delete()
            .uri("/api/skills/1")
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void delete_withUnauthorizedUser_shouldReturnStatus403() {
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

        webTestClient.delete()
            .uri("/api/skills/1")
            .header("Authorization", "Bearer " + accessToken)
            .exchange()
            .expectStatus().isForbidden();
    }
}
