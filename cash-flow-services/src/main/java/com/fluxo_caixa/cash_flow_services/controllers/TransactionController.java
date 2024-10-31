package com.fluxo_caixa.cash_flow_services.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fluxo_caixa.cash_flow_services.domain.DTO.TransactionDTO;
import com.fluxo_caixa.cash_flow_services.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<TransactionDTO>>> getAllTransactions(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.getAllTransactions(jwtToken).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<TransactionDTO>> getTransactionById(@PathVariable Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.getTransactionById(id, jwtToken).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<List<TransactionDTO>>> getTransactionsByUserId(@PathVariable Long userId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.getTransactionsByUserId(userId, jwtToken).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<TransactionDTO>> createTransaction(
            @RequestBody TransactionDTO transactionDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.createTransaction(transactionDTO, jwtToken).thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<TransactionDTO>> updateTransaction(@PathVariable Long id,
            @RequestBody TransactionDTO transactionDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.updateTransaction(id, transactionDTO, jwtToken).thenApply(ResponseEntity::ok);
    }
    
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteTransaction(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String jwtToken = authHeader.substring(7);
        return transactionService.deleteTransaction(id, jwtToken).thenApply(ResponseEntity::ok);
    }
}
