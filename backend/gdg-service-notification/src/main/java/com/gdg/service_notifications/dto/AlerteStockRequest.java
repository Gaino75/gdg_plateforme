package com.gdg.service_notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlerteStockRequest {

    private Long agenceId;
    
    private Long categorieProduitId;
    
    private String libelleProduit;
    
    private int quantiteDisponible;
    
    private Long distributeurId;
    
    private String emailDistributeur;
}
