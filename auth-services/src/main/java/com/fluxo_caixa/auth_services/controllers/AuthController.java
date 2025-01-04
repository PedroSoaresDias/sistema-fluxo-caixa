package com.fluxo_caixa.auth_services.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fluxo_caixa.auth_services.domain.DTO.*;
import com.fluxo_caixa.auth_services.services.AuthService;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest).thenApply(ResponseEntity::ok)
                .exceptionally(e -> ResponseEntity.status(401).body(new AuthResponse(e.getMessage())));
    }
}
