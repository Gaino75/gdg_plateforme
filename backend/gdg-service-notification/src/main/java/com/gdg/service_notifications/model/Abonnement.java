// model/Abonnement.java
package com.gdg.service_notifications.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "abonnement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Abonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consommateur_id", nullable = false)
    private Long consommateurId;

    @Column(name = "agence_id", nullable = false)
    private Long agenceId;

    @Column(name = "categorie_produit_id")
    private Long categorieProduitId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean actif=true;

    @CreationTimestamp
    @Column(name = "date_abonnement", nullable = false)
    private LocalDateTime dateAbonnement;

    @Column(name = "date_desabonnement")
    private LocalDateTime dateDesabonnement;
}
