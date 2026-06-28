package com.gdg.service_stock.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DtoStock {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockProduitDTO {
        private Long id;
        private Long agenceId;
        private Long categorieProduitId;
        private String categorieLibelle;
        private Double poids;
        private Double prixUnitaire;
        private Integer quantiteDisponible;
        private Integer seuilCritique;
        private String etat;
        private Boolean alerteEnvoyee;
        private LocalDateTime derniereMiseAJour;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockPublicDTO {
        private Long agenceId;
        private boolean disponible;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class dtoProduitStokerResponse {
        private Long id;
        private Long agenceId;
        private Long categorieProduitId;
        private String categorieLibelle;
        private Double poids;
        private Double prixUnitaire;
        private Integer quantiteDisponible;
        private Integer seuilCritique;
        private String etat;
        private Boolean alerteEnvoyee;
        private LocalDateTime derniereMiseAJour;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DecrementStockDTO {
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
    public static class ApprovisionnerStockDTO {
        @NotNull(message = "L'ID de l'agence est obligatoire")
        private Long agenceId;

        @NotNull(message = "L'ID de la catégorie est obligatoire")
        private Long categorieProduitId;

        @NotNull
        @Min(value = 1, message = "La quantité doit être au moins 1")
        private Integer quantite;

        private String fournisseur;
        private String numeroBonLivraison;
        private Long effectuePar;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSeuilDTO {
        @NotNull
        @Min(value = 0, message = "Le seuil critique ne peut pas être négatif")
        private Integer seuilCritique;
    }
}
