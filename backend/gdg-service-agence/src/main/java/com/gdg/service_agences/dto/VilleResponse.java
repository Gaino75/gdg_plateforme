package com.gdg.service_agences.dto;

/**
 * DTO pour l'affichage détaillé d'une ville
 */
public class VilleResponse {

    private Long id;
    private String nom;
    private String region;
    private String pays;
    private Long nombreAgences;

    // Constructeur privé
    private VilleResponse() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final VilleResponse dto = new VilleResponse();

        public Builder id(Long id) { dto.id = id; return this; }
        public Builder nom(String nom) { dto.nom = nom; return this; }
        public Builder region(String region) { dto.region = region; return this; }
        public Builder pays(String pays) { dto.pays = pays; return this; }
        public Builder nombreAgences(Long nombreAgences) { dto.nombreAgences = nombreAgences; return this; }

        public VilleResponse build() {
            return dto;
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getRegion() { return region; }
    public String getPays() { return pays; }
    public Long getNombreAgences() { return nombreAgences; }
}