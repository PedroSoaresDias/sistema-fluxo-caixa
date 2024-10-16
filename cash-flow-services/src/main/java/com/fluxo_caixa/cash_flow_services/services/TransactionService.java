package com.fluxo_caixa.cash_flow_services.services;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fluxo_caixa.cash_flow_services.domain.DTO.*;
import com.fluxo_caixa.cash_flow_services.domain.model.Transaction;
import com.fluxo_caixa.cash_flow_services.domain.repository.TransactionRepository;
import com.fluxo_caixa.cash_flow_services.exceptions.UserNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransactionService {
    @Value("${user.service.url}")
    private String userServiceUrl;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<TransactionDTO> createTransaction(TransactionDTO transactionDTO) {
        if (!userExists(transactionDTO.getUserId())) {
            throw new UserNotFoundException("User not found with id: " + transactionDTO.getUserId());
        }

        Transaction transaction = new Transaction();
        transaction.setUserId(transactionDTO.getUserId());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setDescription(transactionDTO.getDescription());

        transaction.setDate(LocalDate.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return CompletableFuture.completedFuture(convertToDTO(savedTransaction));
    }

    @Async
    public CompletableFuture<List<TransactionDTO>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(transactionDTOs);
    }

    @Async
    public CompletableFuture<TransactionDTO> getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));

        TransactionDTO transactionDTO = convertToDTO(transaction);
        return CompletableFuture.completedFuture(transactionDTO);
    }

    @Async
    public CompletableFuture<List<TransactionDTO>> getTransactionsByUserId(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(transactionDTOs);
    }

    private boolean userExists(Long userId) {
        try {
            String url = userServiceUrl + "/users/" + userId;
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(url, UserDTO.class);

            return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
        } catch (EntityNotFoundException e) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();

        dto.setId(transaction.getId());
        dto.setUserId(transaction.getUserId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setDescription(transaction.getDescription());
        dto.setDate(transaction.getDate());

        return dto;
    }
}
