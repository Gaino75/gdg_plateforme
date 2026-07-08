package com.gdg.service_reservations.dto;

import com.gdg.service_reservations.model.Reservation.ModePaiement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReservationRequest {

    @NotNull(message = "L'ID de l'agence est obligatoire")
    private Long agenceId;

    @NotNull(message = "L'ID du consommateur est obligatoire")
    private Long consommateurId;

    @NotNull(message = "L'ID de la catégorie produit est obligatoire")
    private Long categorieProduitId;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    private Integer quantite;

    @NotNull(message = "Le mode de paiement est obligatoire")
    private ModePaiement modePaiement;

    private String numeroTelephone;

    public Long getAgenceId() { return agenceId; }
    public void setAgenceId(Long agenceId) { this.agenceId = agenceId; }

    public Long getConsommateurId() { return consommateurId; }
    public void setConsommateurId(Long consommateurId) { this.consommateurId = consommateurId; }

    public Long getCategorieProduitId() { return categorieProduitId; }
    public void setCategorieProduitId(Long categorieProduitId) { this.categorieProduitId = categorieProduitId; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public ModePaiement getModePaiement() { return modePaiement; }
    public void setModePaiement(ModePaiement modePaiement) { this.modePaiement = modePaiement; }

    public String getNumeroTelephone() { return numeroTelephone; }
    public void setNumeroTelephone(String numeroTelephone) { this.numeroTelephone = numeroTelephone; }
}