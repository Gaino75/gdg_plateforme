package com.gdg.service_reservations.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation", schema = "reservations_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_reservation", nullable = false, unique = true, length = 50)
    private String referenceReservation;

    @Column(name = "agence_id", nullable = false)
    private Long agenceId;

    @Column(name = "consommateur_id", nullable = false)
    private Long consommateurId;

    @Column(name = "categorie_produit_id", nullable = false)
    private Long categorieProduitId;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @Column(name = "montant_total", nullable = false)
    private Double montantTotal;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatutReservation statut = StatutReservation.EN_ATTENTE;

    @Column(name = "mode_paiement", length = 20)
    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    @Column(name = "reference_paiement", length = 150)
    private String referencePaiement;

    @Column(name = "date_reservation", nullable = false)
    private LocalDateTime dateReservation = LocalDateTime.now();

    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration = LocalDateTime.now().plusMinutes(30);

    @Column(name = "date_confirmation")
    private LocalDateTime dateConfirmation;

    @Column(name = "date_recuperation")
    private LocalDateTime dateRecuperation;

    @Column(name = "motif_annulation", columnDefinition = "TEXT")
    private String motifAnnulation;

    @Column(columnDefinition = "TEXT")
    private String observations;

    // Constructeur pratique
    public Reservation(String referenceReservation, Long agenceId, Long consommateurId,
                       Long categorieProduitId, Integer quantite, Double prixUnitaire,
                       Double montantTotal, ModePaiement modePaiement) {
        this.referenceReservation = referenceReservation;
        this.agenceId = agenceId;
        this.consommateurId = consommateurId;
        this.categorieProduitId = categorieProduitId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.montantTotal = montantTotal;
        this.modePaiement = modePaiement;
        this.statut = StatutReservation.EN_ATTENTE;
        this.dateReservation = LocalDateTime.now();
        this.dateExpiration = LocalDateTime.now().plusMinutes(30);
    }

    // Enums
    public enum StatutReservation {
        EN_ATTENTE,   // créée, paiement pas encore fait
        PAYEE,        // paiement confirmé
        CONFIRMEE,    // distributeur a confirmé la disponibilité
        ANNULEE,      // annulée par client ou système
        EXPIREE,      // délai dépassé sans paiement
        RECUPEREE     // client est venu récupérer son gaz
    }

    public enum ModePaiement {
        CASH, ORANGE_MONEY, MTN_MOBILE_MONEY
    }
}