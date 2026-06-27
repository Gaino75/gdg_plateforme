package com.gdg.service_agences.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la création d'une enseigne
 */
public class EnseigneRequest {

    @NotBlank(message = "Le nom de l'enseigne est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;

    private String logo;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;

    @Size(max = 255, message = "L'URL du site web ne doit pas dépasser 255 caractères")
    private String siteWeb;

    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    private String telephone;

    @Size(max = 150, message = "L'email ne doit pas dépasser 150 caractères")
    private String emailContact;

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getEmailContact() { return emailContact; }
    public void setEmailContact(String emailContact) { this.emailContact = emailContact; }
}