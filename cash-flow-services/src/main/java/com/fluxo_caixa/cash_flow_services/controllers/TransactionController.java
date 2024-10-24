package com.fluxo_caixa.cash_flow_services.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public CompletableFuture<ResponseEntity<List<TransactionDTO>>> getAllTransactions() {
        return transactionService.getAllTransactions().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<TransactionDTO>> getTransactionById(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = extractToken(authorizationHeader);
        return transactionService.getTransactionById(id, jwtToken).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<List<TransactionDTO>>> getTransactionsByUserId(@PathVariable Long userId, @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = extractToken(authorizationHeader);
        return transactionService.getTransactionsByUserId(userId, jwtToken).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<TransactionDTO>> createTransaction(@RequestBody TransactionDTO transactionDTO, @RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = extractToken(authorizationHeader);
        return transactionService.createTransaction(transactionDTO, jwtToken).thenApply(ResponseEntity::ok);
    }
    
    private String extractToken(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ") ?
               authorizationHeader.substring(7) : null;
    }
}
