package com.fluxo_caixa.descovery_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DescoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DescoveryServiceApplication.class, args);
	}

}
