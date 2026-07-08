package com.gdg.admin.controller;

import com.gdg.admin.dto.SignalementDTO;
import com.gdg.admin.service.AdminSignalementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/signalements")

public class AdminSignalementController {

    @Autowired
    private AdminSignalementService signalementService;

    // Lister tous les signalements
    @GetMapping
    public ResponseEntity<List<SignalementDTO>> getAll() {
        return ResponseEntity.ok(
            signalementService.getAllSignalements());
    }

    // Valider un signalement
    @PutMapping("/{id}/valider")
    public ResponseEntity<Void> valider(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        signalementService.validerSignalement(
            id, adminId, request.getRemoteAddr());
        return ResponseEntity.ok().build();
    }

    // Rejeter un signalement
    @PutMapping("/{id}/rejeter")
    public ResponseEntity<Void> rejeter(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        signalementService.rejeterSignalement(
            id, adminId, request.getRemoteAddr());
        return ResponseEntity.ok().build();
    }
}
