package com.gdg.service_reservations.dto;

import com.gdg.service_reservations.model.Reservation.ModePaiement;
import com.gdg.service_reservations.model.Reservation.StatutReservation;

import java.time.LocalDateTime;

/**
 * DTO pour l'affichage résumé (liste de réservations)
 * Plus léger que ReservationResponse
 */
public class ReservationSummary {

    private Long id;
    private String referenceReservation;
    private Long agenceId;
    private String nomAgence;  // On pourrait ajouter via un appel au service Agences
    private Long categorieProduitId;
    private Integer quantite;
    private Double montantTotal;
    private StatutReservation statut;
    private ModePaiement modePaiement;
    private LocalDateTime dateReservation;
    private LocalDateTime dateExpiration;

    // Constructeurs
    public ReservationSummary() {}

    public ReservationSummary(Long id, String referenceReservation, Long agenceId, Long categorieProduitId,
                              Integer quantite, Double montantTotal, StatutReservation statut,
                              ModePaiement modePaiement, LocalDateTime dateReservation,
                              LocalDateTime dateExpiration) {
        this.id = id;
        this.referenceReservation = referenceReservation;
        this.agenceId = agenceId;
        this.categorieProduitId = categorieProduitId;
        this.quantite = quantite;
        this.montantTotal = montantTotal;
        this.statut = statut;
        this.modePaiement = modePaiement;
        this.dateReservation = dateReservation;
        this.dateExpiration = dateExpiration;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReferenceReservation() { return referenceReservation; }
    public void setReferenceReservation(String referenceReservation) { this.referenceReservation = referenceReservation; }
    public Long getAgenceId() { return agenceId; }
    public void setAgenceId(Long agenceId) { this.agenceId = agenceId; }
    public String getNomAgence() { return nomAgence; }
    public void setNomAgence(String nomAgence) { this.nomAgence = nomAgence; }
    public Long getCategorieProduitId() { return categorieProduitId; }
    public void setCategorieProduitId(Long categorieProduitId) { this.categorieProduitId = categorieProduitId; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public Double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(Double montantTotal) { this.montantTotal = montantTotal; }
    public StatutReservation getStatut() { return statut; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }
    public ModePaiement getModePaiement() { return modePaiement; }
    public void setModePaiement(ModePaiement modePaiement) { this.modePaiement = modePaiement; }
    public LocalDateTime getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDateTime dateReservation) { this.dateReservation = dateReservation; }
    public LocalDateTime getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDateTime dateExpiration) { this.dateExpiration = dateExpiration; }
}