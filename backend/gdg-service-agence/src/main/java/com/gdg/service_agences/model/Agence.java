package com.gdg.service_agences.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agence", schema = "agences_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private Long validePar;  // ID de l'admin qui a validé

    // Relations
    @ManyToOne
    @JoinColumn(name = "enseigne_id", nullable = false)
    private Enseigne enseigne;

    @ManyToOne
    @JoinColumn(name = "ville_id", nullable = false)
    private Ville ville;

    // Relation : une agence a plusieurs horaires
    @OneToMany(mappedBy = "agence", cascade = CascadeType.ALL)
    private List<HoraireOuverture> horaires;

    public enum StatutAgence {
        EN_ATTENTE, ACTIF, SUSPENDU
    }

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
}