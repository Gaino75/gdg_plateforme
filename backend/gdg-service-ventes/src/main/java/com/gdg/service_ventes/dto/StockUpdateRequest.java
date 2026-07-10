package com.gdg.service_ventes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateRequest {

    private Long agenceId;
    private Long categorieProduitId;
    private Integer quantite;
    private String typeMouvement;
    private String referenceExterne;
    private Long effectuePar;
}