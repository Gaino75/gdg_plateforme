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
            "/api/enseignes/{id}",
            "/api/enseignes/actives",
            "/api/villes",
            "/api/villes/{id}",

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