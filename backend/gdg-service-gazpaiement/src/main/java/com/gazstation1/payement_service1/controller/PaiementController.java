package com.gazstation1.payement_service1.controller;

import com.gazstation1.payement_service1.dto.PaiementDTO;
import com.gazstation1.payement_service1.service.PaiementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;

    @PostMapping
    public ResponseEntity<PaiementDTO.Response> creerPaiement(
            @Valid @RequestBody PaiementDTO.CreationRequest request) {
        PaiementDTO.Response response = paiementService.creerPaiement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaiementDTO.Response> getPaiement(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.getPaiementById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaiementDTO.Response>> getAllPaiements() {
        return ResponseEntity.ok(paiementService.getAllPaiements());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PaiementDTO.Response>> getPaiementsByClient(
            @PathVariable Long clientId) {
        return ResponseEntity.ok(paiementService.getPaiementsByClient(clientId));
    }

    @PutMapping("/{id}/valider")
    public ResponseEntity<PaiementDTO.Response> validerPaiement(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.validerPaiement(id));
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<PaiementDTO.Response> annulerPaiement(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.annulerPaiement(id));
    }
}
