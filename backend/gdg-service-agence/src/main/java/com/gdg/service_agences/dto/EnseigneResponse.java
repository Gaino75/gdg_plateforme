package com.gdg.service_agences.dto;

import com.gdg.service_agences.model.Enseigne.StatutEnseigne;

import java.time.LocalDateTime;

/**
 * DTO pour l'affichage détaillé d'une enseigne
 */
public class EnseigneResponse {

    private Long id;
    private String nom;
    private String logo;
    private String description;
    private String siteWeb;
    private String telephone;
    private String emailContact;
    private StatutEnseigne statut;
    private LocalDateTime dateCreation;
    private Long nombreAgences;  // Pour information supplémentaire

    // Constructeur privé (utilise le builder)
    private EnseigneResponse() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final EnseigneResponse dto = new EnseigneResponse();

        public Builder id(Long id) { dto.id = id; return this; }
        public Builder nom(String nom) { dto.nom = nom; return this; }
        public Builder logo(String logo) { dto.logo = logo; return this; }
        public Builder description(String description) { dto.description = description; return this; }
        public Builder siteWeb(String siteWeb) { dto.siteWeb = siteWeb; return this; }
        public Builder telephone(String telephone) { dto.telephone = telephone; return this; }
        public Builder emailContact(String emailContact) { dto.emailContact = emailContact; return this; }
        public Builder statut(StatutEnseigne statut) { dto.statut = statut; return this; }
        public Builder dateCreation(LocalDateTime dateCreation) { dto.dateCreation = dateCreation; return this; }
        public Builder nombreAgences(Long nombreAgences) { dto.nombreAgences = nombreAgences; return this; }

        public EnseigneResponse build() {
            return dto;
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getLogo() { return logo; }
    public String getDescription() { return description; }
    public String getSiteWeb() { return siteWeb; }
    public String getTelephone() { return telephone; }
    public String getEmailContact() { return emailContact; }
    public StatutEnseigne getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public Long getNombreAgences() { return nombreAgences; }
}