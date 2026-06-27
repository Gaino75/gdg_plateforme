package com.gdg.service_agences.dto;

import com.gdg.service_agences.model.Enseigne.StatutEnseigne;

/**
 * DTO pour l'affichage résumé des enseignes (liste)
 */
public class EnseigneSummary {

    private Long id;
    private String nom;
    private String logo;
    private StatutEnseigne statut;
    private Long nombreAgences;

    // Constructeurs
    public EnseigneSummary() {}

    public EnseigneSummary(Long id, String nom, String logo, StatutEnseigne statut, Long nombreAgences) {
        this.id = id;
        this.nom = nom;
        this.logo = logo;
        this.statut = statut;
        this.nombreAgences = nombreAgences;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public StatutEnseigne getStatut() { return statut; }
    public void setStatut(StatutEnseigne statut) { this.statut = statut; }
    public Long getNombreAgences() { return nombreAgences; }
    public void setNombreAgences(Long nombreAgences) { this.nombreAgences = nombreAgences; }
}