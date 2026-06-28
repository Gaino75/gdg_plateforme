package com.gdg.admin.repository;

import com.gdg.admin.model.DemandeInscriptionAgence;
import com.gdg.admin.model.DemandeInscriptionAgence.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DemandeInscriptionAgenceRepository
        extends JpaRepository<DemandeInscriptionAgence, Long> {

    List<DemandeInscriptionAgence> findByStatut(StatutDemande statut);
    long countByStatut(StatutDemande statut);
}
