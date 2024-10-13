package com.fluxo_caixa.cash_flow_services.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fluxo_caixa.cash_flow_services.domain.DTO.TransactionDTO;
import com.fluxo_caixa.cash_flow_services.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<List<TransactionDTO>>> getTransactionsByUserId(@PathVariable Long userId) {
        return transactionService.getTransactionsByUserId(userId).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<TransactionDTO>> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO)
                .thenApply(ResponseEntity::ok);
    }
}
