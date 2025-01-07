package com.fluxo_caixa.cash_flow_services.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fluxo_caixa.cash_flow_services.adapters.HttpClientAdapter;
import com.fluxo_caixa.cash_flow_services.domain.DTO.UserDTO;
import com.fluxo_caixa.cash_flow_services.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final String userServiceUrl;
    private final HttpClientAdapter httpClient;

    public UserServiceImpl(HttpClientAdapter httpClient, @Value("${user.service.url}") String userServiceUrl) {
        this.httpClient = httpClient;
        this.userServiceUrl = userServiceUrl;
    }

    @Override
    public UserDTO validateUser(Long userId, String jwtToken) {
        String url = userServiceUrl + "/users/" + userId;
        return httpClient.get(url, UserDTO.class, jwtToken);
    }
}