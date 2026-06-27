package com.gdg.service_agences.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ville", schema = "agences_schema")
public class Ville {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String region;

    @Column(nullable = false, length = 100)
    private String pays = "Cameroun";

    @OneToMany(mappedBy = "ville")
    private List<Agence> agences;

    public Ville() {}

    public Ville(String nom, String region, String pays) {
        this.nom = nom;
        this.region = region;
        this.pays = pays != null ? pays : "Cameroun";
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    public List<Agence> getAgences() { return agences; }
    public void setAgences(List<Agence> agences) { this.agences = agences; }
}