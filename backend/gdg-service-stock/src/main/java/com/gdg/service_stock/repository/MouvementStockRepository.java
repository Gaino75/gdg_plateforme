package com.gdg.service_stock.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gdg.service_stock.entity.MouvementStock;
import com.gdg.service_stock.enums.TypeMouvement;
import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

    /** Historique complet d'une agence, du plus récent au plus ancien */
    List<MouvementStock> findByAgenceIdOrderByDateMouvementDesc(Long agenceId);

    /** Mouvements d'une agence par type (ENTREE ou SORTIE) */
    List<MouvementStock> findByAgenceIdAndTypeMouvement(
            Long agenceId, TypeMouvement typeMouvement);

    /** Mouvements sur une période donnée — utile pour les stats distributeur */
    List<MouvementStock> findByAgenceIdAndDateMouvementBetween(
            Long agenceId,
            LocalDateTime debut,
            LocalDateTime fin);
}