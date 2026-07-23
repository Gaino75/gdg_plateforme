package com.gdg.auth.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "consommateur", schema = "auth_schema")
@DiscriminatorValue("CONSOMMATEUR")
public class Consommateur extends Utilisateur {

    @Column(name = "ville_residence", length = 100)
    private String villeResidence;

    // Date de naissance pour vérifier majorité
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    // GETTERS
    public String getVilleResidence() { return villeResidence; }
    public LocalDate getDateNaissance() { return dateNaissance; }

    // SETTERS
    public void setVilleResidence(String villeResidence) {
        this.villeResidence = villeResidence;
    }
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}