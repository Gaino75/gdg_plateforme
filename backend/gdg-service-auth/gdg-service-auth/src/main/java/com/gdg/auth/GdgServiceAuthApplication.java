package com.gdg.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GdgServiceAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(GdgServiceAuthApplication.class, args);
		System.out.println("========================================");
        System.out.println("Service Authentification démarré sur le port 8081");
        System.out.println("========================================");
	}

}
