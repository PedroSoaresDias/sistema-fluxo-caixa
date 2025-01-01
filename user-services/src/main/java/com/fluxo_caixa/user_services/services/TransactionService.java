package com.fluxo_caixa.user_services.services;

import com.fluxo_caixa.user_services.domain.DTO.TransactionDTO;

public interface TransactionService {
    public TransactionDTO createTransaction(TransactionDTO transactionDTO, String jwtToken);
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO, String jwtToken);
}
