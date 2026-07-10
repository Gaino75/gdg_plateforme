package com.gdg.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ================================================================
 * POINT D'ENTRÉE DE L'API GATEWAY — PROJET GPG
 * ================================================================
 *
 * Cette classe démarre la Gateway sur le port 8080.
 *
 * La Gateway est le SEUL point d'entrée de toute l'architecture GPG.
 * Le frontend React ne connaît que cette adresse (http://localhost:8080).
 * Il ne connaît PAS les ports 8081, 8082, etc. directement.
 *
 * Flux d'une requête :
 *   Frontend React
 *       ↓
 *   API Gateway (8080)  ← ce service
 *       ↓  [vérifie le JWT, extrait le rôle]
 *       ↓  [redirige vers le bon microservice]
 *       ↓
 *   Microservice cible (8081 à 8088)
 *       ↓
 *   PostgreSQL / Supabase
 *
 * @author Équipe GPG
 * @version 1.0 — 2025-2026
 * ================================================================
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("==============================================");
        System.out.println("   API GATEWAY GPG démarrée sur le port 8080");
        System.out.println("   Tous les microservices sont accessibles via");
        System.out.println("   http://localhost:8080");
        System.out.println("==============================================");
    }
}
