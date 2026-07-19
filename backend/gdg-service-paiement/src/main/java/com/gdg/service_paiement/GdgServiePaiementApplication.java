package com.gdg.service_paiement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GdgServiePaiementApplication {

	public static void main(String[] args) {
		SpringApplication.run(GdgServiePaiementApplication.class, args);
		System.out.println("========================================");
        System.out.println("💳 Service Paiement démarré sur le port 8086");
        System.out.println("========================================");

	}

}
