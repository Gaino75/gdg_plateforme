package com.gdg.admin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parametre_plateforme", schema = "admin_schema")
public class ParametrePlateforme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Clé unique ex: DUREE_EXPIRATION_RESERVATION
    @Column(nullable = false, unique = true)
    private String cle;

    // Valeur ex: "30"
    @Column(nullable = false, columnDefinition = "TEXT")
    private String valeur;

    // Description du paramètre
    @Column(columnDefinition = "TEXT")
    private String description;

    // ID admin qui a modifié
    @Column(name = "modifie_par")
    private Long modifiePar;

    // Date dernière modification
    @Column(name = "date_modification", nullable = false)
    private LocalDateTime dateModification = LocalDateTime.now();

    // GETTERS
    public Long getId() { return id; }
    public String getCle() { return cle; }
    public String getValeur() { return valeur; }
    public String getDescription() { return description; }
    public Long getModifiePar() { return modifiePar; }
    public LocalDateTime getDateModification() { return dateModification; }

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setCle(String cle) { this.cle = cle; }
    public void setValeur(String valeur) { this.valeur = valeur; }
    public void setDescription(String description) { this.description = description; }
    public void setModifiePar(Long modifiePar) { this.modifiePar = modifiePar; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }
}