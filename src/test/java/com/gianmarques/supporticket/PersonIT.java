package com.gianmarques.supporticket;


import com.gianmarques.supporticket.exception.model.ErrorMessage;
import com.gianmarques.supporticket.mapper.dto.person.PersonResponseDto;
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
    public void listPersons_WithRightCredentials_ReturnStatus200() {

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


    // Get Persons - Without Authenticated
    @Test
    public void listPersons_WithoutAuthenticated_ReturnStatus401() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/persons")
                .exchange()
                .expectStatus().isEqualTo(401)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNull();
    }

    // Get Persons - With Role Client/Support
    @Test
    public void listPersons_WithRoleClientAndSupport_ReturnStatus403() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/persons")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "peter@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        ErrorMessage responseBody2 = webTestClient
                .get()
                .uri("api/v1/persons")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody2.getStatus()).isEqualTo(403);
    }


    // Get Person - With Role Admin
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


    // Get Person - Without Authenticated
    @Test
    public void getPersonById_WithoutAuthenticated_ReturnStatus401() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/persons/200")
                .exchange()
                .expectStatus().isEqualTo(401)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNull();

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


    // Get Person - Passing Invalid Id
    @Test
    public void getPersonById_PassingInvalidId__ReturnStatus404() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/persons/500")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }
}
