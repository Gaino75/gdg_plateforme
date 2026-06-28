package com.gdg.service_stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.gdg.service_stock.enums.TypeMouvement;
import java.time.LocalDateTime;

/**
 * Trace chaque mouvement de stock (RG-15 du cahier des charges).
 * Mappée sur : stock_schema.mouvement_stock
 *
 * Chaque vente ou approvisionnement crée une ligne ici.
 * Table en append-only : on n'update jamais, on insère seulement.
 */
@Entity
@Table(name = "mouvement_stock", schema = "stock_schema")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agence_id", nullable = false)
    private Long agenceId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categorie_produit_id", nullable = false)
    private CategorieProduit categorieProduit;

    /**
     * ENTREE = approvisionnement (stock monte)
     * SORTIE = vente (stock descend)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type_mouvement", nullable = false, length = 20)
    private TypeMouvement typeMouvement;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "quantite_avant", nullable = false)
    private Integer quantiteAvant;

    @Column(name = "quantite_apres", nullable = false)
    private Integer quantiteApres;

    /** Ex : "Vente cash", "Approvisionnement Tradex", "Réservation #42" */
    @Column(length = 100)
    private String motif;

    /**
     * ID externe de la vente ou réservation dans son microservice d'origine.
     * Permet la traçabilité inter-services.
     */
    @Column(name = "reference_externe", length = 100)
    private String referenceExterne;

    /** ID de l'utilisateur (distributeur) qui a effectué l'action */
    @Column(name = "effectue_par")
    private Long effectuePar;

    @Column(name = "date_mouvement", nullable = false)
    @Builder.Default
    private LocalDateTime dateMouvement = LocalDateTime.now();
}