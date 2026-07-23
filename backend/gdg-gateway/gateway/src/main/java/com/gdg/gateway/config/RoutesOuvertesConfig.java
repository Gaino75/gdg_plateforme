package com.gdg.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RoutesOuvertesConfig {

    @Bean
    public List<String> routesOuvertes() {
        return List.of(
            // ============================================================
            // AUTH — Routes publiques
            // ============================================================
            "/auth/register/consommateur",
            "/auth/register/distributeur",
            "/auth/login",
            "/auth/verify-email",
            "/auth/forgot-password",
            "/auth/reset-password",
            "/auth/refresh-token",
            "/auth/register/setup",
            "/auth/register/admin",

            // ============================================================
            // AGENCES — Routes publiques (lecture seule)
            // ============================================================
            "/api/agences/actives",
            "/api/agences/{id}",           // Détail d'une agence publique
            "/api/agences/ville/{villeId}", // Filtrer par ville
            "/api/agences/enseigne/{enseigneId}", // Filtrer par enseigne
            "/api/enseignes",
            "/api/enseignes/**",
            "/api/enseignes/{id}",
            "/api/enseignes/actives",
            "/api/villes",
            "/api/villes/{id}",
            "/api/agences/statistiques", // Statistiques publiques sur les agences
            "/api/agences/enseigne/**", // Filtrer par enseigne
            "/api/agences/ville/**", // Filtrer par ville
            "/api/agences/recherche", // Recherche d'agences par nom, ville, enseigne, etc.
        

            //"/api/agences/enseigne/{enseigneId}/actives", // Filtrer par enseigne et actives

            // ============================================================
            // STOCK — Routes publiques (lecture seule)
            // ============================================================
            "/api/stocks/public/*/disponibilite",
            "/api/categories",
            "/api/categories/{id}",

            // ============================================================
            // PAIEMENT — Callbacks (appelés par Orange/MTN, pas par le frontend)
            // ============================================================
            "/api/paiements/callback/orange",
            "/api/paiements/callback/mtn"
        );
    }
}