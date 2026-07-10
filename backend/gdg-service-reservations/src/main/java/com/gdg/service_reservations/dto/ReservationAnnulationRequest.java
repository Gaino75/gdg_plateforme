package com.gdg.service_reservations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO pour l'annulation d'une réservation
 */
public class ReservationAnnulationRequest {

    @NotNull(message = "L'ID de la réservation est obligatoire")
    private Long reservationId;

    @NotBlank(message = "Le motif d'annulation est obligatoire")
    private String motif;

    @NotNull(message = "L'ID de l'utilisateur qui annule est obligatoire")
    private Long effectuePar;

    // Getters et Setters
    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public Long getEffectuePar() { return effectuePar; }
    public void setEffectuePar(Long effectuePar) { this.effectuePar = effectuePar; }
}