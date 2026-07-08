package com.gdg.service_ventes.controller;

import com.gdg.service_ventes.dto.FactureResponse;
import com.gdg.service_ventes.model.Facture;
import com.gdg.service_ventes.service.FactureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {

    private final FactureService factureService;

    @GetMapping("/{id}")
    public ResponseEntity<FactureResponse> getFactureById(@PathVariable Long id) {
        log.info("📋 Récupération de la facture: {}", id);
        Facture facture = factureService.getFactureById(id);
        return ResponseEntity.ok(factureService.buildFactureResponse(facture));
    }

    @GetMapping("/vente/{venteId}")
    public ResponseEntity<FactureResponse> getFactureByVenteId(@PathVariable Long venteId) {
        log.info("📋 Récupération de la facture pour la vente: {}", venteId);
        Facture facture = factureService.getFactureByVenteId(venteId);
        return ResponseEntity.ok(factureService.buildFactureResponse(facture));
    }

    @GetMapping("/agence/{agenceId}")
    public ResponseEntity<List<Facture>> getFacturesByAgence(@PathVariable Long agenceId) {
        log.info("📋 Récupération des factures pour l'agence: {}", agenceId);
        List<Facture> factures = factureService.getFacturesByAgence(agenceId);
        return ResponseEntity.ok(factures);
    }
}