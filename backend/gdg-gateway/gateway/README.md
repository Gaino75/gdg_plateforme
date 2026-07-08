# API GATEWAY — PROJET GPG
## Guide d'intégration et de démarrage

---

## Structure du projet

```
api-gateway/
├── pom.xml
└── src/main/
    ├── java/com/gdg/gateway/
    │   ├── ApiGatewayApplication.java       ← Point d'entrée
    │   ├── config/
    │   │   ├── CorsConfig.java              ← Configuration CORS (remplace @CrossOrigin)
    │   │   └── RoutesOuvertesConfig.java    ← Routes publiques (sans JWT)
    │   ├── filter/
    │   │   ├── JwtAuthFilter.java           ← Filtre JWT principal (RBAC)
    │   │   └── LoggingFilter.java           ← Trace toutes les requêtes
    │   └── util/
    │       └── JwtUtil.java                 ← Lecture et validation des JWT
    └── resources/
        └── application.yml                  ← Routes et configuration
```

---

## Démarrage

```bash
# Cloner le projet et aller dans le dossier
cd api-gateway

# Lancer la Gateway (port 8080)
mvn spring-boot:run
```

**Ordre de démarrage recommandé :**
1. Service Auth (8081) — en premier car les autres en dépendent
2. Service Agences (8082)
3. Service Stock (8083)
4. Service Ventes (8084)
5. Service Réservations (8085)
6. Service Paiement (8086)
7. Service Notifications (8087)
8. Service Admin (8088)
9. **API Gateway (8080)** — en dernier (elle route vers les autres)

---

## Modifications à faire dans les microservices EXISTANTS

### 1. Supprimer @CrossOrigin de tous les controllers
La Gateway gère maintenant le CORS. Supprimer partout :
```java
// SUPPRIMER CES LIGNES dans tous les controllers
@CrossOrigin(origins = "*")
```

### 2. Corriger le SecurityConfig de chaque microservice
Remplacer le `permitAll()` dangereux par une vraie protection.
Voici le SecurityConfig à utiliser dans chaque microservice métier :

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // OK car on utilise JWT (stateless)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Autoriser uniquement les requêtes venant de la Gateway
                // La Gateway ajoute le header X-User-Id sur les requêtes authentifiées
                // Si ce header est absent, la requête ne vient pas de la Gateway
                .requestMatchers(request ->
                    request.getHeader("X-User-Id") != null).permitAll()
                // Bloquer tout le reste (appels directs sans passer par la Gateway)
                .anyRequest().denyAll()
            );
        return http.build();
    }
}
```

### 3. Corriger le Service Admin — supprimer X-Admin-Id
Dans `AdminService.java`, remplacer le header `X-Admin-Id` non vérifié
par le header `X-User-Id` injecté par la Gateway :

```java
// AVANT (dangereux — le client peut mettre n'importe quel ID) :
@GetMapping("/dashboard")
public ResponseEntity<?> getDashboard(
        @RequestHeader("X-Admin-Id") Long adminId) { ... }

// APRÈS (sécurisé — l'ID vient de la Gateway qui a vérifié le JWT) :
@GetMapping("/dashboard")
public ResponseEntity<?> getDashboard(
        @RequestHeader("X-User-Id") Long adminId,
        @RequestHeader("X-User-Role") String role) {
    // Double vérification (optionnelle, la Gateway l'a déjà fait)
    if (!"ADMIN".equals(role)) {
        return ResponseEntity.status(403).body("Accès refusé");
    }
    // ...
}
```

### 4. Clé JWT identique
La clé dans `application.yml` de la Gateway DOIT être la même
que dans le Service Auth :

```yaml
# Gateway — application.yml
jwt:
  secret: gpg-secret-key-2025-projet-academique-cameroun-dschang-bafoussam

# Service Auth — application.properties
jwt.secret=gpg-secret-key-2025-projet-academique-cameroun-dschang-bafoussam
```

---

## Comment le frontend doit appeler la Gateway

```javascript
// AVANT (appelait directement chaque microservice)
const response = await fetch("http://localhost:8081/auth/login", {...})
const response = await fetch("http://localhost:8082/api/agences", {...})

// APRÈS (tout passe par la Gateway sur le port 8080)
const response = await fetch("http://localhost:8080/auth/login", {...})
const response = await fetch("http://localhost:8080/api/agences", {...})

// Pour les routes protégées, ajouter le token JWT dans le header
const response = await fetch("http://localhost:8080/admin/dashboard", {
    headers: {
        "Authorization": "Bearer " + localStorage.getItem("token"),
        "Content-Type": "application/json"
    }
})
```

---

## Codes d'erreur retournés par la Gateway

| Code | Signification | Cause |
|------|--------------|-------|
| 401  | Non authentifié | Pas de token JWT, ou token invalide/expiré |
| 403  | Accès refusé | Token valide mais rôle insuffisant |
| 200-299 | Succès | La requête a passé tous les filtres |

---

## Règles de gestion respectées par la Gateway

| Règle | Description | Implémentation |
|-------|-------------|----------------|
| RG-08 | Visiteur voit dispo globale sans détail | `/stock/agence/*/dispo` en route ouverte |
| RG-13 | Agence visible seulement après validation admin | `/admin/**` réservé au rôle ADMIN |
| RG-14 | Admin suspend/supprime n'importe quand | RBAC sur `/admin/**` |
| NFR Sécurité | JWT + RBAC | JwtAuthFilter.java |
| NFR Sécurité | CORS contrôlé | CorsConfig.java (plus de `origins = "*"`) |
