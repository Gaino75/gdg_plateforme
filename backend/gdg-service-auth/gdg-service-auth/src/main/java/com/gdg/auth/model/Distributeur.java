package com.gdg.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "distributeur", schema = "auth_schema")
public class Distributeur extends Utilisateur {

    // Rempli après validation de l'agence par l'admin
    @Column(name = "agence_id")
    private Long agenceId;

    // Rôle dans l'agence : Gérant, Caissier, Agent...
    @Column(length = 100)
    private String poste;

    // GETTERS
    public Long getAgenceId() { return agenceId; }
    public String getPoste() { return poste; }

    // SETTERS
    public void setAgenceId(Long agenceId) { this.agenceId = agenceId; }
    public void setPoste(String poste) { this.poste = poste; }
}