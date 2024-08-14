package br.com.treinaweb.twjobs.api;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@SuppressWarnings("resource")
public abstract class E2ETestCommon {
    
    static final MySQLContainer<?> mysql;

    static {
        mysql = new MySQLContainer<>("mysql:8.1.0")
            .withReuse(true);
        mysql.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

}
