package com.fluxo_caixa.auth_services.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fluxo_caixa.auth_services.domain.DTO.AuthRequest;

@Service
public class AuthService implements UserDetailsService {
    @Value("${user.service.url}")
    private String userServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String identifier) {
        String url = userServiceUrl + "/users/username/" + identifier;
        
        try {
            AuthRequest userDTO = restTemplate.getForObject(url, AuthRequest.class);
            
            if (userDTO == null) {
                throw new UsernameNotFoundException("User not found");
            }

            return User.withUsername(userDTO.getUsername())
                    .password(userDTO.getSenha())
                    // .authorities(userDTO.getRoles().stream().map(SimpleGrantedAuthority::new).toList())
                    .build();

        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }
}
