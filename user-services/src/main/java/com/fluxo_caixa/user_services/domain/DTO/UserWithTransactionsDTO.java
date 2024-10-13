package com.fluxo_caixa.user_services.domain.DTO;

import java.util.List;

public class UserWithTransactionsDTO {
    private Long id;
    private String username;
    private String email;
    private List<TransactionDTO> transactions;

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<TransactionDTO> getTransactions() {
        return transactions;
    }
    
    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
