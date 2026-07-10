package com.gdg.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * ================================================================
 * UTILITAIRE JWT — PROJET GPG
 * ================================================================
 *
 * Cette classe est responsable de TOUT ce qui concerne la lecture
 * et la validation des tokens JWT dans la Gateway.
 *
 * Rappel du cycle de vie d'un JWT dans GPG :
 *
 *   1. Le consommateur/distributeur/admin se connecte via POST /auth/login
 *   2. Le Service Auth vérifie email + mot de passe (bcrypt)
 *   3. Le Service Auth GÉNÈRE un token JWT signé avec la clé secrète
 *   4. Ce token est retourné au frontend
 *   5. Le frontend stocke ce token (localStorage ou state)
 *   6. À chaque requête suivante, le frontend envoie ce token dans le header :
 *        Authorization: Bearer eyJhbGci...
 *   7. LA GATEWAY intercepte la requête, extrait le token, et appelle cette classe
 *      pour vérifier si le token est valide et récupérer le rôle de l'utilisateur.
 *
 * Structure d'un JWT :
 *   [HEADER].[PAYLOAD].[SIGNATURE]
 *
 *   PAYLOAD (les données stockées dans le token) :
 *   {
 *     "sub": "42",                          ← l'ID de l'utilisateur
 *     "role": "DISTRIBUTEUR",               ← son rôle dans GPG
 *     "email": "dupont@total.cm",           ← son email
 *     "iat": 1700000000,                    ← quand le token a été créé
 *     "exp": 1700003600                     ← quand il expire (ici : +1 heure)
 *   }
 *
 * ================================================================
 */
@Component
public class JwtUtil {

    /**
     * La clé secrète définie dans application.yml (jwt.secret).
     * DOIT être identique à celle utilisée dans le Service Auth pour signer les tokens.
     * Sans cette correspondance, la validation échouerait systématiquement.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Convertit la clé secrète (String) en objet Key utilisable par JJWT.
     *
     * On utilise l'algorithme HMAC-SHA256 (HS256), qui est un algorithme
     * symétrique : la même clé sert à signer ET à vérifier.
     * C'est pourquoi Service Auth et Gateway partagent la même clé.
     *
     * @return un objet Key prêt à être utilisé par le parser JWT
     */
    private Key getSigningKey() {
        // Keys.hmacShaKeyFor convertit les bytes de notre clé texte
        // en une clé cryptographique compatible HMAC-SHA256
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Extrait et retourne TOUTES les informations (claims) contenues dans le token.
     *
     * Cette méthode fait deux choses en une :
     *   1. Elle VÉRIFIE la signature du token (si quelqu'un a modifié le token, ça échoue)
     *   2. Elle RETOURNE le payload (les données : userId, role, email, expiration...)
     *
     * Si le token est invalide, expiré ou mal formé, une exception est levée.
     *
     * @param token le JWT brut (sans le préfixe "Bearer ")
     * @return l'objet Claims contenant toutes les données du token
     * @throws ExpiredJwtException     si le token a expiré
     * @throws MalformedJwtException   si le token est mal formé
     * @throws SignatureException      si la signature ne correspond pas à notre clé
     */
    public Claims extraireTousClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())   // On lui dit avec quelle clé vérifier
                .build()
                .parseClaimsJws(token)             // Il parse ET vérifie la signature
                .getBody();                        // On récupère le payload
    }

    /**
     * Extrait l'identifiant de l'utilisateur (le "subject" du token).
     *
     * Dans le Service Auth, quand le token est généré, on fait :
     *   .setSubject(String.valueOf(utilisateur.getId()))
     * Donc le subject contient l'ID de l'utilisateur sous forme de String.
     *
     * @param token le JWT brut
     * @return l'ID de l'utilisateur sous forme de String (ex: "42")
     */
    public String extraireUserId(String token) {
        return extraireTousClaims(token).getSubject();
    }

    /**
     * Extrait le rôle de l'utilisateur depuis le token.
     *
     * Dans le Service Auth, quand le token est généré, on fait :
     *   .claim("role", utilisateur.getRole().name())
     * Donc la claim "role" contient : "CONSOMMATEUR", "DISTRIBUTEUR" ou "ADMIN"
     *
     * @param token le JWT brut
     * @return le rôle sous forme de String ("CONSOMMATEUR", "DISTRIBUTEUR", "ADMIN")
     */
    public String extraireRole(String token) {
        return (String) extraireTousClaims(token).get("role");
    }

    /**
     * Extrait l'email de l'utilisateur depuis le token.
     *
     * Utile pour enrichir les requêtes envoyées aux microservices
     * (on peut ajouter l'email dans un header pour que le microservice sache
     *  qui fait la requête sans avoir à décoder le token lui-même).
     *
     * @param token le JWT brut
     * @return l'email de l'utilisateur
     */
    public String extraireEmail(String token) {
        return (String) extraireTousClaims(token).get("email");
    }

    /**
     * Vérifie si le token est expiré.
     *
     * Un token JWT contient une date d'expiration ("exp").
     * Si la date actuelle est après cette date, le token est expiré
     * et l'utilisateur doit se reconnecter (ou utiliser /auth/refresh-token).
     *
     * @param token le JWT brut
     * @return true si le token est expiré, false s'il est encore valide
     */
    public boolean estExpire(String token) {
        Date expiration = extraireTousClaims(token).getExpiration();
        return expiration.before(new Date()); // true si la date d'expiration est dans le passé
    }

    /**
     * Validation complète du token JWT.
     *
     * Cette méthode est la méthode principale appelée par le filtre JWT.
     * Elle retourne true si le token est :
     *   - Bien formé (structure valide)
     *   - Signé avec notre clé secrète (non falsifié)
     *   - Pas encore expiré
     *
     * Elle retourne false (ou lève une exception) si n'importe laquelle
     * de ces conditions n'est pas respectée.
     *
     * @param token le JWT brut (sans "Bearer ")
     * @return true si le token est valide et utilisable
     */
    public boolean estValide(String token) {
        try {
            // extraireTousClaims va lever une exception si le token est invalide ou expiré
            Claims claims = extraireTousClaims(token);

            // Vérification supplémentaire : le token ne doit pas être expiré
            if (claims.getExpiration().before(new Date())) {
                return false; // Token expiré
            }

            return true; // Token valide !

        } catch (ExpiredJwtException e) {
            // Le token a dépassé sa date d'expiration
            System.err.println("[JWT] Token expiré : " + e.getMessage());
            return false;

        } catch (MalformedJwtException e) {
            // Le token ne respecte pas le format JWT standard
            System.err.println("[JWT] Token mal formé : " + e.getMessage());
            return false;

        } catch (SignatureException e) {
            // La signature du token ne correspond pas à notre clé secrète
            // => Quelqu'un a peut-être essayé de falsifier le token
            System.err.println("[JWT] Signature invalide : " + e.getMessage());
            return false;

        } catch (UnsupportedJwtException e) {
            // Type de JWT non supporté
            System.err.println("[JWT] Type de token non supporté : " + e.getMessage());
            return false;

        } catch (IllegalArgumentException e) {
            // Token vide ou null
            System.err.println("[JWT] Token vide ou null : " + e.getMessage());
            return false;
        }
    }
}
