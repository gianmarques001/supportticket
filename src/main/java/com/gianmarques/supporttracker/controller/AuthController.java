package com.gianmarques.supporttracker.controller;


import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import com.gianmarques.supporttracker.mapper.dto.person.PersonLoginDto;
import com.gianmarques.supporttracker.security.jwt.JwtToken;
import com.gianmarques.supporttracker.security.jwt.JwtUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Resource to authenticate")
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final JwtUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtUserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }


    @Operation(summary = "Authenticate a user", description = "Resource to authenticate a user in system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful authentication and return token.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = JwtToken.class))),
                    @ApiResponse(responseCode = "400", description = "Bad credentials",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid field error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class))),
            })

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@Valid @RequestBody PersonLoginDto personLoginDto, HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(personLoginDto.getEmail(), personLoginDto.getPassword());
            authenticationManager.authenticate(authenticationToken);
            JwtToken token = userDetailsService.getAuthenticatedToken(personLoginDto.getEmail());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            log.warn("Error", e.getMessage());
        }
        return ResponseEntity.badRequest().body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Bad credentials"));

    }

}
