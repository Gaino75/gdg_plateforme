package com.gdg.service_reservations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling ()
public class ServiceReservationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceReservationsApplication.class, args);
		System.out.println("========================================");
        System.out.println("Service Reservation démarré sur le port 8085");
        System.out.println("========================================");
	}

}
