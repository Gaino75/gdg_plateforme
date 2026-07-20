package com.gdg.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * ================================================================
 * CONFIGURATION CORS — PROJET GPG
 * ================================================================
 *
 * CORS = Cross-Origin Resource Sharing
 *
 * Le problème CORS survient quand le frontend (React sur http://localhost:3000)
 * essaie d'appeler la Gateway (http://localhost:8080).
 * Le navigateur bloque par défaut les requêtes entre deux origines différentes.
 *
 * Cette configuration dit au navigateur :
 *   "Oui, je m'attends à recevoir des requêtes venant de http://localhost:3000,
 *    c'est autorisé, voici ce que j'accepte."
 *
 * POURQUOI CONFIGURER CORS ICI ET NULLE PART AILLEURS ?
 *   Avant ce projet, chaque microservice avait @CrossOrigin(origins = "*").
 *   C'était un problème de sécurité : n'importe quelle origine pouvait appeler
 *   directement chaque microservice.
 *
 *   Maintenant que la Gateway est le point d'entrée unique, on configure CORS
 *   UNE SEULE FOIS ici, et les microservices n'ont plus besoin de le faire.
 *   Il faudra donc SUPPRIMER les @CrossOrigin des microservices existants.
 *
 * DIFFÉRENCE AVEC origins = "*" (l'ancien problème) :
 *   origins = "*" → tout le monde peut appeler (dangereux)
 *   origins = "http://localhost:3000" → seulement notre frontend (sécurisé)
 *
 * ================================================================
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // --------------------------------------------------------
        // ORIGINES AUTORISÉES
        // --------------------------------------------------------
        // On n'accepte que notre frontend React.
        // En production, remplacer par l'URL réelle du frontend déployé.
        // Ex: "https://gpg-plateforme.cm"
        // --------------------------------------------------------
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",    // Frontend React en développement local
                "http://localhost:5173"     // Frontend React avec Vite (alternative)
        ));

        // --------------------------------------------------------
        // MÉTHODES HTTP AUTORISÉES
        // --------------------------------------------------------
        // On autorise les méthodes standard utilisées par notre API REST :
        //   GET    → lire des données (stock, agences, etc.)
        //   POST   → créer des données (vente, réservation, etc.)
        //   PUT    → modifier des données (profil agence, statut réservation)
        //   DELETE → supprimer des données (annuler une réservation)
        //   OPTIONS → requête préliminaire CORS (le navigateur l'envoie avant chaque requête cross-origin)
        //   PATCH  → modifier partiellement des données (seuils de stock)
        // --------------------------------------------------------
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // --------------------------------------------------------
        // HEADERS AUTORISÉS DANS LES REQUÊTES
        // --------------------------------------------------------
        // Le frontend peut envoyer ces headers :
        //   Content-Type   → type du corps de la requête (application/json)
        //   Authorization  → le token JWT (Bearer eyJ...)
        //   X-Distributeur-Id, UserId → En-têtes personnalisés
        // --------------------------------------------------------
        config.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Distributeur-Id", "UserId"));

        // --------------------------------------------------------
        // CREDENTIALS
        // --------------------------------------------------------
        // true = on autorise l'envoi de cookies et d'en-têtes d'authentification.
        // Nécessaire pour que le header Authorization (JWT) soit transmis.
        // ATTENTION : si allowCredentials = true, on ne peut pas utiliser
        // allowedOrigins = "*". C'est pour ça qu'on liste les origines explicitement.
        // --------------------------------------------------------
        config.setAllowCredentials(true);

        // --------------------------------------------------------
        // DURÉE DU CACHE CORS (en secondes)
        // --------------------------------------------------------
        // Le navigateur peut mettre en cache la réponse à la requête OPTIONS
        // pendant 3600 secondes (1 heure). Cela évite une requête OPTIONS
        // avant chaque appel API.
        // --------------------------------------------------------
        config.setMaxAge(3600L);

        // Appliquer cette configuration à TOUTES les routes de la Gateway
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
