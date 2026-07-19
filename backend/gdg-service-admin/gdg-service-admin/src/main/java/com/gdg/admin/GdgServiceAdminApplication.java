package com.gdg.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GdgServiceAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(GdgServiceAdminApplication.class, args);
		System.out.println("========================================");
        System.out.println("Service Admin démarré sur le port 8088");
        System.out.println("========================================");
	}

}
