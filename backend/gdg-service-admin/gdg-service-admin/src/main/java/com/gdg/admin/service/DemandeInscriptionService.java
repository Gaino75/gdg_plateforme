package com.gdg.admin.service;

import com.gdg.admin.model.DemandeInscriptionAgence;
import com.gdg.admin.model.DemandeInscriptionAgence.StatutDemande;
import com.gdg.admin.repository.DemandeInscriptionAgenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DemandeInscriptionService {

    @Autowired
    private DemandeInscriptionAgenceRepository demandeRepository;

    @Autowired
    private JournalAuditService journalAuditService;

    public List<DemandeInscriptionAgence> getAll() {
        return demandeRepository.findAll();
    }

    public List<DemandeInscriptionAgence> getEnAttente() {
        return demandeRepository
            .findByStatut(StatutDemande.EN_ATTENTE);
    }

    public DemandeInscriptionAgence approuver(
            Long id, Long adminId, String ip) {
        DemandeInscriptionAgence demande =
            demandeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Demande introuvable"));
        demande.setStatut(StatutDemande.APPROUVEE);
        demande.setTraitePar(adminId);
        demande.setDateTraitement(LocalDateTime.now());
        DemandeInscriptionAgence saved =
            demandeRepository.save(demande);
        journalAuditService.logAction(
            adminId, "ADMIN", "APPROUVER_DEMANDE",
            "DEMANDE_INSCRIPTION", id,
            "Agence: " + demande.getNomAgence(), ip);
        return saved;
    }

    public DemandeInscriptionAgence rejeter(
            Long id, String motif, Long adminId, String ip) {
        DemandeInscriptionAgence demande =
            demandeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Demande introuvable"));
        demande.setStatut(StatutDemande.REJETEE);
        demande.setMotifRejet(motif);
        demande.setTraitePar(adminId);
        demande.setDateTraitement(LocalDateTime.now());
        DemandeInscriptionAgence saved =
            demandeRepository.save(demande);
        journalAuditService.logAction(
            adminId, "ADMIN", "REJETER_DEMANDE",
            "DEMANDE_INSCRIPTION", id,
            "Agence: " + demande.getNomAgence()
            + " | Motif: " + motif, ip);
        return saved;
    }
}
