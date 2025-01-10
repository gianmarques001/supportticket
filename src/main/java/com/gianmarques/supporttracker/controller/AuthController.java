package com.gianmarques.supporttracker.controller;


import com.gianmarques.supporttracker.mapper.dto.person.PersonLoginDto;
import com.gianmarques.supporttracker.exception.model.ErrorMessage;
import com.gianmarques.supporttracker.security.jwt.JwtToken;
import com.gianmarques.supporttracker.security.jwt.JwtUserDetailsService;
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


@RestController
@RequestMapping("api/v1")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final JwtUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtUserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/auth")
    public ResponseEntity<?> auth(@Valid @RequestBody PersonLoginDto personLoginDto, HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(personLoginDto.getEmail(), personLoginDto.getPassword());
            authenticationManager.authenticate(authenticationToken);
            JwtToken token = userDetailsService.getAuthenticatedToken(personLoginDto.getEmail());
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            log.warn("Error to authenticated ", e.getMessage());
        }
        return ResponseEntity.badRequest().body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Bad credentials"));

    }
}
