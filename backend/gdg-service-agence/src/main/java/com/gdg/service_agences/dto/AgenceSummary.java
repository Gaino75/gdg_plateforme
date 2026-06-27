package com.gdg.service_agences.dto;

import com.gdg.service_agences.model.Agence.StatutAgence;

/**
 * DTO pour l'affichage résumé des agences (liste)
 */
public class AgenceSummary {

    private Long id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private StatutAgence statut;
    private String nomEnseigne;
    private String nomVille;
    private String region;

    // Constructeurs
    public AgenceSummary() {}

    public AgenceSummary(Long id, String nom, String adresse, String telephone,
                         String email, StatutAgence statut, String nomEnseigne,
                         String nomVille, String region) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.statut = statut;
        this.nomEnseigne = nomEnseigne;
        this.nomVille = nomVille;
        this.region = region;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public StatutAgence getStatut() { return statut; }
    public void setStatut(StatutAgence statut) { this.statut = statut; }
    public String getNomEnseigne() { return nomEnseigne; }
    public void setNomEnseigne(String nomEnseigne) { this.nomEnseigne = nomEnseigne; }
    public String getNomVille() { return nomVille; }
    public void setNomVille(String nomVille) { this.nomVille = nomVille; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
}