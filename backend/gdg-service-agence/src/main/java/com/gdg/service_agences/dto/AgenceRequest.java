package com.gdg.service_agences.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO pour la création d'une agence
 */
public class AgenceRequest {

    @NotBlank(message = "Le nom de l'agence est obligatoire")
    @Size(max = 150, message = "Le nom ne doit pas dépasser 150 caractères")
    private String nom;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    private String adresse;

    private Double latitude;
    private Double longitude;

    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    private String telephone;

    @Size(max = 150, message = "L'email ne doit pas dépasser 150 caractères")
    private String email;

    @NotNull(message = "L'ID de l'enseigne est obligatoire")
    private Long enseigneId;

    @NotNull(message = "L'ID de la ville est obligatoire")
    private Long villeId;

    // Horaires d'ouverture (optionnel)
    private List<HoraireOuvertureDTO> horaires;

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
    public Long getEnseigneId() { return enseigneId; }
    public void setEnseigneId(Long enseigneId) { this.enseigneId = enseigneId; }
    public Long getVilleId() { return villeId; }
    public void setVilleId(Long villeId) { this.villeId = villeId; }
    public List<HoraireOuvertureDTO> getHoraires() { return horaires; }
    public void setHoraires(List<HoraireOuvertureDTO> horaires) { this.horaires = horaires; }
}