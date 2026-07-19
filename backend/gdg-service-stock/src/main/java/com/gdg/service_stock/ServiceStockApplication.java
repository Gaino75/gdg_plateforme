package com.gdg.service_stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceStockApplication.class, args);
        System.out.println("========================================");
        System.out.println("Service Stock démarré sur le port 8083");
        System.out.println("========================================");
    }
}
