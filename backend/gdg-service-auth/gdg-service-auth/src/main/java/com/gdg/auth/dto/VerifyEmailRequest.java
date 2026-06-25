package com.gdg.auth.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class VerifyEmailRequest {
    @NotBlank(message = "Le token est obligatoire")
    private String token;
}
