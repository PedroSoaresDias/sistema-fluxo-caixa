package com.fluxo_caixa.auth_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import io.github.cdimascio.dotenv.Dotenv;

@EnableAsync
@SpringBootApplication
public class AuthServicesApplication {

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
		SpringApplication.run(AuthServicesApplication.class, args);
	}

}
