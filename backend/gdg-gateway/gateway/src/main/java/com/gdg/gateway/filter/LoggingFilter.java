package com.gdg.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ================================================================
 * FILTRE DE LOGGING — PROJET GPG
 * ================================================================
 *
 * Ce filtre trace toutes les requêtes qui passent par la Gateway.
 * Il est utile pour :
 *   - Déboguer les problèmes en développement
 *   - Voir quel microservice reçoit quoi
 *   - Mesurer le temps de traitement des requêtes
 *
 * Il s'exécute APRÈS le filtre JWT (ordre plus bas que HIGHEST_PRECEDENCE),
 * donc on ne loggue que les requêtes qui ont passé l'authentification.
 *
 * En production, ce filtre pourrait écrire dans un fichier de log
 * ou envoyer les données à un système de monitoring (ELK, Grafana...).
 *
 * ================================================================
 */
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Enregistre la requête entrante et mesure le temps de traitement.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest requete = exchange.getRequest();

        // Horodatage du début de la requête
        long debut = System.currentTimeMillis();
        String timestamp = LocalDateTime.now().format(formatter);

        // Récupérer les infos de la requête
        String methode = requete.getMethod().name();        // GET, POST, PUT, DELETE
        String chemin  = requete.getURI().getPath();        // /auth/login, /admin/dashboard, etc.

        // Récupérer les headers injectés par le filtre JWT
        // (disponibles uniquement si la requête a passé l'authentification)
        String userId = requete.getHeaders().getFirst("X-User-Id");
        String role   = requete.getHeaders().getFirst("X-User-Role");

        // Construire le message de log
        String infoUtilisateur = (userId != null)
                ? "userId=" + userId + " | role=" + role
                : "Non authentifié (route publique)";

        System.out.println("[" + timestamp + "] → "
                + methode + " " + chemin
                + " | " + infoUtilisateur);

        // Passer au filtre suivant et mesurer le temps après traitement
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duree = System.currentTimeMillis() - debut;
            int statusCode = exchange.getResponse().getStatusCode() != null
                    ? exchange.getResponse().getStatusCode().value()
                    : 0;

            System.out.println("[" + timestamp + "] ← "
                    + methode + " " + chemin
                    + " | Status: " + statusCode
                    + " | Durée: " + duree + "ms");
        }));
    }

    /**
     * Ordre 1 : s'exécute juste après le filtre JWT (qui est à HIGHEST_PRECEDENCE).
     * Le filtre JWT est à -2147483648, donc 1 s'exécute après lui.
     */
    @Override
    public int getOrder() {
        return 1;
    }
}
