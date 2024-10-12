package com.fluxo_caixa.cash_flow_services.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fluxo_caixa.cash_flow_services.domain.model.Transaction;
import com.fluxo_caixa.cash_flow_services.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<List<Transaction>>> getTransactionsByUser(@PathVariable Long userId) {
        return transactionService.getTransactionsByUser(userId).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/")
    public CompletableFuture<ResponseEntity<Transaction>> createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction)
                .thenApply(transactionSaved -> ResponseEntity.status(HttpStatus.CREATED).build());
    }
}
