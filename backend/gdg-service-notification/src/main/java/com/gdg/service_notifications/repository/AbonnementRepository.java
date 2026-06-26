package com.gdg.service_notifications.repository;

import com.gdg.service_notifications.model.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository  // ⚠️ AJOUTER CETTE ANNOTATION
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {

    List<Abonnement> findByConsommateurIdAndActifTrue(Long consommateurId);
    
    List<Abonnement> findByAgenceIdAndActifTrue(Long agenceId);
    
    @Query("SELECT a FROM Abonnement a WHERE a.agenceId = :agenceId AND a.actif = true " +
           "AND (a.categorieProduitId IS NULL OR a.categorieProduitId = :categorieProduitId)")
    List<Abonnement> findAbonnesPourAgenceEtCategorie(
            @Param("agenceId") Long agenceId,
            @Param("categorieProduitId") Long categorieProduitId
    );
    
    @Query("SELECT a FROM Abonnement a WHERE a.consommateurId = :consommateurId " +
           "AND a.agenceId = :agenceId AND a.actif = true " +
           "AND (a.categorieProduitId IS NULL OR a.categorieProduitId = :categorieProduitId)")
    Optional<Abonnement> findAbonnementActif(
            @Param("consommateurId") Long consommateurId,
            @Param("agenceId") Long agenceId,
            @Param("categorieProduitId") Long categorieProduitId
    );
    
    @Modifying
    @Transactional
    @Query("UPDATE Abonnement a SET a.actif = false, a.dateDesabonnement = CURRENT_TIMESTAMP " +
           "WHERE a.consommateurId = :consommateurId AND a.agenceId = :agenceId " +
           "AND (a.categorieProduitId IS NULL OR a.categorieProduitId = :categorieProduitId)")
    void desactiverAbonnement(
            @Param("consommateurId") Long consommateurId,
            @Param("agenceId") Long agenceId,
            @Param("categorieProduitId") Long categorieProduitId
    );
    
    boolean existsByConsommateurIdAndAgenceIdAndActifTrue(Long consommateurId, Long agenceId);
}