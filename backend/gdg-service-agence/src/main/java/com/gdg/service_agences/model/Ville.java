package com.gdg.service_agences.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "ville", schema = "agences_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    // Relation : une ville a plusieurs agences
    @OneToMany(mappedBy = "ville")
    private List<Agence> agences;

    // Constructeur pratique
    public Ville(String nom, String region, String pays) {
        this.nom = nom;
        this.region = region;
        this.pays = pays != null ? pays : "Cameroun";
    }
}