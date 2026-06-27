package com.gdg.service_agences.controller;

import com.gdg.service_agences.dto.VilleRequest;
import com.gdg.service_agences.dto.VilleResponse;
import com.gdg.service_agences.dto.VilleSummary;
import com.gdg.service_agences.model.Ville;
import com.gdg.service_agences.service.VilleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/villes")
public class VilleController {

    private final VilleService villeService;

    // ============================================================
    // CONSTRUCTEUR (remplace @RequiredArgsConstructor)
    // ============================================================

    public VilleController(VilleService villeService) {
        this.villeService = villeService;
    }

    // ============================================================
    // MÉTHODES
    // ============================================================

    @GetMapping
    public ResponseEntity<List<VilleSummary>> getAllVilles() {
        List<Ville> villes = villeService.getAllVilles();
        List<VilleSummary> summaries = villes.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VilleResponse> getVilleById(@PathVariable Long id) {
        Ville ville = villeService.getVilleById(id);
        return ResponseEntity.ok(mapToResponse(ville));
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<VilleSummary>> getVillesByRegion(@PathVariable String region) {
        List<Ville> villes = villeService.getVillesByRegion(region);
        List<VilleSummary> summaries = villes.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @PostMapping
    public ResponseEntity<VilleResponse> creerVille(@Valid @RequestBody VilleRequest request) {
        Ville ville = new Ville(request.getNom(), request.getRegion(), request.getPays());
        Ville nouvelleVille = villeService.creerVille(ville);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(nouvelleVille));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VilleResponse> updateVille(
            @PathVariable Long id,
            @Valid @RequestBody VilleRequest request) {
        Ville ville = new Ville(request.getNom(), request.getRegion(), request.getPays());
        ville.setId(id);
        Ville updated = villeService.updateVille(id, ville);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerVille(@PathVariable Long id) {
        villeService.supprimerVille(id);
        return ResponseEntity.noContent().build();
    }

    // ============================================================
    // MÉTHODES DE MAPPING
    // ============================================================

    private VilleSummary mapToSummary(Ville ville) {
        return new VilleSummary(ville.getId(), ville.getNom(), ville.getRegion(), ville.getPays());
    }

    private VilleResponse mapToResponse(Ville ville) {
        return VilleResponse.builder()
                .id(ville.getId())
                .nom(ville.getNom())
                .region(ville.getRegion())
                .pays(ville.getPays())
                .nombreAgences(ville.getAgences() != null ? (long) ville.getAgences().size() : 0L)
                .build();
    }
}