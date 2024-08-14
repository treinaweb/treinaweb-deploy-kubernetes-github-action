package br.com.treinaweb.twjobs.api.ping.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.treinaweb.twjobs.api.E2ETestCommon;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PingRestControllerE2ETest extends E2ETestCommon {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void ping_shouldReturnPongWithStatus200() {
        webTestClient.get()
            .uri("/api/ping")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.message").isNotEmpty();
    }
    
}
