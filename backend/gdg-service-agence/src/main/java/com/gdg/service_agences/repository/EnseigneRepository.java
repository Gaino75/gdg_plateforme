package com.gdg.service_agences.repository;

import com.gdg.service_agences.model.Enseigne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnseigneRepository extends JpaRepository<Enseigne, Long> {
    
    // Trouver une enseigne par son nom
    Optional<Enseigne> findByNom(String nom);
    
    // Trouver toutes les enseignes actives
    List<Enseigne> findByStatut(Enseigne.StatutEnseigne statut);
}