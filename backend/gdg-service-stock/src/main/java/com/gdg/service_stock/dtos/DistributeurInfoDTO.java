package com.gdg.service_stock.dtos;

import lombok.Data;

@Data
public class DistributeurInfoDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Long agenceId;

    
}
