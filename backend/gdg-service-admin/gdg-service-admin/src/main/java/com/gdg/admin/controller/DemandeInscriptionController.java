package com.gdg.admin.controller;

import com.gdg.admin.dto.TraiterDemandeRequest;
import com.gdg.admin.model.DemandeInscriptionAgence;
import com.gdg.admin.service.DemandeInscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/demandes")

public class DemandeInscriptionController {

    @Autowired
    private DemandeInscriptionService demandeService;

    // Toutes les demandes
    @GetMapping
    public ResponseEntity<List<DemandeInscriptionAgence>> getAll() {
        return ResponseEntity.ok(demandeService.getAll());
    }

    // Demandes en attente
    @GetMapping("/en-attente")
    public ResponseEntity<List<DemandeInscriptionAgence>> getEnAttente() {
        return ResponseEntity.ok(demandeService.getEnAttente());
    }

    // Approuver une demande
    @PutMapping("/{id}/approuver")
    public ResponseEntity<DemandeInscriptionAgence> approuver(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        return ResponseEntity.ok(
            demandeService.approuver(
                id, adminId, request.getRemoteAddr()));
    }

    // Rejeter une demande
    @PutMapping("/{id}/rejeter")
    public ResponseEntity<DemandeInscriptionAgence> rejeter(
            @PathVariable Long id,
            @RequestBody TraiterDemandeRequest body,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        return ResponseEntity.ok(
            demandeService.rejeter(
                id, body.getMotifRejet(),
                adminId, request.getRemoteAddr()));
    }
}
