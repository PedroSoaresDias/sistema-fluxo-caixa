package com.fluxo_caixa.auth_services.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fluxo_caixa.auth_services.domain.DTO.*;
import com.fluxo_caixa.auth_services.exceptions.AuthenticationException;
import com.fluxo_caixa.auth_services.services.AuthService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            return ResponseEntity.ok(authService.authenticate(authRequest));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new AuthResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponse("Erro interno do servidor"));
        }
    }
}
