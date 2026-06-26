package com.gdg.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Token obligatoire")
    private String resetToken;

    @NotBlank(message = "Nouveau mot de passe obligatoire")
    private String nouveauMotDePasse;
}
