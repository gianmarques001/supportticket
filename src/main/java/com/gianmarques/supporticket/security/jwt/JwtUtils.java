package com.gianmarques.supporticket.security.jwt;

// Classe Responsavel por gerar o Token

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtUtils {

    public static final String JWT_BEARER = "Bearer ";

    public static final String JWT_AUTHORIZATION = "Authorization";

    public static final String SECRET_KEY = "0123456789-0123456789-0123456789";

    public static final long EXPIRE_DAYS = 0;

    public static final long EXPIRE_HOURS = 0;

    public static final long EXPIRE_MINUTES = 10;
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);


    private JwtUtils() {
    }

    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)); // Prepara a chave para ser criptografada no momento que gerar o token
    }


    private static Date toExpireDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static JwtToken createToken(String email, String role) {
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);
        String token = Jwts.builder()
                .subject(email) // email do usuario -> precisa confirmar se esse token é válido, por conta disso precisa passar um campo unico para identificar
                .issuedAt(issuedAt) // passa o momento que o token foi criado
                .expiration(limit) // passa o limite do token
                .signWith(getSecretKey())
                .claim("role", role) // chave e valor, adiciona alguma informação no token que não possui método para isso.
                .compact();

        return new JwtToken(token);
    }

    // gpt
    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(refactorToken(token)).getPayload();
        } catch (JwtException e) {
            log.error("Token inválido");
        }
        return null;
    }


    // remove a Bearer do token
    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }

    public static String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // gpt
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(refactorToken(token));
            return true;
        } catch (JwtException ex) {
            log.error(String.format("Token invalido %s", ex.getMessage()));
        }
        return false;
    }

}
