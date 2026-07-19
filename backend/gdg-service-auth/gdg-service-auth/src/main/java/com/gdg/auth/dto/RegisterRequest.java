package com.gdg.auth.dto;

import com.gdg.auth.model.Utilisateur;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Nom obligatoire")
    private String nom;

    @NotBlank(message = "Prénom obligatoire")
    private String prenom;

    @NotBlank(message = "Email obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Mot de passe obligatoire")
    private String motDePasse;

    private String telephone;

    // Défini automatiquement par l'endpoint
    private Utilisateur.Role role;

    // Consommateur seulement
    private String villeResidence;
    private LocalDate dateNaissance;

    // Distributeur seulement
    private String poste;

    // Admin seulement
    private String niveauAcces;
}