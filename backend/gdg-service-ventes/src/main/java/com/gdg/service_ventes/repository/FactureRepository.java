package com.gdg.service_ventes.repository;

import com.gdg.service_ventes.model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

    Optional<Facture> findByNumeroFacture(String numeroFacture);

    Optional<Facture> findByVenteId(Long venteId);

    List<Facture> findByVenteAgenceId(Long agenceId);

    List<Facture> findByVenteConsommateurId(Long consommateurId);
}
