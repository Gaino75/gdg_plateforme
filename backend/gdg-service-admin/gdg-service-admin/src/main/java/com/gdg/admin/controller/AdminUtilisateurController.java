package com.gdg.admin.controller;

import com.gdg.admin.dto.ActionRequest;
import com.gdg.admin.dto.UtilisateurDTO;
import com.gdg.admin.service.AdminUtilisateurService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/utilisateurs")

public class AdminUtilisateurController {

    @Autowired
    private AdminUtilisateurService utilisateurService;

    // Lister tous les utilisateurs
    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAll() {
        return ResponseEntity.ok(
            utilisateurService.getAllUtilisateurs());
    }

    // Suspendre un utilisateur
    @PutMapping("/{id}/suspendre")
    public ResponseEntity<Void> suspendre(
            @PathVariable Long id,
            @RequestBody ActionRequest body,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        utilisateurService.suspendreUtilisateur(
            id, body.getMotif(), adminId,
            request.getRemoteAddr());
        return ResponseEntity.ok().build();
    }

    // Réactiver un utilisateur
    @PutMapping("/{id}/reactiver")
    public ResponseEntity<Void> reactiver(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        utilisateurService.reactiverUtilisateur(
            id, adminId, request.getRemoteAddr());
        return ResponseEntity.ok().build();
    }

    // Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        utilisateurService.supprimerUtilisateur(
            id, adminId, request.getRemoteAddr());
        return ResponseEntity.noContent().build();
    }
}
