package com.gdg.service_stock.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Stock d'une catégorie de produit pour une agence donnée.
 * Mappée sur : stock_schema.stock_produit
 *
 * Contrainte BDD : UNIQUE(agence_id, categorie_produit_id)
 * → une seule ligne de stock par couple (agence, produit)
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identifiant de l'agence propriétaire de ce stock.
     * Pas de @ManyToOne car l'agence est gérée par un autre microservice.
     * On stocke uniquement l'ID (communication inter-services via API).
     */
    @Column(name = "agence_id", nullable = false)
    private Long agenceId;

    /** Catégorie de produit (3kg, 9kg, 12.5kg, 30kg) */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categorie_produit_id", nullable = false)
    private CategorieProduit categorieProduit;

    /** Nombre de bouteilles disponibles */
    @Column(name = "quantite_disponible", nullable = false)
    @Builder.Default
    private Integer quantiteDisponible = 0;

    /** Seuil en dessous duquel une alerte est déclenchée */
    @Column(name = "seuil_critique", nullable = false)
    @Builder.Default
    private Integer seuilCritique = 5;

    /**
     * true = alerte déjà envoyée pour ce niveau de stock.
     * Remis à false quand le stock remonte au-dessus du seuil.
     * Évite d'envoyer des dizaines d'alertes répétées.
     */
    @Column(name = "alerte_envoyee", nullable = false)
    @Builder.Default
    private Boolean alerteEnvoyee = false;

    @Column(name = "derniere_mise_a_jour", nullable = false)
    @Builder.Default
    private LocalDateTime derniereMiseAJour = LocalDateTime.now();

    // ─── Méthodes métier ───────────────────────────────────────────────────

    /** Retourne true si le stock est au niveau critique ou en dessous */
    public boolean estCritique() {
        return this.quantiteDisponible <= this.seuilCritique;
    }

    /** Retourne l'état lisible du stock */
    public String getEtat() {
        if (this.quantiteDisponible == 0) return "RUPTURE";
        if (estCritique())               return "CRITIQUE";
        return "DISPONIBLE";
    }

    /** Décrémente le stock et met à jour l'horodatage */
    public void decrementer(int quantite) {
        if (quantite > this.quantiteDisponible) {
            throw new IllegalArgumentException(
                "Stock insuffisant : demandé=" + quantite +
                ", disponible=" + this.quantiteDisponible
            );
        }
        this.quantiteDisponible -= quantite;
        this.derniereMiseAJour = LocalDateTime.now();
    }

    /** Incrémente le stock et remet l'alerte à zéro si on repasse au-dessus du seuil */
    public void incrementer(int quantite) {
        this.quantiteDisponible += quantite;
        this.derniereMiseAJour = LocalDateTime.now();
        if (!estCritique()) {
            this.alerteEnvoyee = false;  // reset : prochaine alerte possible
        }
    }
}