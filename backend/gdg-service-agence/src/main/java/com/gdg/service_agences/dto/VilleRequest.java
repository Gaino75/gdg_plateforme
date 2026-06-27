package com.gdg.service_agences.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la création d'une ville
 */
public class VilleRequest {

    @NotBlank(message = "Le nom de la ville est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;

    @NotBlank(message = "La région est obligatoire")
    @Size(max = 100, message = "La région ne doit pas dépasser 100 caractères")
    private String region;

    @Size(max = 100, message = "Le pays ne doit pas dépasser 100 caractères")
    private String pays = "Cameroun";

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
}