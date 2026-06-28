package com.gdg.admin.dto;

public class AgenceDTO {
    private Long id;
    private String nom;
    private String adresse;
    private String statut;
    private Long enseigneId;
    private Long villeId;
    private String telephone;

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getAdresse() { return adresse; }
    public String getStatut() { return statut; }
    public Long getEnseigneId() { return enseigneId; }
    public Long getVilleId() { return villeId; }
    public String getTelephone() { return telephone; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setEnseigneId(Long enseigneId) { this.enseigneId = enseigneId; }
    public void setVilleId(Long villeId) { this.villeId = villeId; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}
