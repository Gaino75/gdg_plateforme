package com.gdg.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;
    private String prenom;
    private String email; 
    private String motDePasse;
    private String tokenVerification;
    private boolean emailVerifie;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String telephone;

    @Enumerated(EnumType.STRING)
    private Statut statut;


    public enum Role {
        ADMIN, CONSOMMATEUR, DISTRIBUTEUR
    }

    public enum Statut {
        ACTIF, INACTIF,SUSPEMDU
    }
}
