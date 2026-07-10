package com.gdg.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RoutesOuvertesConfig {

    @Bean
    public List<String> routesOuvertes() {
        return List.of(
            "/auth/register/consommateur",
            "/auth/register/distributeur",
            "/auth/login",
            "/auth/verify-email",
            "/auth/forgot-password",
            "/auth/reset-password",
            "/auth/refresh-token",
            "/auth/register/setup",
            "/auth/register/admin",

            "/api/agences/actives",
            
            "/api/enseignes",
            "/api/enseignes/**",
            "/api/villes",
            "/api/villes/**",

            "/api/stocks/public/*/disponibilite",
            "/api/categories",
            "/api/categories/**",

            "/api/paiements/callback/orange",
            "/api/paiements/callback/mtn"
        );
    }
}
