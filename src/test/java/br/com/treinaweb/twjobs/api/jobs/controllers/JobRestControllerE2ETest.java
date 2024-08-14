package br.com.treinaweb.twjobs.api.jobs.controllers;

import java.math.BigDecimal;
import java.util.List;

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
import br.com.treinaweb.twjobs.api.jobs.dtos.JobRequest;
import br.com.treinaweb.twjobs.core.enums.JobLevel;
import br.com.treinaweb.twjobs.core.enums.JobType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/sql/skills/skills-insert.sql", "/sql/users/users-insert.sql", "/sql/jobs/jobs-insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/jobs/jobs-delete.sql", "/sql/skills/skills-delete.sql", "/sql/users/users-delete.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class JobRestControllerE2ETest extends E2ETestCommon {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void findAll_shouldReturnJobsWithPaginationAndStatus200() {
        webTestClient.get()
            .uri("/api/jobs?sort=id,asc")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$._embedded.jobResponseList[0].id").isEqualTo(1)
            .jsonPath("$._embedded.jobResponseList[0].title").isEqualTo("Desenvolvedor Java e Spring")
            .jsonPath("$._embedded.jobResponseList[0].description").isEqualTo("Vaga para um desenvolvedor Java e Spring")
            .jsonPath("$._embedded.jobResponseList[0].company").isEqualTo("AVMakers")
            .jsonPath("$._embedded.jobResponseList[0].location").isEqualTo("São Paulo")
            .jsonPath("$._embedded.jobResponseList[0].type").isEqualTo("FULL_TIME")
            .jsonPath("$._embedded.jobResponseList[0].level").isEqualTo("MID_LEVEL")
            .jsonPath("$._embedded.jobResponseList[0].salary").isEqualTo(7500.00)
            .jsonPath("$.page.size").isEqualTo(2)
            .jsonPath("$.page.totalElements").isEqualTo(3)
            .jsonPath("$.page.totalPages").isEqualTo(2)
            .jsonPath("$.page.number").isEqualTo(1);
    }

    @Test
    void findById_shouldReturnJobAndStatus200() {
        webTestClient.get()
            .uri("/api/jobs/1")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.title").isEqualTo("Desenvolvedor Java e Spring")
            .jsonPath("$.description").isEqualTo("Vaga para um desenvolvedor Java e Spring")
            .jsonPath("$.company").isEqualTo("AVMakers")
            .jsonPath("$.location").isEqualTo("São Paulo")
            .jsonPath("$.type").isEqualTo("FULL_TIME")
            .jsonPath("$.level").isEqualTo("MID_LEVEL")
            .jsonPath("$.salary").isEqualTo(7500.00);
    }

    @Test
    void findById_shouldReturnStatus404_whenJobNotFound() {
        webTestClient.get()
            .uri("/api/jobs/10")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void create_withUnauthenticatedUser_shouldReturnStatus401() {
        var body = JobRequest.builder()
            .title("Desenvolvedor Java e Spring")
            .description("Vaga para um desenvolvedor Java e Spring")
            .location("São Paulo")
            .type(JobType.FULL_TIME)
            .level(JobLevel.MID_LEVEL)
            .salary(BigDecimal.valueOf(7500.00))
            .skills(List.of(1L, 2L))
            .build();

        webTestClient.post()
            .uri("/api/jobs")
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

        var body = JobRequest.builder()
            .title("Desenvolvedor Java e Spring")
            .description("Vaga para um desenvolvedor Java e Spring")
            .location("São Paulo")
            .type(JobType.FULL_TIME)
            .level(JobLevel.MID_LEVEL)
            .salary(BigDecimal.valueOf(7500.00))
            .skills(List.of(1L, 2L))
            .build();

        webTestClient.post()
            .uri("/api/jobs")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    void create_withAuthenticatedUser_shouldReturnJobAndStatus201() {
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

        var body = JobRequest.builder()
            .title("Desenvolvedor Java e Spring")
            .description("Vaga para um desenvolvedor Java e Spring")
            .location("São Paulo")
            .type(JobType.FULL_TIME)
            .level(JobLevel.MID_LEVEL)
            .salary(BigDecimal.valueOf(7500.00))
            .skills(List.of(1L, 2L))
            .build();
        
        webTestClient.post()
            .uri("/api/jobs")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.id").isNumber()
            .jsonPath("$.title").isEqualTo("Desenvolvedor Java e Spring")
            .jsonPath("$.description").isEqualTo("Vaga para um desenvolvedor Java e Spring")
            .jsonPath("$.company").isEqualTo("AVMakers")
            .jsonPath("$.location").isEqualTo("São Paulo")
            .jsonPath("$.type").isEqualTo("FULL_TIME")
            .jsonPath("$.level").isEqualTo("MID_LEVEL")
            .jsonPath("$.salary").isEqualTo(7500.00);
    }

    @Test
    void create_withInvalidData_shouldReturnStatus400() {
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

        var body = JobRequest.builder()
            .title("")
            .description("")
            .location("")
            .type(JobType.FULL_TIME)
            .level(JobLevel.MID_LEVEL)
            .salary(BigDecimal.valueOf(-7500.00))
            .skills(List.of())
            .build();

        webTestClient.post()
            .uri("/api/jobs")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isEqualTo("Validation error")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.errors").isNotEmpty()
            .jsonPath("$.errors.title").isNotEmpty()
            .jsonPath("$.errors.description").isNotEmpty()
            .jsonPath("$.errors.location").isNotEmpty()
            .jsonPath("$.errors.salary").isNotEmpty()
            .jsonPath("$.errors.skills").isNotEmpty();
    }

    @Test
    void update_withUnauthenticatedUser_shouldReturnStatus401() {
        var body = JobRequest.builder()
            .title("Desenvolvedor Java e Spring")
            .description("Vaga para um desenvolvedor Java e Spring")
            .location("São Paulo")
            .type(JobType.FULL_TIME)
            .level(JobLevel.MID_LEVEL)
            .salary(BigDecimal.valueOf(7500.00))
            .skills(List.of(1L, 2L))
            .build();

        webTestClient.put()
            .uri("/api/jobs/1")
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

        var body = JobRequest.builder()
            .title("Desenvolvedor Java e Spring")
            .description("Vaga para um desenvolvedor Java e Spring")
            .location("São Paulo")
            .type(JobType.FULL_TIME)
            .level(JobLevel.MID_LEVEL)
            .salary(BigDecimal.valueOf(7500.00))
            .skills(List.of(1L, 2L))
            .build();

        webTestClient.put()
            .uri("/api/jobs/1")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    void update_withNotOwnerUser_shouldReturnStatus403() {
        var tokenBody = LoginRequest.builder()
            .email("contato@treinaweb.com.br")
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

        var body = JobRequest.builder()
            .title("Desenvolvedor Java e Spring")
            .description("Vaga para um desenvolvedor Java e Spring")
            .location("São Paulo")
            .type(JobType.FULL_TIME)
            .level(JobLevel.MID_LEVEL)
            .salary(BigDecimal.valueOf(7500.00))
            .skills(List.of(1L, 2L))
            .build();

        webTestClient.put()
            .uri("/api/jobs/1")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    void update_withOwnerUser_shouldReturnJobAndStatus200() {
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

        var body = JobRequest.builder()
            .title("Desenvolvedor Java e Spring")
            .description("Vaga para um desenvolvedor Java e Spring")
            .location("São Paulo")
            .type(JobType.FULL_TIME)
            .level(JobLevel.MID_LEVEL)
            .salary(BigDecimal.valueOf(7500.00))
            .skills(List.of(1L, 2L))
            .build();

        webTestClient.put()
            .uri("/api/jobs/1")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.title").isEqualTo("Desenvolvedor Java e Spring")
            .jsonPath("$.description").isEqualTo("Vaga para um desenvolvedor Java e Spring")
            .jsonPath("$.company").isEqualTo("AVMakers")
            .jsonPath("$.location").isEqualTo("São Paulo")
            .jsonPath("$.type").isEqualTo("FULL_TIME")
            .jsonPath("$.level").isEqualTo("MID_LEVEL")
            .jsonPath("$.salary").isEqualTo(7500.00);
    }

    @Test
    void update_withInvalidData_shouldReturnStatus400() {
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

        var body = JobRequest.builder()
            .title("")
            .description("")
            .location("")
            .type(JobType.FULL_TIME)
            .level(JobLevel.MID_LEVEL)
            .salary(BigDecimal.valueOf(-7500.00))
            .skills(List.of())
            .build();

        webTestClient.put()
            .uri("/api/jobs/1")
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody()
            .jsonPath("$.message").isEqualTo("Validation error")
            .jsonPath("$.timestamp").isNotEmpty()
            .jsonPath("$.errors").isNotEmpty()
            .jsonPath("$.errors.title").isNotEmpty()
            .jsonPath("$.errors.description").isNotEmpty()
            .jsonPath("$.errors.location").isNotEmpty()
            .jsonPath("$.errors.salary").isNotEmpty()
            .jsonPath("$.errors.skills").isNotEmpty();
    }

    @Test
    void delete_withUnauthenticatedUser_shouldReturnStatus401() {
        webTestClient.delete()
            .uri("/api/jobs/1")
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
            .uri("/api/jobs/1")
            .header("Authorization", "Bearer " + accessToken)
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    void delete_withNotOwnerUser_shouldReturnStatus403() {
        var tokenBody = LoginRequest.builder()
            .email("contato@treinaweb.com.br")
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
            .uri("/api/jobs/1")
            .header("Authorization", "Bearer " + accessToken)
            .exchange()
            .expectStatus().isForbidden();
    }

    @Test
    void delete_withOwnerUser_shouldReturnStatus204() {
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
            .uri("/api/jobs/1")
            .header("Authorization", "Bearer " + accessToken)
            .exchange()
            .expectStatus().isNoContent();
    }
    
}
