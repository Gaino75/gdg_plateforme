package com.gdg.service_agences.repository;

import com.gdg.service_agences.model.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Long> {
    
    // Trouver une ville par son nom
    Optional<Ville> findByNom(String nom);
    
    // Trouver les villes par région
    List<Ville> findByRegion(String region);
}