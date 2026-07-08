package com.gdg.admin.controller;

import com.gdg.admin.dto.ActionRequest;
import com.gdg.admin.dto.AgenceDTO;
import com.gdg.admin.service.AdminAgenceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/agences")

public class AdminAgenceController {

    @Autowired
    private AdminAgenceService agenceService;

    // Agences en attente de validation
    @GetMapping("/en-attente")
    public ResponseEntity<List<AgenceDTO>> getEnAttente() {
        return ResponseEntity.ok(
            agenceService.getAgencesEnAttente());
    }

    // Valider une agence
    @PutMapping("/{id}/valider")
    public ResponseEntity<Void> valider(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        agenceService.validerAgence(
            id, adminId, request.getRemoteAddr());
        return ResponseEntity.ok().build();
    }

    // Rejeter une agence
    @PutMapping("/{id}/rejeter")
    public ResponseEntity<Void> rejeter(
            @PathVariable Long id,
            @RequestBody ActionRequest body,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        agenceService.rejeterAgence(
            id, body.getMotif(), adminId,
            request.getRemoteAddr());
        return ResponseEntity.ok().build();
    }

    // Suspendre une agence
    @PutMapping("/{id}/suspendre")
    public ResponseEntity<Void> suspendre(
            @PathVariable Long id,
            @RequestBody ActionRequest body,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        agenceService.suspendreAgence(
            id, body.getMotif(), adminId,
            request.getRemoteAddr());
        return ResponseEntity.ok().build();
    }

    // Réactiver une agence
    @PutMapping("/{id}/reactiver")
    public ResponseEntity<Void> reactiver(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        agenceService.reactiverAgence(
            id, adminId, request.getRemoteAddr());
        return ResponseEntity.ok().build();
    }
}