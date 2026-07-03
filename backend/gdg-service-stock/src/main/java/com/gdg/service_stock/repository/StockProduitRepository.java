package com.gdg.service_stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gdg.service_stock.entity.StockProduit;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockProduitRepository extends JpaRepository<StockProduit, Long> {

    /** Tout le stock d'une agence */
    List<StockProduit> findByAgenceId(Long agenceId);

    /** Stock précis : une agence + une catégorie */
    Optional<StockProduit> findByAgenceIdAndCategorieProduitId(
            Long agenceId, Long categorieProduitId);

    /** Stocks critiques d'une agence (pour alertes distributeur) */
    @Query("""
        SELECT s FROM StockProduit s
        WHERE s.agenceId = :agenceId
          AND s.quantiteDisponible <= s.seuilCritique
    """)
    List<StockProduit> findStocksCritiquesParAgence(@Param("agenceId") Long agenceId);

    /**
     * Stocks critiques dont l'alerte n'a PAS encore été envoyée.
     * Utilisé par le scheduler pour ne pas re-notifier en boucle.
     */
    @Query("""
        SELECT s FROM StockProduit s
        WHERE s.quantiteDisponible <= s.seuilCritique
          AND s.alerteEnvoyee = false
    """)
    List<StockProduit> findStocksCritiquesNonNotifies();

    /** Vérifie si au moins un produit est disponible dans une agence
     *  (pour l'affichage visiteur : disponible/indisponible) */
    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
        FROM StockProduit s
        WHERE s.agenceId = :agenceId
          AND s.quantiteDisponible > 0
    """)
    boolean existsStockDisponibleParAgence(@Param("agenceId") Long agenceId);
}
