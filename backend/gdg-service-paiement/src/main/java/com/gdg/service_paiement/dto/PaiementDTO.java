package com.gdg.service_paiement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.gdg.service_paiement.enums.StatutPaiement;

public class PaiementDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreationRequest {

        @NotNull
        private Long clientId;

        @NotNull
        private Long commandeId;

        @NotNull
        private Long agenceId;

        @NotNull
        @Positive
        private Double montant;

        @NotNull
        private String modePaiement;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InitierRequest {

        @NotNull
        private Long reservationId;

        @NotNull
        private Long consommateurId;

        @NotNull
        private Long agenceId;

        @NotNull
        @Positive
        private Double montant;

        @NotBlank
        private String modePaiement;

        private String numeroTelephone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CallbackRequest {
        private String referenceTransaction;
        private String referenceOperateur;
        private String statut;
        private String payload;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long commandeId;
        private Long clientId;
        private Long agenceId;
        private Double montant;
        private String modePaiement;
        private StatutPaiement statut;
        private String referenceTransaction;
        private String numeroTelephone;
        private String operateur;
        private LocalDateTime dateCreation;
        private LocalDateTime dateMiseAJour;
    }
}
