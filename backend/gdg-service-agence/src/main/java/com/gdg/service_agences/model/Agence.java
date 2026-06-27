package com.gdg.service_agences.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agence", schema = "agences_schema")
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(nullable = false, length = 255)
    private String adresse;

    private Double latitude;
    private Double longitude;

    @Column(length = 20)
    private String telephone;

    @Column(length = 150)
    private String email;

    @Column(name = "logo_facture", length = 255)
    private String logoFacture;

    @Column(name = "entete_facture", columnDefinition = "TEXT")
    private String enteteFacture;

    @Column(name = "pied_facture", columnDefinition = "TEXT")
    private String piedFacture;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutAgence statut = StatutAgence.EN_ATTENTE;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @Column(name = "valide_par")
    private Long validePar;

    @ManyToOne
    @JoinColumn(name = "enseigne_id", nullable = false)
    private Enseigne enseigne;

    @ManyToOne
    @JoinColumn(name = "ville_id", nullable = false)
    private Ville ville;

    @OneToMany(mappedBy = "agence", cascade = CascadeType.ALL)
    private List<HoraireOuverture> horaires;

    public enum StatutAgence {
        EN_ATTENTE, ACTIF, SUSPENDU
    }

    // Constructeur par défaut (obligatoire pour JPA)
    public Agence() {}

    // Constructeur pratique
    public Agence(String nom, String adresse, Enseigne enseigne, Ville ville, String telephone, String email) {
        this.nom = nom;
        this.adresse = adresse;
        this.enseigne = enseigne;
        this.ville = ville;
        this.telephone = telephone;
        this.email = email;
        this.statut = StatutAgence.EN_ATTENTE;
        this.dateCreation = LocalDateTime.now();
    }

    // ============================================================
    // GETTERS ET SETTERS
    // ============================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLogoFacture() { return logoFacture; }
    public void setLogoFacture(String logoFacture) { this.logoFacture = logoFacture; }

    public String getEnteteFacture() { return enteteFacture; }
    public void setEnteteFacture(String enteteFacture) { this.enteteFacture = enteteFacture; }

    public String getPiedFacture() { return piedFacture; }
    public void setPiedFacture(String piedFacture) { this.piedFacture = piedFacture; }

    public StatutAgence getStatut() { return statut; }
    public void setStatut(StatutAgence statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }

    public Long getValidePar() { return validePar; }
    public void setValidePar(Long validePar) { this.validePar = validePar; }

    public Enseigne getEnseigne() { return enseigne; }
    public void setEnseigne(Enseigne enseigne) { this.enseigne = enseigne; }

    public Ville getVille() { return ville; }
    public void setVille(Ville ville) { this.ville = ville; }

    public List<HoraireOuverture> getHoraires() { return horaires; }
    public void setHoraires(List<HoraireOuverture> horaires) { this.horaires = horaires; }
}