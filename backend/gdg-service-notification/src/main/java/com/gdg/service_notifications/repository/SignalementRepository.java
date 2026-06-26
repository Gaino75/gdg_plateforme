package com.gdg.service_notifications.repository;

import com.gdg.service_notifications.model.Signalement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository  // ⚠️ AJOUTER CETTE ANNOTATION
public interface SignalementRepository extends JpaRepository<Signalement, Long> {

    List<Signalement> findByAgenceId(Long agenceId);
    
    List<Signalement> findByAgenceIdAndTypeSignalement(Long agenceId, Signalement.TypeSignalement type);
    
    List<Signalement> findByAgenceIdAndStatut(Long agenceId, Signalement.StatutSignalement statut);
    
    long countByAgenceIdAndStatut(Long agenceId, Signalement.StatutSignalement statut);

    List<Signalement> findByAgenceIdAndCategorieProduitIdAndStatut(
            Long agenceId, Long categorieProduitId, Signalement.StatutSignalement statut
    );
    
    @Query("SELECT s FROM Signalement s WHERE s.agenceId = :agenceId " +
           "AND s.categorieProduitId = :categorieProduitId " +
           "AND s.typeSignalement = :type " +
           "AND s.statut = 'EN_ATTENTE' " +
           "AND s.dateSignalement >= :depuis")
    List<Signalement> findSignalementsRecentsEnAttente(
            @Param("agenceId") Long agenceId,
            @Param("categorieProduitId") Long categorieProduitId,
            @Param("type") Signalement.TypeSignalement type,
            @Param("depuis") LocalDateTime depuis
    );
    
    @Query("SELECT COUNT(DISTINCT s.consommateurId) FROM Signalement s " +
           "WHERE s.agenceId = :agenceId AND s.categorieProduitId = :categorieProduitId " +
           "AND s.typeSignalement = :type AND s.statut = 'EN_ATTENTE' " +
           "AND s.dateSignalement >= :depuis")
    long countDistinctConsommateursSignalant(
            @Param("agenceId") Long agenceId,
            @Param("categorieProduitId") Long categorieProduitId,
            @Param("type") Signalement.TypeSignalement type,
            @Param("depuis") LocalDateTime depuis
    );
    
    boolean existsByConsommateurIdAndAgenceIdAndCategorieProduitIdAndDateSignalementAfter(
            Long consommateurId, Long agenceId, Long categorieProduitId, LocalDateTime date
    );
}