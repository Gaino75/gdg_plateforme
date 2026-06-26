// model/Signalement.java
package com.gdg.service_notifications.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "signalement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Signalement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consommateur_id", nullable = false)
    private Long consommateurId;

    @Column(name = "agence_id", nullable = false)
    private Long agenceId;

    @Column(name = "categorie_produit_id", nullable = false)
    private Long categorieProduitId;

    @Column(name = "type_signalement", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeSignalement typeSignalement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSignalement statut;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @CreationTimestamp
    @Column(name = "date_signalement", nullable = false)
    private LocalDateTime dateSignalement;

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    @Column(name = "traite_par")
    private Long traitePar;

    public enum TypeSignalement {
        RUPTURE, DISPONIBLE
    }

    public enum StatutSignalement {
        EN_ATTENTE, CONFIRME, REJETE
    }
}
