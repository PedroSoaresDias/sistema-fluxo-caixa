package com.fluxo_caixa.user_services.services.impl;

import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fluxo_caixa.user_services.domain.DTO.TransactionDTO;
import com.fluxo_caixa.user_services.services.UserTransactionService;

@Service
public class UserTransactionServiceImpl implements UserTransactionService {
    private final TransactionServiceImpl transactionService;

    public UserTransactionServiceImpl(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    @Async
    public CompletableFuture<TransactionDTO> createTransaction(Long userId, TransactionDTO transactionDTO,
            String jwtToken) {
        transactionDTO.setUserId(userId);
        return CompletableFuture.completedFuture(transactionService.createTransaction(transactionDTO, jwtToken));
    }

    @Async
    public CompletableFuture<TransactionDTO> updateTransaction(Long userId, TransactionDTO transactionDTO,
            String jwtToken) {
        transactionDTO.setUserId(userId);
        return CompletableFuture.completedFuture(transactionService.updateTransaction(transactionDTO, jwtToken));
    }
}
