package com.gdg.service_stock.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.gdg.service_stock.enums.TypeMouvement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DtoMouvement {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MouvementStockDTO {
        private Long id;
        private Long agenceId;
        private String categorieLibelle;
        private TypeMouvement typeMouvement;
        private Integer quantite;
        private Integer quantiteAvant;
        private Integer quantiteApres;
        private String motif;
        private String referenceExterne;
        private Long effectuePar;
        private LocalDateTime dateMouvement;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MouvementResultDTO {
        private Long mouvementId;
        private TypeMouvement typeMouvement;
        private Integer quantiteAvant;
        private Integer quantiteApres;
        private String etatApres;
        private boolean alerteCritiqueDeclenchee;
        private LocalDateTime dateMouvement;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class dtoMouvementStockRequest {
        @NotNull(message = "L'ID de l'agence est obligatoire")
        private Long agenceId;

        @NotNull(message = "L'ID de la catégorie est obligatoire")
        private Long categorieProduitId;

        @NotNull
        @Min(value = 1, message = "La quantité doit être au moins 1")
        private Integer quantite;

        private String referenceExterne;
        private Long effectuePar;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class dtoMouvementStockReponse {
        private Long mouvementId;
        private TypeMouvement typeMouvement;
        private Integer quantiteAvant;
        private Integer quantiteApres;
        private String etatApres;
        private boolean alerteCritiqueDeclenchee;
        private LocalDateTime dateMouvement;
    }
}
