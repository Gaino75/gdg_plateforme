package com.gdg.service_reservations.dto;

import com.gdg.service_reservations.model.Reservation.ModePaiement;
import com.gdg.service_reservations.model.Reservation.StatutReservation;

import java.time.LocalDateTime;

/**
 * DTO pour l'affichage détaillé d'une réservation
 * Ce que le serveur renvoie au client
 */
public class ReservationResponse {

    private Long id;
    private String referenceReservation;
    private Long agenceId;
    private Long consommateurId;
    private Long categorieProduitId;
    private Integer quantite;
    private Double prixUnitaire;
    private Double montantTotal;
    private StatutReservation statut;
    private ModePaiement modePaiement;
    private String referencePaiement;
    private LocalDateTime dateReservation;
    private LocalDateTime dateExpiration;
    private LocalDateTime dateConfirmation;
    private LocalDateTime dateRecuperation;
    private String motifAnnulation;
    private String observations;

    // Constructeur privé (utilise le builder)
    private ReservationResponse() {}

    // Builder pattern pour une construction fluide
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ReservationResponse dto = new ReservationResponse();

        public Builder id(Long id) { dto.id = id; return this; }
        public Builder referenceReservation(String reference) { dto.referenceReservation = reference; return this; }
        public Builder agenceId(Long agenceId) { dto.agenceId = agenceId; return this; }
        public Builder consommateurId(Long consommateurId) { dto.consommateurId = consommateurId; return this; }
        public Builder categorieProduitId(Long id) { dto.categorieProduitId = id; return this; }
        public Builder quantite(Integer quantite) { dto.quantite = quantite; return this; }
        public Builder prixUnitaire(Double prix) { dto.prixUnitaire = prix; return this; }
        public Builder montantTotal(Double montant) { dto.montantTotal = montant; return this; }
        public Builder statut(StatutReservation statut) { dto.statut = statut; return this; }
        public Builder modePaiement(ModePaiement mode) { dto.modePaiement = mode; return this; }
        public Builder referencePaiement(String ref) { dto.referencePaiement = ref; return this; }
        public Builder dateReservation(LocalDateTime date) { dto.dateReservation = date; return this; }
        public Builder dateExpiration(LocalDateTime date) { dto.dateExpiration = date; return this; }
        public Builder dateConfirmation(LocalDateTime date) { dto.dateConfirmation = date; return this; }
        public Builder dateRecuperation(LocalDateTime date) { dto.dateRecuperation = date; return this; }
        public Builder motifAnnulation(String motif) { dto.motifAnnulation = motif; return this; }
        public Builder observations(String observations) { dto.observations = observations; return this; }

        public ReservationResponse build() {
            return dto;
        }
    }

    // Getters seulement (pas de setters pour l'immutabilité)
    public Long getId() { return id; }
    public String getReferenceReservation() { return referenceReservation; }
    public Long getAgenceId() { return agenceId; }
    public Long getConsommateurId() { return consommateurId; }
    public Long getCategorieProduitId() { return categorieProduitId; }
    public Integer getQuantite() { return quantite; }
    public Double getPrixUnitaire() { return prixUnitaire; }
    public Double getMontantTotal() { return montantTotal; }
    public StatutReservation getStatut() { return statut; }
    public ModePaiement getModePaiement() { return modePaiement; }
    public String getReferencePaiement() { return referencePaiement; }
    public LocalDateTime getDateReservation() { return dateReservation; }
    public LocalDateTime getDateExpiration() { return dateExpiration; }
    public LocalDateTime getDateConfirmation() { return dateConfirmation; }
    public LocalDateTime getDateRecuperation() { return dateRecuperation; }
    public String getMotifAnnulation() { return motifAnnulation; }
    public String getObservations() { return observations; }
}