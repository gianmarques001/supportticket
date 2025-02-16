package com.gianmarques.supporticket;


import com.gianmarques.supporticket.entity.Ticket;
import com.gianmarques.supporticket.exception.model.ErrorMessage;
import com.gianmarques.supporticket.mapper.dto.ticket.TicketListResponseDto;
import com.gianmarques.supporticket.mapper.dto.ticket.TicketRequestDto;
import com.gianmarques.supporticket.mapper.dto.ticket.TicketResponseDto;
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
public class TicketIT {

    @Autowired
    WebTestClient webTestClient;

    // Get All Tickets - With Role Admin/Support
    @Test
    public void listAllTickets_WithRoleAdminAndSupport_ReturnStatus200() {

        List<TicketListResponseDto> responseBody = webTestClient
                .get()
                .uri("api/v1/tickets")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "bruce@test.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TicketListResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.size()).isEqualTo(1);

        List<TicketListResponseDto> responseBody2 = webTestClient
                .get()
                .uri("api/v1/tickets")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TicketListResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody2.size()).isEqualTo(1);
    }

    // Get All Tickets - Without Authenticated
    @Test
    public void listAllTickets_WithoutAuthenticated_ReturnStatus401() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/tickets")

                .exchange()
                .expectStatus().isEqualTo(401)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNull();
    }

    // Get All Tickets - With Role Client
    @Test
    public void listAllTickets_WithRoleClient_ReturnStatus403() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/tickets")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    // Get Ticket - With Role Admin/Support
    @Test
    public void getTicketById_WithRoleAdminAndSupport_ReturnStatus200() {

        List<TicketResponseDto> responseBody = webTestClient
                .get()
                .uri("api/v1/tickets/110")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "bruce@test.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TicketResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.size()).isEqualTo(1);


        List<TicketResponseDto> responseBody2 = webTestClient
                .get()
                .uri("api/v1/tickets/110")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TicketResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody2.size()).isEqualTo(1);
    }

    // Get Ticket - Without Authenticated
    @Test
    public void getTicketById_WithoutAuthenticated_ReturnStatus401() {
        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/tickets/110")

                .exchange()
                .expectStatus().isEqualTo(401)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNull();
    }

    // Get Ticket - With Role Client
    @Test
    public void getTicketById_WithRoleClient_ReturnStatus403() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/tickets/110")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    // Get Ticket - Passing Invalid ID
    @Test
    public void getTicketById_PassingInvalidId_ReturnStatus404() {

        ErrorMessage responseBody = webTestClient
                .get()
                .uri("api/v1/tickets/111")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "bruce@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }


    // Send Ticket - With Role Client
    @Test
    public void sendTicket_WithRoleClient_ReturnStatus201() {

        TicketResponseDto responseBody = webTestClient
                .post()
                .uri("api/v1/tickets")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TicketRequestDto("Error on Screen", "Error on my notebook screen"))
                .exchange()
                .expectStatus().isEqualTo(201)
                .expectBody(TicketResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(Ticket.Status.OPEN.toString());
        Assertions.assertThat(responseBody).isNotNull();
    }


    // Send Ticket - Without Authenticated
    @Test
    public void sendTicket_WithoutAuthenticated_ReturnStatus401() {

        ErrorMessage responseBody = webTestClient
                .post()
                .uri("api/v1/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TicketRequestDto("Error on Screen", "Error on my notebook screen"))
                .exchange()
                .expectStatus().isEqualTo(401)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNull();
    }

    // Send Ticket - With Role Admin/Support
    @Test
    public void sendTicket_WithRoleAdminAndSupport_ReturnStatus403() {

        ErrorMessage responseBody = webTestClient
                .post()
                .uri("api/v1/tickets")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TicketRequestDto("Error on Screen", "Error on my notebook screen"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
        Assertions.assertThat(responseBody).isNotNull();

        ErrorMessage responseBody2 = webTestClient
                .post()
                .uri("api/v1/tickets")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "bruce@test.com", "12345678"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new TicketRequestDto("Error on Screen", "Error on my notebook screen"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody2.getStatus()).isEqualTo(403);
        Assertions.assertThat(responseBody2).isNotNull();

    }


    // Update Ticket - With Role Support
    @Test
    public void updateTicketById_WithRoleSupport_ReturnStatus200() {

        webTestClient
                .patch()
                .uri("api/v1/tickets/110")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "bruce@test.com", "12345678"))
                .exchange()
                .expectStatus().isOk();

    }

    // Update Ticket - Without Authenticated
    @Test
    public void updateTicketById_WithoutAuthenticated_ReturnStatus401() {

        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("api/v1/tickets/110")
                .exchange()
                .expectStatus().isEqualTo(401)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNull();

    }

    // Update Ticket - With Role Admin/Client
    @Test
    public void updateTicketById_WithRoleAdminAndClient_ReturnStatus403() {

        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("api/v1/tickets/110")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "tony@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        ErrorMessage responseBody2 = webTestClient
                .patch()
                .uri("api/v1/tickets/110")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "user01@admin.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody2).isNotNull();
        Assertions.assertThat(responseBody2.getStatus()).isEqualTo(403);
    }

    // Update Ticket - With Invalid ID
    @Test
    public void updateTicketById_PassingInvalidId_ReturnStatus404() {

        ErrorMessage responseBody = webTestClient
                .patch()
                .uri("api/v1/tickets/112")
                .headers(JwtAuthentication.getHeadersAuthorization(webTestClient, "bruce@test.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }


}
