package com.gdg.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "Le token de rafraîchissement est obligatoire")
    private String refreshToken;
}
