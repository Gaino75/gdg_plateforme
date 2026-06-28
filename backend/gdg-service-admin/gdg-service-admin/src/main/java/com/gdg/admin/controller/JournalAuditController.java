package com.gdg.admin.controller;

import com.gdg.admin.model.JournalAudit;
import com.gdg.admin.service.JournalAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/journal")
@CrossOrigin(origins = "*")
public class JournalAuditController {

    @Autowired
    private JournalAuditService journalAuditService;

    // Tout le journal
    @GetMapping
    public ResponseEntity<List<JournalAudit>> getAll() {
        return ResponseEntity.ok(journalAuditService.getAll());
    }

    // Journal par utilisateur
    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<List<JournalAudit>> getByUtilisateur(
            @PathVariable Long id) {
        return ResponseEntity.ok(
            journalAuditService.getByUtilisateur(id));
    }

    // Journal par action
    @GetMapping("/action/{action}")
    public ResponseEntity<List<JournalAudit>> getByAction(
            @PathVariable String action) {
        return ResponseEntity.ok(
            journalAuditService.getByAction(action));
    }

    // Journal par période
    @GetMapping("/periode")
    public ResponseEntity<List<JournalAudit>> getByPeriode(
            @RequestParam @DateTimeFormat(
                iso = DateTimeFormat.ISO.DATE_TIME)
                LocalDateTime debut,
            @RequestParam @DateTimeFormat(
                iso = DateTimeFormat.ISO.DATE_TIME)
                LocalDateTime fin) {
        return ResponseEntity.ok(
            journalAuditService.getByPeriode(debut, fin));
    }
}
