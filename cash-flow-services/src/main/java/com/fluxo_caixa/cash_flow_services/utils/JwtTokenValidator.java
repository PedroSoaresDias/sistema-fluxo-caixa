package com.fluxo_caixa.cash_flow_services.utils;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtTokenValidator {
    @Value("${jwt.secret}")
    private String secret;

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
            return !isTokenExpired(decodedJWT);
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
        return decodedJWT.getSubject();
    }

    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().before(new Date());
    }
}