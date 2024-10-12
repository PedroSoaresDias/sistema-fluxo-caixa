package com.fluxo_caixa.cash_flow_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CashFlowServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashFlowServicesApplication.class, args);
	}

}
