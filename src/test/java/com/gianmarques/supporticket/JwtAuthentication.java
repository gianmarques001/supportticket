package com.gianmarques.supporticket;

import com.gianmarques.supporticket.exception.model.ErrorMessage;
import com.gianmarques.supporticket.mapper.dto.person.PersonLoginDto;
import com.gianmarques.supporticket.security.jwt.JwtToken;
import org.springframework.http.HttpHeaders;

import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeadersAuthorization(WebTestClient client, String email, String password) {

        String token = client.
                post()
                .uri("/api/v1/auth")

                .bodyValue(new PersonLoginDto(email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }

    public static Consumer<HttpHeaders> getHeadersAuthorization(WebTestClient client, String email, String password, Integer status) {

        ErrorMessage token = client.
                post()
                .uri("/api/v1/auth")

                .bodyValue(new PersonLoginDto(email, password))
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }
}
