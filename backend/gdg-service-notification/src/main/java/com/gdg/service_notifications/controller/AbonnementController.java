package com.gdg.service_notifications.controller;

import com.gdg.service_notifications.dto.AbonnementRequest;
import com.gdg.service_notifications.model.Abonnement;
import com.gdg.service_notifications.service.AbonnementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/abonnements")
@RequiredArgsConstructor
public class AbonnementController {

    private final AbonnementService abonnementService;

    /**
     * Créer un abonnement
     * POST /api/abonnements
     */
    @PostMapping
    // Créer un abonnement aux notifications
    public ResponseEntity<Abonnement> creerAbonnement(
            @Valid @RequestBody AbonnementRequest request) {
        log.info("📋 Création d'un abonnement pour le consommateur: {}, agence: {}",
                request.getConsommateurId(), request.getAgenceId());
        Abonnement response = abonnementService.creerAbonnement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Désactiver un abonnement
     * DELETE /api/abonnements
     */
    @DeleteMapping
    // Désactiver un abonnement
    public ResponseEntity<Void> desactiverAbonnement(
            @RequestParam Long consommateurId,
            @RequestParam Long agenceId,
            @RequestParam(required = false) Long categorieProduitId) {
        log.info("🔴 Désactivation de l'abonnement pour le consommateur: {}, agence: {}, catégorie: {}",
                consommateurId, agenceId, categorieProduitId);
        abonnementService.desactiverAbonnement(consommateurId, agenceId, categorieProduitId);
        return ResponseEntity.ok().build();
    }

    /**
     * Récupérer tous les abonnements d'un consommateur
     * GET /api/abonnements/consommateur/{consommateurId}
     */
    @GetMapping("/consommateur/{consommateurId}")
    // Récupérer tous les abonnements d'un consommateur
    public ResponseEntity<List<Abonnement>> getAbonnementsByConsommateur(
            @PathVariable Long consommateurId) {
        log.info("📋 Récupération des abonnements pour le consommateur: {}", consommateurId);
        List<Abonnement> abonnements = abonnementService.getAbonnementsByConsommateur(consommateurId);
        return ResponseEntity.ok(abonnements);
    }

    /**
     * Récupérer tous les abonnés d'une agence
     * GET /api/abonnements/agence/{agenceId}
     */
    @GetMapping("/agence/{agenceId}")
    // Récupérer tous les abonnés d'une agence
    public ResponseEntity<List<Abonnement>> getAbonnesPourAgence(
            @PathVariable Long agenceId) {
        log.info("📋 Récupération des abonnés pour l'agence: {}", agenceId);
        List<Abonnement> abonnements = abonnementService.getAbonnesPourAgence(agenceId);
        return ResponseEntity.ok(abonnements);
    }

    /**
     * Vérifier si un consommateur est abonné à une agence
     * GET /api/abonnements/check
     */
    @GetMapping("/check")
    // Vérifier si un consommateur est abonné à une agence
    public ResponseEntity<Boolean> isAbonne(
            @RequestParam Long consommateurId,
            @RequestParam Long agenceId) {
        log.info("🔍 Vérification de l'abonnement pour le consommateur: {}, agence: {}",
                consommateurId, agenceId);
        boolean isAbonne = abonnementService.isAbonne(consommateurId, agenceId);
        return ResponseEntity.ok(isAbonne);
    }
}
