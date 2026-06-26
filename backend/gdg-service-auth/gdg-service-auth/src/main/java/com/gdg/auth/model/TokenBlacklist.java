package com.gdg.auth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_blacklist", schema = "auth_schema")
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Le token JWT révoqué
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;

    // L'utilisateur qui s'est déconnecté
    @Column(name = "utilisateur_id", nullable = false)
    private Long utilisateurId;

    // Date de déconnexion
    @Column(name = "date_revocation", nullable = false)
    private LocalDateTime dateRevocation = LocalDateTime.now();

    // Raison : DECONNEXION, SUSPENSION, EXPIRATION...
    @Column(length = 100)
    private String raison;

    // GETTERS
    public Long getId() { return id; }
    public String getToken() { return token; }
    public Long getUtilisateurId() { return utilisateurId; }
    public LocalDateTime getDateRevocation() { return dateRevocation; }
    public String getRaison() { return raison; }

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setToken(String token) { this.token = token; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }
    public void setDateRevocation(LocalDateTime dateRevocation) { this.dateRevocation = dateRevocation; }
    public void setRaison(String raison) { this.raison = raison; }
}