package com.fluxo_caixa.auth_services.services;

import com.fluxo_caixa.auth_services.domain.DTO.AuthRequest;

public interface UserService {
    AuthRequest getUserByUsername(String username);
}
