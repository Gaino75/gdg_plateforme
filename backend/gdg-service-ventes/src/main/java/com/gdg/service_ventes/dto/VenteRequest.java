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

    @NotNull(message = "L'identifiant de l'agence est obligatoire")
    private Long agenceId;

    @NotNull(message = "L'identifiant du distributeur est obligatoire")
    private Long distributeurId;

    private Long consommateurId;

    @NotNull(message = "L'identifiant de la catégorie produit est obligatoire")
    private Long categorieProduitId;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantite;

    @NotNull(message = "Le prix unitaire est obligatoire")
    private Double prixUnitaire;

    @NotNull(message = "Le mode de paiement est obligatoire")
    private Vente.ModePaiement modePaiement;

    private Vente.TypeVente typeVente;

    private String referencePaiement;

    private String observations;

    private String nomClient;

    private String telephoneClient;
}
