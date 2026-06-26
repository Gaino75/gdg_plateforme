// dto/SignalementRequest.java
package com.gdg.service_notifications.dto;

import com.gdg.service_notifications.model.Signalement;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignalementRequest {

    @NotNull
    private Long consommateurId;

    @NotNull
    private Long agenceId;

    @NotNull
    private Long categorieProduitId;

    @NotNull
    private Signalement.TypeSignalement typeSignalement;

    private String commentaire;
}
