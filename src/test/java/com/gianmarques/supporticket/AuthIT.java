package com.gianmarques.supporticket;

import com.gianmarques.supporticket.exception.model.ErrorMessage;
import com.gianmarques.supporticket.mapper.dto.person.PersonLoginDto;
import com.gianmarques.supporticket.security.jwt.JwtToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthIT {

    @Autowired
    private WebTestClient webTestClient;

    // Auth - With Right Credentials
    @Test
    public void auth_WithRightCredentials_ReturnTokenWithStatus200() {
        JwtToken responseBody = webTestClient.post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonLoginDto("user01@admin.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();

    }

    // Auth - With Bad Credentials
    @Test
    public void auth_WithBadCredentials_ReturnTokenWithStatus400() {

        // Incorrect Email
        ErrorMessage responseBody = webTestClient.post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonLoginDto("user01@test.com", "12345678"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        // Incorrect Password
        ErrorMessage responseBody2 = webTestClient.post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonLoginDto("user01@admin.com", "123456799"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody2.getStatus()).isEqualTo(400);

    }


    @Test
    public void auth_WithEmptyCredentials_ReturnTokenWithStatus422(){

        ErrorMessage responseBody = webTestClient.post()
                .uri("api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonLoginDto("user01@test.com", null))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

}
