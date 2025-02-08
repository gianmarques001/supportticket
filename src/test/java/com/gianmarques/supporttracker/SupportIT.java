package com.gianmarques.supporttracker;

import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import com.gianmarques.supporttracker.mapper.dto.person.PersonRequestDto;
import com.gianmarques.supporttracker.mapper.dto.person.PersonResponseDto;
import com.gianmarques.supporttracker.mapper.dto.support.SupportListResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class SupportIT {

    @Autowired
    WebTestClient webTestClient;

    // Save Support - With Role Admin with Right Credentials
    @Test
    public void saveSupport_PassingCorrectData_ReturnStatus201() {
        PersonResponseDto responseBody = webTestClient
                .post()
                .uri("api/v1/support")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonRequestDto("Charles Xavier ", "charles@test.com", "12345678"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PersonResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getEmail()).isEqualTo("charles@test.com");
    }

    // Save Support - Without Authenticated
    @Test
    public void saveSupport_PassingIncorrectData_ReturnStatus401() {

        ErrorMessage responseBody = webTestClient
                .post()
                .uri("api/v1/support")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonRequestDto("Charles Xavier", "charles@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(401)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        Assertions.assertThat(responseBody).isNull();
    }

    // Save Support - With Role Client
    @Test
    public void saveSupport_WithRoleClient_ReturnStatus403() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("api/v1/support")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonRequestDto("Charles Xavier ", "charles@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    // Save Support - Duplicate E-mail
    @Test
    public void saveSupport_DuplicateEmail_ReturnStatus409() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("api/v1/support")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonRequestDto("Charles Xavier ", "peter@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    // Save Support - Invalid Fields
    @Test
    public void saveSupport_InvalidFields_ReturnStatus422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("api/v1/support")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new PersonRequestDto("Charles Xavier ", "charles@test.com", null))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }


    // Get All Tickets - With Role Support
    @Test
    public void listTickets_WithRightCredentials_ReturnStatus200() {
        List<SupportListResponseDto> responseBody = webTestClient
                .get()
                .uri("api/v1/support")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "bruce@test.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SupportListResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.size()).isEqualTo(1);
    }

    // Get All Ticket - Without Authenticated
    @Test
    public void listTickets_WithoutAuthenticated_ReturnStatus401() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/support")

                .exchange()
                .expectStatus().isEqualTo(401)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNull();
    }

    // Get All Tickets - With Role Client
    @Test
    public void listTickets_WithRoleClient_ReturnStatus403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/support")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "steve@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }
}
