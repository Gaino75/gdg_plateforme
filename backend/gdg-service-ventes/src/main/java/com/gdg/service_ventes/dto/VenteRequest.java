package com.gdg.service_ventes.dto;

import com.gdg.service_ventes.model.Vente;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenteRequest {

    @NotNull
    private Long agenceId;

    @NotNull
    private Long distributeurId;

    private Long consommateurId;

    @NotNull
    private Long categorieProduitId;

    @NotNull
    @Min(1)
    private Integer quantite;

    @NotNull
    private Double prixUnitaire;

    @NotNull
    private Vente.ModePaiement modePaiement;

    private Vente.TypeVente typeVente;

    private String referencePaiement;

    private String observations;

    private String nomClient;

    private String telephoneClient;
}
