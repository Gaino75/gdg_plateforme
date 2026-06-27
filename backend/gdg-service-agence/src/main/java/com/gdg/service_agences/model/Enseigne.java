package com.gdg.service_agences.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "enseigne", schema = "agences_schema")
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

    @OneToMany(mappedBy = "enseigne", cascade = CascadeType.ALL)
    private List<Agence> agences;

    public enum StatutEnseigne {
        ACTIF, INACTIF
    }

    public Enseigne() {}

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

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSiteWeb() { return siteWeb; }
    public void setSiteWeb(String siteWeb) { this.siteWeb = siteWeb; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmailContact() { return emailContact; }
    public void setEmailContact(String emailContact) { this.emailContact = emailContact; }

    public StatutEnseigne getStatut() { return statut; }
    public void setStatut(StatutEnseigne statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public List<Agence> getAgences() { return agences; }
    public void setAgences(List<Agence> agences) { this.agences = agences; }
}