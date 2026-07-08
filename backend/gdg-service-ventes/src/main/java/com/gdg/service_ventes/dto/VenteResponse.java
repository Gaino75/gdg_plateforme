package com.gdg.service_ventes.dto;

import com.gdg.service_ventes.model.Vente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenteResponse {

    private Long id;
    private String referenceVente;
    private Long agenceId;
    private Long distributeurId;
    private Long consommateurId;
    private Long categorieProduitId;
    private Integer quantite;
    private Double prixUnitaire;
    private Double prixTotal;
    private Vente.ModePaiement modePaiement;
    private Vente.TypeVente typeVente;
    private Vente.StatutVente statut;
    private String referencePaiement;
    private LocalDateTime dateVente;
    private String observations;
    private FactureResponse facture;
}