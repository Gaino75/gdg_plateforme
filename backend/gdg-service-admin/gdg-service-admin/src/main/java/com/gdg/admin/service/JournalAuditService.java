package com.gdg.admin.service;

import com.gdg.admin.model.JournalAudit;
import com.gdg.admin.repository.JournalAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JournalAuditService {

    @Autowired
    private JournalAuditRepository journalAuditRepository;

    // Enregistrer une action dans le journal
    public void logAction(Long utilisateurId, String role,
                          String action, String entiteType,
                          Long entiteId, String details,
                          String adresseIp) {
        JournalAudit log = new JournalAudit();
        log.setUtilisateurId(utilisateurId);
        log.setRoleUtilisateur(role);
        log.setAction(action);
        log.setEntiteType(entiteType);
        log.setEntiteId(entiteId);
        log.setDetails(details);
        log.setAdresseIp(adresseIp);
        log.setDateAction(LocalDateTime.now());
        journalAuditRepository.save(log);
    }

    public List<JournalAudit> getAll() {
        return journalAuditRepository.findAll();
    }

    public List<JournalAudit> getByUtilisateur(Long id) {
        return journalAuditRepository.findByUtilisateurId(id);
    }

    public List<JournalAudit> getByAction(String action) {
        return journalAuditRepository.findByAction(action);
    }

    public List<JournalAudit> getByPeriode(
            LocalDateTime debut, LocalDateTime fin) {
        return journalAuditRepository
            .findByDateActionBetween(debut, fin);
    }
}
