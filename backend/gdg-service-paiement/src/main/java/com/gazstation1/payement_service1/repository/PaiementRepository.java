package com.gazstation1.payement_service1.repository;

import com.gazstation1.payement_service1.model.Paiement;
import com.gazstation1.payement_service1.enums.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    //SELECT * FROM paiements WHERE client_id = ?
    List<Paiement> findByClientId(Long clientId);

    Optional<Paiement> findByCommandeId(Long commandeId);

    Optional<Paiement> findByReferenceTransaction(String referenceTransaction);

    List<Paiement> findByStatut(StatutPaiement statut);
}
