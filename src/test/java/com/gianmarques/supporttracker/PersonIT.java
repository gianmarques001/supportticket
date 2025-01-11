package com.gianmarques.supporttracker;


import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import com.gianmarques.supporttracker.mapper.dto.person.PersonResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PersonIT {


    @Autowired
    private WebTestClient webTestClient;

    // Get Persons - With Role Admin
    @Test
    public void listPersons_WithRoleAdminUsingRightCredentials_ReturnStatus200() {

        List<PersonResponseDto> responseBody = webTestClient
                .get()
                .uri("api/v1/persons")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PersonResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.size()).isEqualTo(5);
    }

    // Get Persons - With Role Admin with Bad Credentials
    @Test
    public void listPersons_WithRoleAdminUsingBadCredentials_ReturnErrorMessageStatus401() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/persons")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345679", 400))
                .exchange()
                .expectStatus().isEqualTo(401)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNull();
    }

    // Get Persons - With Role Support
    @Test
    public void listPersons_WithRoleSupport_ReturnErrorMessageStatus403() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/persons")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "peter@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    // Get Persons - With Role Client
    @Test
    public void listPersons_WithRoleClient_ReturnErrorMessageStatus403() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/persons")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    // Get Person - With Role Admin - Passing Valid Id
    @Test
    public void getPersonById_WithRoleAdminValidId_ReturnStatus200() {

        PersonResponseDto responseBody = webTestClient
                .get()
                .uri("api/v1/persons/200")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PersonResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getEmail()).isEqualTo("tony@test.com");
    }

    // Get Person - With Role Admin - Passing Invalid Id
    @Test
    public void getPersonById_WithRoleAdminWithInvalidId_ReturnStatus200() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/persons/500")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();

    }

    // Get Person - With Role Support/Client
    @Test
    public void getPersonById_WithRoleSupportAndClient_ReturnErrorMessage403() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/persons/200")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        ErrorMessage responseBody2 = webTestClient
                .get()
                .uri("api/v1/persons/200")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "peter@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody2.getStatus()).isEqualTo(403);

    }


}
