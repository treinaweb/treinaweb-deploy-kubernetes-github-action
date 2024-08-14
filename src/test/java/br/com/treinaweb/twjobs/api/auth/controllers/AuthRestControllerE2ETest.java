package br.com.treinaweb.twjobs.api.auth.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.treinaweb.twjobs.api.E2ETestCommon;
import br.com.treinaweb.twjobs.api.auth.dtos.LoginRequest;
import br.com.treinaweb.twjobs.api.auth.dtos.RefreshRequest;
import br.com.treinaweb.twjobs.api.auth.dtos.TokenResponse;
import br.com.treinaweb.twjobs.api.auth.dtos.UserRequest;
import br.com.treinaweb.twjobs.core.enums.Role;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthRestControllerE2ETest extends E2ETestCommon {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void register_withValidUserRequest_shouldReturnUserResponseAndStatus201() {
        var body = UserRequest.builder()
            .name("Cleyson Lima")
            .email("cleyson@mail.com")
            .password("senha@123")
            .role(Role.CANDIDATE)
            .build();


        webTestClient.post()
            .uri("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.id").isNumber()
            .jsonPath("$.name").isEqualTo(body.getName())
            .jsonPath("$.email").isEqualTo(body.getEmail())
            .jsonPath("$.role").isEqualTo(body.getRole().toString());
    }

    @Test
    void login_withValidLoginRequest_shouldReturnTokenResponseAndStatus200() {
        var body = LoginRequest.builder()
            .email("contato@avmakers.com.br")
            .password("senha@123")
            .build();

        webTestClient.post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.accessToken").isNotEmpty()
            .jsonPath("$.refreshToken").isNotEmpty();
    }

    @Test
    void login_withInvalidLoginRequest_shouldReturnStatus401() {
        var body = LoginRequest.builder()
            .email("invaliduser@mail.com")
            .password("invalidpassword")
            .build();

        webTestClient.post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isUnauthorized();
    }

    @Test
    void refresh_withValidRefreshRequest_shouldReturnTokenResponseAndStatus200() {
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
        var refreshToken = tokenResponse.getRefreshToken();

        var refreshBody = RefreshRequest.builder()
            .refreshToken(refreshToken)
            .build();

        webTestClient.post()
            .uri("/api/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(refreshBody)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.accessToken").isNotEmpty()
            .jsonPath("$.refreshToken").isNotEmpty();
    }

    @Test
    void refresh_withInvalidRefreshRequest_shouldReturnStatus401() {
        var body = RefreshRequest.builder()
            .refreshToken("invalidtoken")
            .build();

        webTestClient.post()
            .uri("/api/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .exchange()
            .expectStatus().isUnauthorized();
    }
}
