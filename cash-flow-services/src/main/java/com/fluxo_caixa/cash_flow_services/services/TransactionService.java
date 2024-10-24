package com.fluxo_caixa.cash_flow_services.services;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    private HttpHeaders createHeaders(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        return headers;
    }

    @Async
    public CompletableFuture<TransactionDTO> createTransaction(TransactionDTO transactionDTO, String jwtToken) {
        validateUser(transactionDTO.getUserId(), jwtToken);

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
    public CompletableFuture<TransactionDTO> getTransactionById(Long id, String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));

        TransactionDTO transactionDTO = convertToDTO(transaction);
        return CompletableFuture.completedFuture(transactionDTO);
    }

    @Async
    public CompletableFuture<List<TransactionDTO>> getTransactionsByUserId(Long userId, String jwtToken) {
        validateUser(userId, jwtToken);

        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(transactionDTOs);
    }

    public void validateUser(Long userId, String jwtToken) {
        HttpHeaders headers = createHeaders(jwtToken);

        String url = userServiceUrl + "/users/" + userId;
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<UserDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserDTO.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new UserNotFoundException("User not found with id: " + userId);
            }
        } catch (Exception e) {
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
