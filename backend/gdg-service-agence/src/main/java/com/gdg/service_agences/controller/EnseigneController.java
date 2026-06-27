package com.gdg.service_agences.controller;

import com.gdg.service_agences.dto.EnseigneRequest;
import com.gdg.service_agences.dto.EnseigneResponse;
import com.gdg.service_agences.dto.EnseigneSummary;
import com.gdg.service_agences.model.Enseigne;
import com.gdg.service_agences.service.EnseigneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enseignes")
public class EnseigneController {

    private final EnseigneService enseigneService;

    // ============================================================
    // CONSTRUCTEUR (remplace @RequiredArgsConstructor)
    // ============================================================

    public EnseigneController(EnseigneService enseigneService) {
        this.enseigneService = enseigneService;
    }

    // ============================================================
    // MÉTHODES
    // ============================================================

    @GetMapping
    public ResponseEntity<List<EnseigneSummary>> getAllEnseignes() {
        List<Enseigne> enseignes = enseigneService.getAllEnseignes();
        List<EnseigneSummary> summaries = enseignes.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/actives")
    public ResponseEntity<List<EnseigneSummary>> getEnseignesActives() {
        List<Enseigne> enseignes = enseigneService.getEnseignesActives();
        List<EnseigneSummary> summaries = enseignes.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnseigneResponse> getEnseigneById(@PathVariable Long id) {
        Enseigne enseigne = enseigneService.getEnseigneById(id);
        return ResponseEntity.ok(mapToResponse(enseigne));
    }

    @PostMapping
    public ResponseEntity<EnseigneResponse> creerEnseigne(@Valid @RequestBody EnseigneRequest request) {
        Enseigne enseigne = new Enseigne(
            request.getNom(),
            request.getLogo(),
            request.getDescription(),
            request.getSiteWeb(),
            request.getTelephone(),
            request.getEmailContact()
        );
        Enseigne nouvelleEnseigne = enseigneService.creerEnseigne(enseigne);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(nouvelleEnseigne));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnseigneResponse> updateEnseigne(
            @PathVariable Long id,
            @Valid @RequestBody EnseigneRequest request) {
        Enseigne enseigne = new Enseigne(
            request.getNom(),
            request.getLogo(),
            request.getDescription(),
            request.getSiteWeb(),
            request.getTelephone(),
            request.getEmailContact()
        );
        enseigne.setId(id);
        Enseigne updated = enseigneService.updateEnseigne(id, enseigne);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEnseigne(@PathVariable Long id) {
        enseigneService.supprimerEnseigne(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/desactiver")
    public ResponseEntity<Void> desactiverEnseigne(@PathVariable Long id) {
        enseigneService.desactiverEnseigne(id);
        return ResponseEntity.ok().build();
    }

    // ============================================================
    // MÉTHODES DE MAPPING
    // ============================================================

    private EnseigneSummary mapToSummary(Enseigne enseigne) {
        return new EnseigneSummary(
            enseigne.getId(),
            enseigne.getNom(),
            enseigne.getLogo(),
            enseigne.getStatut(),
            enseigne.getAgences() != null ? (long) enseigne.getAgences().size() : 0L
        );
    }

    private EnseigneResponse mapToResponse(Enseigne enseigne) {
        return EnseigneResponse.builder()
                .id(enseigne.getId())
                .nom(enseigne.getNom())
                .logo(enseigne.getLogo())
                .description(enseigne.getDescription())
                .siteWeb(enseigne.getSiteWeb())
                .telephone(enseigne.getTelephone())
                .emailContact(enseigne.getEmailContact())
                .statut(enseigne.getStatut())
                .dateCreation(enseigne.getDateCreation())
                .nombreAgences(enseigne.getAgences() != null ? (long) enseigne.getAgences().size() : 0L)
                .build();
    }
}