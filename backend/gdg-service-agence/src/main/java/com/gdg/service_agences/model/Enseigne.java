package com.gdg.service_agences.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "enseigne", schema = "agences_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enseigne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @Column(length = 255)
    private String logo;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "site_web", length = 255)
    private String siteWeb;

    @Column(length = 20)
    private String telephone;

    @Column(name = "email_contact", length = 150)
    private String emailContact;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutEnseigne statut = StatutEnseigne.ACTIF;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    // Relation : une enseigne a plusieurs agences
    @OneToMany(mappedBy = "enseigne", cascade = CascadeType.ALL)
    private List<Agence> agences;

    public enum StatutEnseigne {
        ACTIF, INACTIF
    }

    // Constructeur pratique
    public Enseigne(String nom, String logo, String description, String siteWeb, String telephone, String emailContact) {
        this.nom = nom;
        this.logo = logo;
        this.description = description;
        this.siteWeb = siteWeb;
        this.telephone = telephone;
        this.emailContact = emailContact;
        this.statut = StatutEnseigne.ACTIF;
        this.dateCreation = LocalDateTime.now();
    }
}