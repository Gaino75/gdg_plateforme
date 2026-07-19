package com.gdg.service_agences;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceAgencesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAgencesApplication.class, args);
		System.out.println("========================================");
        System.out.println("Service Agence démarré sur le port 8082");
        System.out.println("========================================");
	}

}
