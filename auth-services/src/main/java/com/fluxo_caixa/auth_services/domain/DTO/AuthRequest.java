package com.fluxo_caixa.auth_services.domain.DTO;

public record AuthRequest(Long id, String username, String email, String senha) {}