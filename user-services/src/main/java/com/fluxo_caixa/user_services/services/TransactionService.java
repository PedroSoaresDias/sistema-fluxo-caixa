package com.fluxo_caixa.user_services.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fluxo_caixa.user_services.domain.DTO.TransactionDTO;

@Service
public class TransactionService {
    private final RestTemplate restTemplate;
    private final String fluxoCaixaServiceUrl;

    public TransactionService(RestTemplate restTemplate,
            @Value("${fluxo-caixa.service.url}") String fluxoCaixaServiceUrl) {
        this.restTemplate = restTemplate;
        this.fluxoCaixaServiceUrl = fluxoCaixaServiceUrl;
    }
    
    public TransactionDTO createTransaction(TransactionDTO transactionDTO, String jwtToken) {
        String url = fluxoCaixaServiceUrl + "/transactions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<TransactionDTO> request = new HttpEntity<>(transactionDTO, headers);

        ResponseEntity<TransactionDTO> response = restTemplate.postForEntity(url, request, TransactionDTO.class);
        return response.getBody();
    }

    public TransactionDTO updateTransaction(TransactionDTO transactionDTO, String jwtToken) {
        String url = fluxoCaixaServiceUrl + "/transactions/" + transactionDTO.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<TransactionDTO> request = new HttpEntity<>(transactionDTO, headers);

        restTemplate.exchange(url, HttpMethod.PUT, request, TransactionDTO.class);
        return transactionDTO;
    }
}
