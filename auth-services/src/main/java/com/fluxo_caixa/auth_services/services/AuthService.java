package com.fluxo_caixa.auth_services.services;

import java.util.concurrent.CompletableFuture;

import com.fluxo_caixa.auth_services.domain.DTO.AuthRequest;
import com.fluxo_caixa.auth_services.domain.DTO.AuthResponse;

public interface AuthService {
    public CompletableFuture<AuthResponse> authenticate(AuthRequest authRequest);
}
