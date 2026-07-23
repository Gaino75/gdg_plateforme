package com.gdg.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "administrateur", schema = "auth_schema")
@DiscriminatorValue("ADMIN")
public class Administrateur extends Utilisateur {

    // STANDARD = admin normal
    // SUPER_ADMIN = peut tout faire
    @Column(name = "niveau_acces", length = 20)
    private String niveauAcces = "STANDARD";

    // GETTER
    public String getNiveauAcces() { return niveauAcces; }

    // SETTER
    public void setNiveauAcces(String niveauAcces) {
        this.niveauAcces = niveauAcces;
    }
}