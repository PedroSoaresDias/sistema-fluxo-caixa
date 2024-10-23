package com.fluxo_caixa.cash_flow_services.utils;

// import java.util.Arrays;
// import java.util.Collection;
// import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject();
    }

    // public Collection<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
    //     DecodedJWT decodedJWT = JWT.decode(token);
    //     String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

    //     return Arrays.stream(roles)
    //             .map(SimpleGrantedAuthority::new)
    //             .collect(Collectors.toList());
    // }
}