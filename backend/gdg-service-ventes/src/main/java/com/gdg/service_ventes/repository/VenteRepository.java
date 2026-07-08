package com.gdg.service_ventes.repository;

import com.gdg.service_ventes.model.Vente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {

    List<Vente> findByAgenceIdOrderByDateVenteDesc(Long agenceId);

    Page<Vente> findByAgenceId(Long agenceId, Pageable pageable);

    List<Vente> findByDistributeurIdOrderByDateVenteDesc(Long distributeurId);

    Optional<Vente> findByReferenceVente(String referenceVente);

    @Query("SELECT SUM(v.quantite) FROM Vente v WHERE v.agenceId = :agenceId AND v.categorieProduitId = :categorieProduitId AND v.statut = 'CONFIRMEE'")
    Integer sumQuantiteVendueParCategorie(@Param("agenceId") Long agenceId, @Param("categorieProduitId") Long categorieProduitId);

    @Query("SELECT SUM(v.prixTotal) FROM Vente v WHERE v.agenceId = :agenceId AND v.statut = 'CONFIRMEE'")
    Double sumChiffreAffaireParAgence(@Param("agenceId") Long agenceId);

    long countByAgenceIdAndStatut(Long agenceId, Vente.StatutVente statut);
}
