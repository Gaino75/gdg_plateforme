package com.gdg.auth.controller;

import com.gdg.auth.dto.DistributeurInfoResponse;
import com.gdg.auth.service.DistributeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/internal/distributeurs")

public class InternalDistributeurController {

    @Autowired
    private DistributeurService distributeurService;

    @PutMapping("/{distributeurId}/agence/{agenceId}")
    public ResponseEntity<String> assignerAgence(
            @PathVariable Long distributeurId,
            @PathVariable Long agenceId) {
        return ResponseEntity.ok(
            distributeurService.assignerAgence(distributeurId, agenceId));
    }

    @GetMapping("/by-agence/{agenceId}")
    public ResponseEntity<DistributeurInfoResponse> getByAgenceId(
            @PathVariable Long agenceId) {
        return ResponseEntity.ok(
            distributeurService.getByAgenceId(agenceId));
    }

    @GetMapping("/by-email")
    public ResponseEntity<DistributeurInfoResponse> getByEmail(
            @RequestParam String email) {
        return ResponseEntity.ok(
            distributeurService.getByEmail(email));
    }

    @DeleteMapping("/by-agence/{agenceId}")
    public ResponseEntity<String> retirerAgence(@PathVariable Long agenceId) {
        return ResponseEntity.ok(
            distributeurService.retirerAgence(agenceId));
    }
}
