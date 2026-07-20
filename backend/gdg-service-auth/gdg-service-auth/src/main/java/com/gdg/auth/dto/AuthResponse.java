package com.gdg.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponse {

    private Long id;
    private String token;
    private String role;
    private String nom;
    private String prenom;
    private String email;
}
