package com.gdg.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilResponse {

    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role;
    private String statut;
    private LocalDateTime dateInscription;
    private Boolean emailVerifie;
}
