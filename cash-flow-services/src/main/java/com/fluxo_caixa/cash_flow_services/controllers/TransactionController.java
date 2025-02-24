package com.fluxo_caixa.cash_flow_services.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fluxo_caixa.cash_flow_services.domain.DTO.TransactionDTO;
import com.fluxo_caixa.cash_flow_services.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ResponseEntity<List<TransactionDTO>>> getAllTransactions(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.getAllTransactions(jwtToken).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ResponseEntity<TransactionDTO>> getTransactionById(@PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.getTransactionById(id, jwtToken).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ResponseEntity<List<TransactionDTO>>> getTransactionsByUserId(@PathVariable Long userId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.getTransactionsByUserId(userId, jwtToken).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<ResponseEntity<TransactionDTO>> createTransaction(@PathVariable Long userId,
            @RequestBody TransactionDTO transactionDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.createTransaction(transactionDTO, userId, jwtToken).thenApply(savedTransaction -> ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @PutMapping("/{id}/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<ResponseEntity<TransactionDTO>> updateTransaction(@PathVariable Long id, @PathVariable Long userId,
            @RequestBody TransactionDTO transactionDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.updateTransaction(id, transactionDTO, userId, jwtToken).thenApply(updatedTransaction -> ResponseEntity.noContent().build());
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<ResponseEntity<Void>> deleteTransaction(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.deleteTransaction(id, jwtToken).thenApply(deletedTransaction -> ResponseEntity.noContent().build());
    }
}
