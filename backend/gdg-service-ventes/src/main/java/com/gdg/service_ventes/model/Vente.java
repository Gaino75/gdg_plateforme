package com.gdg.service_ventes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "vente", schema = "ventes_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_vente", nullable = false, unique = true)
    private String referenceVente;

    @Column(name = "agence_id", nullable = false)
    private Long agenceId;

    @Column(name = "distributeur_id", nullable = false)
    private Long distributeurId;

    @Column(name = "consommateur_id")
    private Long consommateurId;

    @Column(name = "categorie_produit_id", nullable = false)
    private Long categorieProduitId;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @Column(name = "prix_total", nullable = false)
    private Double prixTotal;

    @Column(name = "mode_paiement", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModePaiement modePaiement;

    @Column(name = "type_vente", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeVente typeVente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutVente statut = StatutVente.CONFIRMEE;

    @Column(name = "reference_paiement")
    private String referencePaiement;

    @CreationTimestamp
    @Column(name = "date_vente", nullable = false)
    private LocalDateTime dateVente;

    @Column(columnDefinition = "TEXT")
    private String observations;

    public enum ModePaiement {
        CASH, ORANGE_MONEY, MTN_MOBILE_MONEY
    }

    public enum TypeVente {
        EN_LIGNE, HORS_LIGNE
    }

    public enum StatutVente {
        CONFIRMEE, ANNULEE, REMBOURSEE
    }
}