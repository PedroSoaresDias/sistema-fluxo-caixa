package com.fluxo_caixa.cash_flow_services.domain.DTO;

import java.time.LocalDate;

public record TransactionDTO(Long id, Long userId, Double amount, String type, String description, LocalDate date) {}