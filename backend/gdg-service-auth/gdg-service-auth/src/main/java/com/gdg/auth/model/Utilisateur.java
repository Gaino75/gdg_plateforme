
package com.gdg.auth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Table(name = "utilisateur", schema = "auth_schema")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role",discriminatorType=DiscriminatorType.STRING)

@JsonTypeInfo(use =JsonTypeInfo.Id.NAME, property = "role", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Consommateur.class, name = "CONSOMMATEUR"),
    @JsonSubTypes.Type(value = Distributeur.class, name = "DISTRIBUTEUR"),
    @JsonSubTypes.Type(value = Administrateur.class, name = "ADMIN"),
})

public abstract class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(length = 20)
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20, insertable = false, updatable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Statut statut = Statut.ACTIF;

    @Column(name = "date_inscription", nullable = false)
    private LocalDateTime dateInscription = LocalDateTime.now();

    // Vérification email
    @Column(name = "email_verifie", nullable = false)
    private Boolean emailVerifie = false;

    // Token envoyé par email pour vérification
    @Column(name = "token_verification", length = 255)
    private String tokenVerification;

    // Date d'expiration du token de vérification
    @Column(name = "date_expiration_token")
    private LocalDateTime dateExpirationToken;

    // GETTERS
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public String getTelephone() { return telephone; }
    public Role getRole() { return role; }
    public Statut getStatut() { return statut; }
    public LocalDateTime getDateInscription() { return dateInscription; }
    public Boolean getEmailVerifie() { return emailVerifie; }
    public String getTokenVerification() { return tokenVerification; }
    public LocalDateTime getDateExpirationToken() { return dateExpirationToken; }

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setRole(Role role) { this.role = role; }
    public void setStatut(Statut statut) { this.statut = statut; }
    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }
    public void setEmailVerifie(Boolean emailVerifie) { this.emailVerifie = emailVerifie; }
    public void setTokenVerification(String tokenVerification) { this.tokenVerification = tokenVerification; }
    public void setDateExpirationToken(LocalDateTime dateExpirationToken) { this.dateExpirationToken = dateExpirationToken; }

    public enum Role {
        CONSOMMATEUR, DISTRIBUTEUR, ADMIN
    }

    public enum Statut {
        ACTIF, INACTIF, SUSPENDU
    }
}
