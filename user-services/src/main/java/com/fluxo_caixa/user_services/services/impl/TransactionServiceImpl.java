package com.fluxo_caixa.user_services.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fluxo_caixa.user_services.adapters.HttpClientAdapter;
import com.fluxo_caixa.user_services.domain.DTO.TransactionDTO;
import com.fluxo_caixa.user_services.services.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final HttpClientAdapter httpClient;
    private final String fluxoCaixaServiceUrl;

    public TransactionServiceImpl(HttpClientAdapter httpClient,
            @Value("${fluxo-caixa.service.url}") String fluxoCaixaServiceUrl) {
        this.httpClient = httpClient;
        this.fluxoCaixaServiceUrl = fluxoCaixaServiceUrl;
    }
    
    public TransactionDTO createTransaction(TransactionDTO transactionDTO, String jwtToken) {
        String url = fluxoCaixaServiceUrl + "/transactions";
        return httpClient.post(url, transactionDTO, TransactionDTO.class, jwtToken);
    }

    public TransactionDTO updateTransaction(TransactionDTO transactionDTO, String jwtToken) {
        String url = fluxoCaixaServiceUrl + "/transactions/" + transactionDTO.getId();
        httpClient.put(url, transactionDTO, Void.class, jwtToken);
        return transactionDTO;
    }
}
