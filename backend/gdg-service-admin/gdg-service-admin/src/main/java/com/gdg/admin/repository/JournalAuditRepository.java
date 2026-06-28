package com.gdg.admin.repository;

import com.gdg.admin.model.JournalAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JournalAuditRepository
        extends JpaRepository<JournalAudit, Long> {

    List<JournalAudit> findByUtilisateurId(Long utilisateurId);
    List<JournalAudit> findByAction(String action);
    List<JournalAudit> findByEntiteTypeAndEntiteId(
        String entiteType, Long entiteId);
    List<JournalAudit> findByDateActionBetween(
        LocalDateTime debut, LocalDateTime fin);
}

    

