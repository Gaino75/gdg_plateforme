package com.gdg.service_ventes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GdgServiceVentesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GdgServiceVentesApplication.class, args);
        System.out.println("========================================");
        System.out.println("💰 Service Ventes démarré !");
        System.out.println("🔗 http://localhost:8084");
        System.out.println("========================================");
    }
}