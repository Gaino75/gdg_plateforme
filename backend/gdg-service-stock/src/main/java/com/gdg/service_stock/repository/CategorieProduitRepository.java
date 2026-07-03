package com.gdg.service_stock.repository;

import com.gdg.service_stock.entity.CategorieProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieProduitRepository extends JpaRepository<CategorieProduit, Long> {

    /** Uniquement les catégories actives (pour l'affichage public) */
    List<CategorieProduit> findByActifTrue();

    Optional<CategorieProduit> findByLibelle(String libelle);
}