package com.gdg.service_ventes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FactureResponse {

    private Long id;
    private String numeroFacture;
    private Long venteId;
    private LocalDateTime dateEmission;
    private String urlPdf;
    private String logoAgence;
    private String enteteAgence;
    private String piedAgence;
    private String nomClient;
    private String telephoneClient;
    private Double montantHt;
    private Double tauxTva;
    private Double montantTva;
    private Double montantTtc;
}