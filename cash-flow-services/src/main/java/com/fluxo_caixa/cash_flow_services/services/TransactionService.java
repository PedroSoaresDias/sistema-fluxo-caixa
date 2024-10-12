package com.fluxo_caixa.cash_flow_services.services;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fluxo_caixa.cash_flow_services.domain.DTO.UserDTO;
import com.fluxo_caixa.cash_flow_services.domain.model.Transaction;
import com.fluxo_caixa.cash_flow_services.domain.repository.TransactionRepository;

@Service
public class TransactionService {
    @Value("${user.service.url}")
    private String userServiceUrl;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<Transaction> createTransaction(Transaction transaction) {
        String url = userServiceUrl + "/users/" + transaction.getUserId();

        UserDTO userDTO = restTemplate.getForObject(url, UserDTO.class);

        if (userDTO == null) {
            throw new UsernameNotFoundException("User not found");
        }

        transaction.setDate(LocalDate.now());

        return CompletableFuture.completedFuture(transactionRepository.save(transaction));
    }

    @Async
    public CompletableFuture<List<Transaction>> getTransactionsByUser(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        return CompletableFuture.completedFuture(transactions);
    }
}
