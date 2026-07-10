package com.gdg.service_notifications.controller;

import com.gdg.service_notifications.dto.SignalementRequest;
import com.gdg.service_notifications.model.Signalement;
import com.gdg.service_notifications.service.SignalementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/api/signalements")
@RequiredArgsConstructor
public class SignalementController {

    private final SignalementService signalementService;

    /**
     * Créer un signalement (rupture ou disponibilité)
     * POST /api/signalements
     */
    @PostMapping
    // Créer un signalement de rupture ou de disponibilité
    public ResponseEntity<Signalement> creerSignalement(
            @Valid @RequestBody SignalementRequest request) {
        log.info("📢 Création d'un signalement de type {} pour l'agence {}, catégorie {}",
                request.getTypeSignalement(), request.getAgenceId(), request.getCategorieProduitId());
        Signalement response = signalementService.creerSignalement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Récupérer tous les signalements d'une agence
     * GET /api/signalements/agence/{agenceId}
     */
    @GetMapping("/agence/{agenceId}")
    // Récupérer tous les signalements d'une agence
    public ResponseEntity<List<Signalement>> getSignalementsByAgence(
            @PathVariable Long agenceId) {
        log.info("📋 Récupération des signalements pour l'agence: {}", agenceId);
        List<Signalement> signalements = signalementService.getSignalementsByAgence(agenceId);
        return ResponseEntity.ok(signalements);
    }

    /**
     * Récupérer les signalements d'une agence par type
     * GET /api/signalements/agence/{agenceId}/type/{type}
     */
    @GetMapping("/agence/{agenceId}/type/{type}")
    // Récupérer les signalements d'une agence par type
    public ResponseEntity<List<Signalement>> getSignalementsByAgenceAndType(
            @PathVariable Long agenceId,
            @PathVariable String type) {
        log.info("📋 Récupération des signalements de type {} pour l'agence: {}", type, agenceId);
        List<Signalement> signalements = signalementService.getSignalementsByAgenceAndType(
                agenceId, 
                com.gdg.service_notifications.model.Signalement.TypeSignalement.valueOf(type)
        );
        return ResponseEntity.ok(signalements);
    }

    /**
     * Récupérer les signalements d'une agence par statut
     * GET /api/signalements/agence/{agenceId}/statut/{statut}
     */
    @GetMapping("/agence/{agenceId}/statut/{statut}")
    // Récupérer les signalements d'une agence par statut
    public ResponseEntity<List<Signalement>> getSignalementsByAgenceAndStatut(
            @PathVariable Long agenceId,
            @PathVariable String statut) {
        log.info("📋 Récupération des signalements avec statut {} pour l'agence: {}", statut, agenceId);
        List<Signalement> signalements = signalementService.getSignalementsByAgenceAndStatut(
                agenceId,
                com.gdg.service_notifications.model.Signalement.StatutSignalement.valueOf(statut)
        );
        return ResponseEntity.ok(signalements);
    }

    /**
     * Récupérer un signalement par son ID
     * GET /api/signalements/{id}
     */
    @GetMapping("/{id}")
    // Récupérer un signalement par son ID
    public ResponseEntity<Signalement> getSignalementById(
            @PathVariable Long id) {
        log.info("📋 Récupération du signalement: {}", id);
        Signalement signalement = signalementService.getSignalementById(id);
        return ResponseEntity.ok(signalement);
    }

    /**
     * Traiter un signalement (valider ou rejeter) - pour l'admin
     * PUT /api/signalements/{id}/traiter
     */
    @PutMapping("/{id}/traiter")
    // Traiter un signalement (valider ou rejeter)
    public ResponseEntity<Signalement> traiterSignalement(
            @PathVariable Long id,
            @RequestParam String statut,
            @RequestParam(required = false) Long traitePar) {
        log.info("⚙️ Traitement du signalement {}: nouveau statut {}", id, statut);
        Signalement response = signalementService.traiterSignalement(
                id,
                com.gdg.service_notifications.model.Signalement.StatutSignalement.valueOf(statut),
                traitePar
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Compter les signalements en attente pour une agence
     * GET /api/signalements/agence/{agenceId}/pending/count
     */
    @GetMapping("/agence/{agenceId}/pending/count")
    // Compter les signalements en attente pour une agence
    public ResponseEntity<Long> countPendingSignalementsByAgence(
            @PathVariable Long agenceId) {
        log.info("🔢 Comptage des signalements en attente pour l'agence: {}", agenceId);
        long count = signalementService.countPendingSignalementsByAgence(agenceId);
        return ResponseEntity.ok(count);
    }

    @GetMapping
    public ResponseEntity<List<Signalement>>getAllSignalements(){
        return ResponseEntity.ok(signalementService.getAllSignalements());
    }
    
}
