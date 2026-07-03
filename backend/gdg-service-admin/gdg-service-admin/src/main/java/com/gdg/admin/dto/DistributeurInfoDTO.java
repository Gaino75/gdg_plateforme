package com.gdg.admin.dto;

public class DistributeurInfoDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Long agenceId;

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public Long getAgenceId() { return agenceId; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setAgenceId(Long agenceId) { this.agenceId = agenceId; }
}
