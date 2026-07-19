package com.gdg.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer  // ← Active le serveur de configuration
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
        System.out.println("==============================================");
        System.out.println("   CONFIG SERVER GDG démarré sur le port 8888");
        System.out.println("   http://localhost:8888");
        System.out.println("==============================================");
    }
}