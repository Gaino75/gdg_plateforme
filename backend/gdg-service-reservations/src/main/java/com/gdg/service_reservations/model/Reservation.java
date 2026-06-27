package com.gdg.service_reservations.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation", schema = "reservations_schema")
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

    public enum StatutReservation {
        EN_ATTENTE, PAYEE, CONFIRMEE, ANNULEE, EXPIREE, RECUPEREE
    }

    public enum ModePaiement {
        CASH, ORANGE_MONEY, MTN_MOBILE_MONEY
    }

    // ============================================================
    // CONSTRUCTEURS
    // ============================================================

    public Reservation() {}

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

    // ============================================================
    // GETTERS ET SETTERS
    // ============================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReferenceReservation() { return referenceReservation; }
    public void setReferenceReservation(String referenceReservation) { this.referenceReservation = referenceReservation; }

    public Long getAgenceId() { return agenceId; }
    public void setAgenceId(Long agenceId) { this.agenceId = agenceId; }

    public Long getConsommateurId() { return consommateurId; }
    public void setConsommateurId(Long consommateurId) { this.consommateurId = consommateurId; }

    public Long getCategorieProduitId() { return categorieProduitId; }
    public void setCategorieProduitId(Long categorieProduitId) { this.categorieProduitId = categorieProduitId; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public Double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public Double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(Double montantTotal) { this.montantTotal = montantTotal; }

    public StatutReservation getStatut() { return statut; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }

    public ModePaiement getModePaiement() { return modePaiement; }
    public void setModePaiement(ModePaiement modePaiement) { this.modePaiement = modePaiement; }

    public String getReferencePaiement() { return referencePaiement; }
    public void setReferencePaiement(String referencePaiement) { this.referencePaiement = referencePaiement; }

    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }

    public LocalDateTime getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDateTime dateExpiration) { this.dateExpiration = dateExpiration; }

    public LocalDateTime getDateConfirmation() { return dateConfirmation; }
    public void setDateConfirmation(LocalDateTime dateConfirmation) { this.dateConfirmation = dateConfirmation; }

    public LocalDateTime getDateRecuperation() { return dateRecuperation; }
    public void setDateRecuperation(LocalDateTime dateRecuperation) { this.dateRecuperation = dateRecuperation; }

    public String getMotifAnnulation() { return motifAnnulation; }
    public void setMotifAnnulation(String motifAnnulation) { this.motifAnnulation = motifAnnulation; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}