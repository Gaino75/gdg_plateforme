package com.gdg.service_paiement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdg.service_paiement.enums.StatutPaiement;
import org.springframework.stereotype.Repository;
import com.gdg.service_paiement.model.Paiement;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    //SELECT * FROM paiements WHERE client_id = ?
    List<Paiement> findByClientId(Long clientId);

    Optional<Paiement> findByCommandeId(Long commandeId);

    Optional<Paiement> findByReferenceTransaction(String referenceTransaction);

    List<Paiement> findByStatut(StatutPaiement statut);
}
