package com.gianmarques.supporttracker;

import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import com.gianmarques.supporttracker.mapper.dto.client.ClientResponseDto;
import com.gianmarques.supporttracker.mapper.dto.client.ClientUpdateDto;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
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
public class ClientIT {


    @Autowired
    WebTestClient webTestClient;

    // Save Client - Passing Correct Data
    @Test
    public void saveClient_PassingCorrectData_ReturnStatus201() {

        ClientResponseDto responseBody = webTestClient
                .post()
                .uri("api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonRequestDto("Stephen Strange", "stephen@test.com", "12345678"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getEmail()).isEqualTo("stephen@test.com");

    }

    // Save Client - Passing Incorrect Data
    @Test
    public void saveClient_PassingIncorrectData_ReturnStatus400() {

        // Incorrect Password
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonRequestDto("Stephen Strange", "stephen@test.com", "1234567"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        // Incorrect Email
        ErrorMessage responseBody2 = webTestClient
                .post()
                .uri("api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonRequestDto("Stephen Strange", null, "12345678"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody2).isNotNull();
        Assertions.assertThat(responseBody2.getStatus()).isEqualTo(400);

    }

    // Save Client - Duplicate E-mail
    @Test
    public void saveClient_DuplicateEmail_ReturnStatus409() {

        ErrorMessage responseBody = webTestClient
                .post()
                .uri("api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonRequestDto("Tony Stark", "tony@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    // Update Client - Passing Correct Data
    @Test
    public void updateClientById_PassingCorrectData_ReturnStatus204() {
        webTestClient
                .patch()
                .uri("api/v1/clients/200")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientUpdateDto("tony@test.com", "12345678", "123456789", "123456789"))
                .exchange()
                .expectStatus().isEqualTo(204);
    }


    // Update Client - Passing Incorrect Data
    @Test
    public void updateClientById_PassingIncorrectData_ReturnStatus204() {

        // Incorrect Password
        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("api/v1/clients/200")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientUpdateDto("tony@test.com", "1234567999", "123456789", "123456789"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        // Incorrect Confirm Password
        ErrorMessage responseBody2 = webTestClient
                .patch()
                .uri("api/v1/clients/200")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientUpdateDto("tony@test.com", "12345678", "1234567800", "123456789"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody2).isNotNull();
        Assertions.assertThat(responseBody2.getStatus()).isEqualTo(400);


        // Without Authenticated
        webTestClient
                .patch()
                .uri("api/v1/clients/200")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ClientUpdateDto("tony@test.com", "12345678", "123456789", "123456789"))
                .exchange()
                .expectStatus().isEqualTo(401);


    }

}
