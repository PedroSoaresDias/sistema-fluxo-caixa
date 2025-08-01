package com.fluxo_caixa.cash_flow_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.github.cdimascio.dotenv.Dotenv;

@EnableDiscoveryClient
@SpringBootApplication
public class CashFlowServicesApplication {

	public static void main(String[] args) {
		Dotenv dotenv;

        if (System.getenv("DOCKER_ENV") != null) {
            dotenv = Dotenv.configure()
                .directory("/app")
                .load();
        } else {
            dotenv = Dotenv.configure()
                .directory("./")
                .load();
        }

        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));
		SpringApplication.run(CashFlowServicesApplication.class, args);
	}

}
