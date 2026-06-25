package com.gdg.auth.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import com.gdg.auth.model.Utilisateur;

@Data
public class RegisterRequest {
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email n'est pas valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;


    private Utilisateur.Role role;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;
    
    



    
}
