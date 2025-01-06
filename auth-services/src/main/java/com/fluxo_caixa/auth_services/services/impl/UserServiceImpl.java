package com.fluxo_caixa.auth_services.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fluxo_caixa.auth_services.adapters.HttpClientAdapter;
import com.fluxo_caixa.auth_services.domain.DTO.AuthRequest;
import com.fluxo_caixa.auth_services.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final String userServiceUrl;
    private final HttpClientAdapter httpClient;

    public UserServiceImpl(HttpClientAdapter httpClient, @Value("${user.service.url}") String userServiceUrl) {
        this.httpClient = httpClient;
        this.userServiceUrl = userServiceUrl;
    }

    @Override
    public AuthRequest getUserByUsername(String username) {
        String url = userServiceUrl + "/users/username/" + username;
        return httpClient.get(url, AuthRequest.class);
    }
}
