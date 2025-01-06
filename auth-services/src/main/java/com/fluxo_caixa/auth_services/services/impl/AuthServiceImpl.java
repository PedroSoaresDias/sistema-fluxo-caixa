package com.fluxo_caixa.auth_services.services.impl;

import java.util.concurrent.CompletableFuture;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fluxo_caixa.auth_services.domain.DTO.AuthRequest;
import com.fluxo_caixa.auth_services.domain.DTO.AuthResponse;
import com.fluxo_caixa.auth_services.services.AuthService;
import com.fluxo_caixa.auth_services.services.UserService;
import com.fluxo_caixa.auth_services.utils.JwtTokenGenerator;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtTokenGenerator jwtTokenGenerator) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    public CompletableFuture<AuthResponse> authenticate(AuthRequest authRequest) {
        AuthRequest user = userService.getUserByUsername(authRequest.username());
        if (!passwordEncoder.matches(authRequest.senha(), user.senha())) {
            throw new RuntimeException("Credenciais inválidas");
        }
        String token = jwtTokenGenerator.generateToken(user.username());
        return CompletableFuture.completedFuture(new AuthResponse(token));
    }
}
