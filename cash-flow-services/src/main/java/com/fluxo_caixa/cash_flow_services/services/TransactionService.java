package com.fluxo_caixa.cash_flow_services.services;

import java.util.List;

import com.fluxo_caixa.cash_flow_services.domain.DTO.TransactionDTO;

public interface TransactionService {
    public TransactionDTO createTransaction(TransactionDTO transactionDTO, Long userId, String jwtToken);

    public TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO, Long userId, String jwtToken);
    
    public Void deleteTransaction(Long id, String jwtToken);

    public List<TransactionDTO> getAllTransactions(String jwtToken);

    public TransactionDTO getTransactionById(Long id, String jwtToken);

    public List<TransactionDTO> getTransactionsByUserId(Long userId, String jwtToken);
}