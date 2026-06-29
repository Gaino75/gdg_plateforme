package com.gdg.service_notifications;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class GdgServiceNotificationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GdgServiceNotificationsApplication.class, args);
        System.out.println("========================================");
        System.out.println("📬 Service Notifications démarré !");
        System.out.println("🔗 http://localhost:8087/swagger-ui/index.html");
        System.out.println("========================================");
    }
}