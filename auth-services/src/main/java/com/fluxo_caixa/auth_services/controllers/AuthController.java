package com.fluxo_caixa.auth_services.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fluxo_caixa.auth_services.services.AuthService;
import com.fluxo_caixa.auth_services.utils.JwtTokenProvider;

import com.fluxo_caixa.auth_services.domain.DTO.*;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        try {
            UserDetails userDetails = authService.loadUserByUsername(authRequest.getUsername());
    
            if (!passwordEncoder.matches(authRequest.getSenha(), userDetails.getPassword())) {
                return CompletableFuture
                        .completedFuture(ResponseEntity.status(401).body(new AuthResponse("Credenciais inválidas")));
            }
    
            String token = jwtTokenProvider.createToken(userDetails.getUsername());
    
            return CompletableFuture.completedFuture(ResponseEntity.ok(new AuthResponse(token)));
            
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(401).body(new AuthResponse("Falha na autenticação: " + e.getMessage())));
        }
    }
}
