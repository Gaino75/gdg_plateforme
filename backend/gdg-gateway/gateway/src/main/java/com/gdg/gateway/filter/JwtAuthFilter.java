package com.gdg.gateway.filter;

import com.gdg.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * ================================================================
 * FILTRE JWT — COEUR DE LA GATEWAY GPG
 * ================================================================
 *
 * Ce filtre est exécuté automatiquement pour CHAQUE requête qui
 * arrive sur la Gateway, avant d'être redirigée vers un microservice.
 *
 * Son rôle est simple :
 *   1. Vérifier si la route est publique (pas besoin de JWT)
 *      → Si oui : laisser passer directement
 *   2. Si la route est protégée, chercher le token JWT dans le header
 *      → Si absent : rejeter avec 401 UNAUTHORIZED
 *   3. Valider le token JWT (signature, expiration...)
 *      → Si invalide : rejeter avec 401 UNAUTHORIZED
 *   4. Extraire le rôle de l'utilisateur depuis le token
 *   5. Vérifier que le rôle est autorisé pour cette route
 *      → Si interdit : rejeter avec 403 FORBIDDEN
 *   6. Enrichir la requête avec les infos de l'utilisateur
 *      (ajouter des headers X-User-Id, X-User-Role, X-User-Email)
 *      → Le microservice cible peut lire ces headers sans décoder le JWT
 *   7. Transmettre la requête enrichie au microservice cible
 *
 * IMPORTANT : Spring Cloud Gateway est basé sur WebFlux (réactif).
 * Les méthodes retournent des Mono<Void> au lieu de void classique.
 * Mono = un conteneur asynchrone qui contiendra un résultat "plus tard".
 *
 * ================================================================
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    /**
     * L'utilitaire qui sait lire et valider les tokens JWT.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * La liste des routes publiques (sans JWT requis).
     * Définie dans RoutesOuvertesConfig.java
     */
    @Autowired
    private List<String> routesOuvertes;

    /**
     * ============================================================
     * MÉTHODE PRINCIPALE : filter()
     * ============================================================
     * C'est ici que tout se passe. Cette méthode est appelée
     * automatiquement par Spring Cloud Gateway pour chaque requête.
     *
     * @param exchange contient la requête HTTP entrante et la réponse à construire
     * @param chain    permet de passer la requête au filtre suivant (ou au microservice)
     * @return Mono<Void> — le traitement asynchrone de la requête
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // Récupérer la requête HTTP entrante
        ServerHttpRequest requete = exchange.getRequest();

        // Récupérer le chemin de l'URL (ex: "/auth/login", "/admin/agences/en-attente")
        String chemin = requete.getURI().getPath();

        // --------------------------------------------------------
        // ÉTAPE 1 : Vérifier si la route est publique
        // --------------------------------------------------------
        // On parcourt la liste des routes ouvertes et on vérifie
        // si le chemin de la requête correspond à l'une d'elles.
        // On utilise "matches" avec des wildcards pour gérer les patterns comme /auth/**
        // --------------------------------------------------------
        boolean estPublique = routesOuvertes.stream()
                .anyMatch(route -> correspondAuPattern(chemin, route));

        if (estPublique) {
            // Route publique → on laisse passer sans vérifier de JWT
            System.out.println("[GATEWAY] Route publique, accès libre : " + chemin);
            return chain.filter(exchange);
        }

        // --------------------------------------------------------
        // ÉTAPE 2 : La route est protégée → chercher le token JWT
        // --------------------------------------------------------
        // Le token doit être dans le header HTTP "Authorization"
        // avec le format : "Bearer eyJhbGci..."
        //
        // HttpHeaders.AUTHORIZATION = "Authorization"
        // --------------------------------------------------------
        String headerAuthorization = requete.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Vérifier que le header Authorization existe et commence par "Bearer "
        if (headerAuthorization == null || !headerAuthorization.startsWith("Bearer ")) {
            System.err.println("[GATEWAY] Header Authorization manquant ou mal formé pour : " + chemin);
            return rejeterRequete(exchange, HttpStatus.UNAUTHORIZED,
                    "Token JWT requis. Connectez-vous d'abord via POST /auth/login");
        }

        // Extraire le token en retirant le préfixe "Bearer "
        // "Bearer eyJhbGci..." → "eyJhbGci..."
        String token = headerAuthorization.substring(7);

        // --------------------------------------------------------
        // ÉTAPE 3 : Valider le token JWT
        // --------------------------------------------------------
        // jwtUtil.estValide() vérifie :
        //   - La signature (le token n'a pas été falsifié)
        //   - La date d'expiration (le token n'est pas expiré)
        //   - Le format (c'est bien un JWT valide)
        // --------------------------------------------------------
        if (!jwtUtil.estValide(token)) {
            System.err.println("[GATEWAY] Token JWT invalide ou expiré pour : " + chemin);
            return rejeterRequete(exchange, HttpStatus.UNAUTHORIZED,
                    "Token JWT invalide ou expiré. Reconnectez-vous.");
        }

        // --------------------------------------------------------
        // ÉTAPE 4 : Extraire les informations de l'utilisateur
        // --------------------------------------------------------
        String userId = jwtUtil.extraireUserId(token);
        String role   = jwtUtil.extraireRole(token);
        String email  = jwtUtil.extraireEmail(token);

        System.out.println("[GATEWAY] Requête authentifiée → userId=" + userId
                + " | role=" + role + " | chemin=" + chemin);

        // --------------------------------------------------------
        // ÉTAPE 5 : Contrôle d'accès basé sur les rôles (RBAC)
        // --------------------------------------------------------
        // Vérifier que l'utilisateur a le droit d'accéder à cette route.
        //
        // Règles :
        //   /admin/**        → ADMIN uniquement (le plus sensible)
        //   /ventes/**       → DISTRIBUTEUR ou ADMIN
        //   /approvisionnements/** → DISTRIBUTEUR ou ADMIN
        //   /factures/**     → DISTRIBUTEUR ou ADMIN (ou CONSOMMATEUR pour ses propres factures)
        //   /reservations/** → CONSOMMATEUR, DISTRIBUTEUR ou ADMIN
        //   Tout le reste    → n'importe quel utilisateur connecté
        // --------------------------------------------------------
        if (!estAutorise(chemin, role)) {
            System.err.println("[GATEWAY] Accès refusé → role=" + role
                    + " non autorisé pour : " + chemin);
            return rejeterRequete(exchange, HttpStatus.FORBIDDEN,
                    "Accès refusé. Votre rôle (" + role + ") n'est pas autorisé pour cette action.");
        }

        // --------------------------------------------------------
        // ÉTAPE 6 : Enrichir la requête avec les infos utilisateur
        // --------------------------------------------------------
        // On ajoute des headers personnalisés à la requête avant de la
        // transmettre au microservice cible.
        //
        // Ainsi, le microservice peut lire directement :
        //   - X-User-Id    : l'ID de l'utilisateur connecté
        //   - X-User-Role  : son rôle (CONSOMMATEUR, DISTRIBUTEUR, ADMIN)
        //   - X-User-Email : son email
        //
        // Plus besoin pour le microservice de décoder le JWT lui-même !
        // C'est la Gateway qui fait ce travail et partage le résultat.
        //
        // SECURITE : Ces headers ne viennent QUE de la Gateway (côté serveur).
        // Un utilisateur malveillant ne peut pas les forger car on les REMPLACE
        // toujours (mutate().header() écrase les valeurs existantes).
        // C'est pour ça qu'on ne doit JAMAIS faire confiance à ces headers
        // si la requête vient directement d'un client externe (sans passer par la Gateway).
        // --------------------------------------------------------
        ServerHttpRequest requeteEnrichie = requete.mutate()
                .header("X-User-Id", userId)       // ID de l'utilisateur
                .header("X-User-Role", role)        // Rôle : CONSOMMATEUR, DISTRIBUTEUR ou ADMIN
                .header("X-User-Email", email)      // Email de l'utilisateur
                .build();

        // --------------------------------------------------------
        // ÉTAPE 7 : Transmettre la requête enrichie au microservice
        // --------------------------------------------------------
        // exchange.mutate().request(requeteEnrichie) crée un nouvel exchange
        // avec notre requête enrichie (avec les nouveaux headers).
        // chain.filter() passe cet exchange au prochain filtre ou au microservice.
        // --------------------------------------------------------
        return chain.filter(exchange.mutate().request(requeteEnrichie).build());
    }

    /**
     * ============================================================
     * CONTRÔLE D'ACCÈS BASÉ SUR LES RÔLES (RBAC)
     * ============================================================
     * Vérifie si le rôle de l'utilisateur est autorisé à accéder
     * au chemin demandé.
     *
     * @param chemin le chemin de l'URL (ex: "/admin/agences/en-attente")
     * @param role   le rôle de l'utilisateur ("CONSOMMATEUR", "DISTRIBUTEUR", "ADMIN")
     * @return true si l'accès est autorisé, false sinon
     */
    private boolean estAutorise(String chemin, String role) {

        // --------------------------------------------------------
        // ROUTES ADMIN : ADMIN uniquement
        // --------------------------------------------------------
        // Validation des agences, gestion des comptes, signalements, dashboard global.
        // C'est la zone la plus sensible. Un DISTRIBUTEUR ou CONSOMMATEUR
        // ne doit JAMAIS pouvoir y accéder.
        // --------------------------------------------------------
        if (chemin.startsWith("/admin/")) {
            return "ADMIN".equals(role);
        }

        if (chemin.startsWith("/api/ventes") || chemin.startsWith("/ventes/")
                || chemin.startsWith("/api/approvisionnements/") || chemin.startsWith("/approvisionnements/")) {
            return "DISTRIBUTEUR".equals(role) || "ADMIN".equals(role);
        }

        if (chemin.startsWith("/api/factures/") || chemin.startsWith("/factures/")) {
            return "DISTRIBUTEUR".equals(role) || "ADMIN".equals(role)
                    || "CONSOMMATEUR".equals(role);
        }

        if (chemin.contains("/decrementer") || chemin.contains("/incrementer")
                || chemin.contains("/approvisionner") || chemin.contains("/seuil")) {
            return "DISTRIBUTEUR".equals(role) || "ADMIN".equals(role);
        }

        if (chemin.startsWith("/api/reservations/") || chemin.startsWith("/reservations/")) {
            return "CONSOMMATEUR".equals(role)
                    || "DISTRIBUTEUR".equals(role)
                    || "ADMIN".equals(role);
        }

        if (chemin.startsWith("/api/paiements/") || chemin.startsWith("/paiements/")) {
            return "CONSOMMATEUR".equals(role)
                    || "DISTRIBUTEUR".equals(role)
                    || "ADMIN".equals(role);
        }

        if (chemin.startsWith("/api/notifications/") || chemin.startsWith("/notifications/")
                || chemin.startsWith("/api/abonnements/") || chemin.startsWith("/abonnements/")
                || chemin.startsWith("/api/signalements/") || chemin.startsWith("/signalements/")) {
            return "CONSOMMATEUR".equals(role)
                    || "DISTRIBUTEUR".equals(role)
                    || "ADMIN".equals(role);
        }
        if(chemin.contains("/critiques") || chemin.contains("/historiques")) {
            return "DISTRIBUTEUR".equals(role)
                    || "ADMIN".equals(role);
        }
        if (chemin.startsWith("/api/stocks/agence/") || chemin.startsWith("/stock/agence/")) {
            return "CONSOMMATEUR".equals(role)
                    || "DISTRIBUTEUR".equals(role)
                    || "ADMIN".equals(role);
        }
        if(chemin.contains("/a-verifier")  || chemin.contains("/resoudre")){
            return "ADMIN".equals(role);
        }
        if(chemin.startsWith("/api/notifications/"))
        if(chemin.equals("/api/signalements") || chemin.equals("/api/stocks/global")){
            return "ADMIN".equals(role);
        }

        return true;
    }

    /**
     * ============================================================
     * CORRESPONDANCE AVEC UN PATTERN D'URL
     * ============================================================
     * Vérifie si un chemin correspond à un pattern de route.
     * Supporte le wildcard ** (tout ce qui suit).
     *
     * Exemples :
     *   correspondAuPattern("/auth/login", "/auth/login")    → true
     *   correspondAuPattern("/auth/register", "/auth/**")    → true
    
     * @return true si le chemin correspond au pattern
     */
    private boolean correspondAuPattern(String chemin, String pattern) {
        // Cas simple : correspondance exacte
        if (chemin.equals(pattern)) {
            return true;
        }

        // Cas wildcard : pattern se termine par /**
        // Ex: "/auth/**" → correspond à "/auth/login", "/auth/register", etc.
        if (pattern.endsWith("/**")) {
            String prefixe = pattern.substring(0, pattern.length() - 3);
            return chemin.startsWith(prefixe);
        }

        // Cas wildcard simple : pattern contient *
        // Ex: "/stock/agence/*/dispo" → correspond à "/stock/agence/5/dispo"
        if (pattern.contains("*")) {
            // Convertir le pattern en regex :
            // "." → "\." (échapper le point)
            // "*" → "[^/]*" (n'importe quoi sauf un slash)
            String regex = pattern
                    .replace(".", "\\.")
                    .replace("*", "[^/]*");
            return chemin.matches(regex);
        }

        return false;
    }

    /**
     * ============================================================
     * REJETER UNE REQUÊTE
     * ============================================================
     * Construit et envoie une réponse d'erreur HTTP au client.
     *
     * @param exchange l'échange HTTP en cours
     * @param status   le code HTTP à retourner (401, 403, etc.)
     * @param message  le message d'erreur (pour les logs, pas retourné au client ici)
     * @return Mono<Void> — la réponse d'erreur
     */
    private Mono<Void> rejeterRequete(ServerWebExchange exchange,
                                       HttpStatus status,
                                       String message) {
        ServerHttpResponse reponse = exchange.getResponse();
        reponse.setStatusCode(status);

        // Optionnel : ajouter un header explicatif
        reponse.getHeaders().add("X-Gateway-Error", message);

        // Compléter la réponse sans corps (juste le code HTTP)
        // En production on pourrait retourner un JSON d'erreur
        return reponse.setComplete();
    }

    /**
     * ============================================================
     * ORDRE DE PRIORITÉ DU FILTRE
     * ============================================================
     * Dans Spring Cloud Gateway, plusieurs filtres peuvent s'enchaîner.
     * Ordered.HIGHEST_PRECEDENCE = -2147483648 (la plus haute priorité).
     *
     * On veut que notre filtre JWT s'exécute EN PREMIER,
     * avant tous les autres filtres de Spring Cloud Gateway.
     * Ainsi, une requête non authentifiée est rejetée immédiatement,
     * sans même atteindre les filtres de routage.
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

  
}
