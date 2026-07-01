package com.gazstation1.payement_service1.dto;

import com.gazstation1.payement_service1.enums.StatutPaiement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PaiementDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder

    public static class  CreationRequest{

        @NotNull(message = "L'identifiant du client est obligatoire")
        private Long clientId;

        @NotNull(message = "L'identifiant de la commande est obligatoire")
        private Long commandeId;

        @NotNull(message = "Le montant est obligatoire")
        @Positive(message = "Le montant doit etre positif")
        private Double montant;
        @NotNull(message = "Le mode de paiemant est obligatoire")
        private String modePaiement;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long commandeId;
        private  Long clientId;
        private Double montant;
        private String modePaiement;
        private StatutPaiement statut;
        private String referenceTransaction;
        private LocalDateTime dateCreation;
        private LocalDateTime dateMiseAJour;
    }
}
