package com.gdg.service_reservations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO pour la confirmation de paiement
 */
public class ReservationPaiementRequest {

    @NotNull(message = "L'ID de la réservation est obligatoire")
    private Long reservationId;

    @NotBlank(message = "La référence de paiement est obligatoire")
    private String referencePaiement;

    // Getters et Setters
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    public String getReferencePaiement() { return referencePaiement; }
    public void setReferencePaiement(String referencePaiement) { this.referencePaiement = referencePaiement; }
}