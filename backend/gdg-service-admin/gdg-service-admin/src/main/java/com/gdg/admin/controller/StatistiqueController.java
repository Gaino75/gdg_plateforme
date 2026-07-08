package com.gdg.admin.controller;

import com.gdg.admin.model.StatistiqueJournaliere;
import com.gdg.admin.service.StatistiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/statistiques")

public class StatistiqueController {

    @Autowired
    private StatistiqueService statistiqueService;

    // Stats globales plateforme
    @GetMapping("/globales")
    public ResponseEntity<List<StatistiqueJournaliere>> getGlobales() {
        return ResponseEntity.ok(
            statistiqueService.getStatsGlobales());
    }

    // Stats par agence
    @GetMapping("/agence/{agenceId}")
    public ResponseEntity<List<StatistiqueJournaliere>> getByAgence(
            @PathVariable Long agenceId) {
        return ResponseEntity.ok(
            statistiqueService.getStatsByAgence(agenceId));
    }

    // Stats par période
    @GetMapping("/periode")
    public ResponseEntity<List<StatistiqueJournaliere>> getByPeriode(
            @RequestParam @DateTimeFormat(
                iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(
                iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(
            statistiqueService.getStatsByPeriode(debut, fin));
    }

    // Montant total global
    @GetMapping("/montant-global")
    public ResponseEntity<Double> getMontantGlobal() {
        return ResponseEntity.ok(
            statistiqueService.getMontantTotalGlobal());
    }
}
