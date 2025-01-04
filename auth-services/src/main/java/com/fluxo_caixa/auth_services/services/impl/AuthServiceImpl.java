package com.fluxo_caixa.auth_services.services.impl;

import java.util.concurrent.CompletableFuture;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fluxo_caixa.auth_services.domain.DTO.AuthRequest;
import com.fluxo_caixa.auth_services.domain.DTO.AuthResponse;
import com.fluxo_caixa.auth_services.services.AuthService;
import com.fluxo_caixa.auth_services.utils.JwtTokenGenerator;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final RemoteUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;

    public AuthServiceImpl(RemoteUserService userService, PasswordEncoder passwordEncoder, JwtTokenGenerator jwtTokenGenerator) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    public CompletableFuture<AuthResponse> authenticate(AuthRequest authRequest) {
        UserDetails userDetails = loadUserByUsername(authRequest.getUsername());
        if (!passwordEncoder.matches(authRequest.getSenha(), userDetails.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }
        String token = jwtTokenGenerator.generateToken(userDetails.getUsername());
        return CompletableFuture.completedFuture(new AuthResponse(token));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AuthRequest user = userService.getUserByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return User.withUsername(user.getUsername())
                    .password(user.getSenha())
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }
}
