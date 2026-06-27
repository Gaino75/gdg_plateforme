package com.gdg.service_agences.dto;

import jakarta.validation.constraints.Size;

/**
 * DTO pour la mise à jour d'une agence
 */
public class AgenceUpdateRequest {

    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères")
    private String nom;

    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    private String adresse;

    private Double latitude;
    private Double longitude;

    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    private String telephone;

    @Size(max = 150, message = "L'email ne doit pas dépasser 150 caractères")
    private String email;

    private String logoFacture;
    private String enteteFacture;
    private String piedFacture;

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLogoFacture() { return logoFacture; }
    public void setLogoFacture(String logoFacture) { this.logoFacture = logoFacture; }
    public String getEnteteFacture() { return enteteFacture; }
    public void setEnteteFacture(String enteteFacture) { this.enteteFacture = enteteFacture; }
    public String getPiedFacture() { return piedFacture; }
    public void setPiedFacture(String piedFacture) { this.piedFacture = piedFacture; }
}