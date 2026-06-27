package com.gdg.service_agences.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO pour la validation d'une agence (par l'admin)
 */
public class AgenceValidationRequest {

    @NotNull(message = "L'ID de l'agence est obligatoire")
    private Long agenceId;

    @NotNull(message = "L'ID de l'administrateur est obligatoire")
    private Long adminId;

    private Boolean valider = true;  // true = valider, false = rejeter
    private String motifRejet;

    // Getters et Setters
    public Long getAgenceId() { return agenceId; }
    public void setAgenceId(Long agenceId) { this.agenceId = agenceId; }
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public Boolean getValider() { return valider; }
    public void setValider(Boolean valider) { this.valider = valider; }
    public String getMotifRejet() { return motifRejet; }
    public void setMotifRejet(String motifRejet) { this.motifRejet = motifRejet; }
}