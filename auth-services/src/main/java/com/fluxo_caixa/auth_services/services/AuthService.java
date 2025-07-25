package com.fluxo_caixa.auth_services.services;

import com.fluxo_caixa.auth_services.domain.DTO.AuthRequest;
import com.fluxo_caixa.auth_services.domain.DTO.AuthResponse;

public interface AuthService {
    public AuthResponse authenticate(AuthRequest authRequest);
}
