package com.fluxo_caixa.user_services.services;

import java.util.concurrent.CompletableFuture;

import com.fluxo_caixa.user_services.domain.DTO.TransactionDTO;

public interface UserTransactionService {
    public CompletableFuture<TransactionDTO> createTransaction(Long userId, TransactionDTO transactionDTO, String jwtToken);
    public CompletableFuture<TransactionDTO> updateTransaction(Long userId, TransactionDTO transactionDTO, String jwtToken);
}
