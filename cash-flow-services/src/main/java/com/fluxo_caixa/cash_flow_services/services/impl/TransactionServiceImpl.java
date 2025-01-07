package com.fluxo_caixa.cash_flow_services.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fluxo_caixa.cash_flow_services.domain.DTO.*;
import com.fluxo_caixa.cash_flow_services.domain.model.Transaction;
import com.fluxo_caixa.cash_flow_services.domain.repository.TransactionRepository;
import com.fluxo_caixa.cash_flow_services.services.TransactionService;
import com.fluxo_caixa.cash_flow_services.services.UserService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    private HttpHeaders createHeaders(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        return headers;
    }

    @Async
    public CompletableFuture<TransactionDTO> createTransaction(TransactionDTO transactionDTO, String jwtToken) {
        userService.validateUser(transactionDTO.userId(), jwtToken);

        Transaction transaction = new Transaction();
        transaction.setUserId(transactionDTO.userId());
        transaction.setAmount(transactionDTO.amount());
        transaction.setType(transactionDTO.type());
        transaction.setDescription(transactionDTO.description());
        transaction.setDate(LocalDate.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return CompletableFuture.completedFuture(convertToDTO(savedTransaction));
    }

    @Async
    public CompletableFuture<TransactionDTO> updateTransaction(Long id, TransactionDTO transactionDTO, String jwtToken) {
        userService.validateUser(transactionDTO.userId(), jwtToken);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));

        transaction.setUserId(transactionDTO.userId());
        transaction.setAmount(transactionDTO.amount());
        transaction.setType(transactionDTO.type());
        transaction.setDescription(transactionDTO.description());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return CompletableFuture.completedFuture(convertToDTO(savedTransaction));
    }
    
    @Async
    public CompletableFuture<Void> deleteTransaction(Long id, String jwtToken) {
        createHeaders(jwtToken);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));

        transactionRepository.delete(transaction);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<TransactionDTO>> getAllTransactions(String jwtToken) {
        createHeaders(jwtToken);
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(transactionDTOs);
    }

    @Async
    public CompletableFuture<TransactionDTO> getTransactionById(Long id, String jwtToken) {
        createHeaders(jwtToken);
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));

        TransactionDTO transactionDTO = convertToDTO(transaction);
        return CompletableFuture.completedFuture(transactionDTO);
    }

    @Async
    public CompletableFuture<List<TransactionDTO>> getTransactionsByUserId(Long userId, String jwtToken) {
        userService.validateUser(userId, jwtToken);

        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(transactionDTOs);
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO(transaction.getId(), transaction.getUserId(), transaction.getAmount(), transaction.getType(), transaction.getDescription(), transaction.getDate());

        return dto;
    }
}