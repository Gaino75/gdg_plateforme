package com.gdg.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgenceSummaryResponse {
    private Long id;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private String statut;
    private String nomEnseigne;
    private String nomVille;

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getAdresse() { return adresse; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getStatut() { return statut; }
    public String getNomEnseigne() { return nomEnseigne; }
    public String getNomVille() { return nomVille; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setEmail(String email) { this.email = email; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setNomEnseigne(String nomEnseigne) { this.nomEnseigne = nomEnseigne; }
    public void setNomVille(String nomVille) { this.nomVille = nomVille; }
}
