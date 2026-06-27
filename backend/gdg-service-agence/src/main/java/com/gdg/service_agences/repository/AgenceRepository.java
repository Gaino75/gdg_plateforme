package com.gdg.service_agences.repository;

import com.gdg.service_agences.model.Agence;
import com.gdg.service_agences.model.Agence.StatutAgence;
import com.gdg.service_agences.model.Enseigne;
import com.gdg.service_agences.model.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgenceRepository extends JpaRepository<Agence, Long> {

    // Trouver les agences d'une enseigne
    List<Agence> findByEnseigne(Enseigne enseigne);

    // Trouver les agences dans une ville
    List<Agence> findByVille(Ville ville);

    // Trouver les agences d'une enseigne dans une ville
    List<Agence> findByEnseigneAndVille(Enseigne enseigne, Ville ville);

    // Trouver les agences par statut
    List<Agence> findByStatut(StatutAgence statut);

    // Trouver les agences actives d'une enseigne
    List<Agence> findByStatutAndEnseigne(StatutAgence statut, Enseigne enseigne);

    // Trouver les agences actives dans une ville
    List<Agence> findByStatutAndVille(StatutAgence statut, Ville ville);

    // Trouver les agences en attente (pour l'admin)
    List<Agence> findByStatutOrderByDateCreationAsc(StatutAgence statut);

    // Recherche par nom (like)
    List<Agence> findByNomContainingIgnoreCase(String nom);

    // Recherche par ville et enseigne et statut
    List<Agence> findByVilleAndEnseigneAndStatut(Ville ville, Enseigne enseigne, StatutAgence statut);

    // Compter les agences par statut
    long countByStatut(StatutAgence statut);

    // Compter les agences par enseigne
    long countByEnseigne(Enseigne enseigne);

    // Trouver les agences avec leurs horaires (requête JPQL)
    @Query("SELECT a FROM Agence a LEFT JOIN FETCH a.horaires WHERE a.id = :id")
    Optional<Agence> findByIdWithHoraires(@Param("id") Long id);

    // Trouver les agences actives avec leurs horaires
    @Query("SELECT a FROM Agence a LEFT JOIN FETCH a.horaires WHERE a.statut = :statut")
    List<Agence> findAllByStatutWithHoraires(@Param("statut") StatutAgence statut);
}