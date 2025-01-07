package com.fluxo_caixa.cash_flow_services.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.fluxo_caixa.cash_flow_services.domain.DTO.TransactionDTO;

public interface TransactionService {
    public CompletableFuture<TransactionDTO> createTransaction(TransactionDTO transactionDTO, String jwtToken);

    public CompletableFuture<TransactionDTO> updateTransaction(Long id, TransactionDTO transactionDTO, String jwtToken);
    
    public CompletableFuture<Void> deleteTransaction(Long id, String jwtToken);

    public CompletableFuture<List<TransactionDTO>> getAllTransactions(String jwtToken);

    public CompletableFuture<TransactionDTO> getTransactionById(Long id, String jwtToken);

    public CompletableFuture<List<TransactionDTO>> getTransactionsByUserId(Long userId, String jwtToken);
}