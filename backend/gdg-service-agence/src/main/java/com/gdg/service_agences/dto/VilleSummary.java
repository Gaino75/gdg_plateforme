package com.gdg.service_agences.dto;

/**
 * DTO pour l'affichage résumé des villes (liste)
 */
public class VilleSummary {

    private Long id;
    private String nom;
    private String region;
    private String pays;

    // Constructeurs
    public VilleSummary() {}

    public VilleSummary(Long id, String nom, String region, String pays) {
        this.id = id;
        this.nom = nom;
        this.region = region;
        this.pays = pays;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
}