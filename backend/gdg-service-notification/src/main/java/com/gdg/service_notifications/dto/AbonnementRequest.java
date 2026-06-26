package com.gdg.service_notifications.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbonnementRequest {

    @NotNull
    private Long consommateurId;

    @NotNull
    private Long agenceId;

    private Long categorieProduitId;  // null = toutes les catégories

    // ❌ SUPPRIMEZ actif car il n'est pas utilisé dans la création
    // private Boolean actif;
}