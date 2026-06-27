package com.gdg.service_agences.controller;

import com.gdg.service_agences.model.Ville;
import com.gdg.service_agences.service.VilleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/villes")
@RequiredArgsConstructor
public class VilleController {

    private final VilleService villeService;

    // GET /api/villes → Récupérer toutes les villes
    @GetMapping
    public ResponseEntity<List<Ville>> getAllVilles() {
        return ResponseEntity.ok(villeService.getAllVilles());
    }

    // GET /api/villes/{id} → Récupérer une ville par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Ville> getVilleById(@PathVariable Long id) {
        return ResponseEntity.ok(villeService.getVilleById(id));
    }

    // GET /api/villes/region/{region} → Récupérer les villes par région
    @GetMapping("/region/{region}")
    public ResponseEntity<List<Ville>> getVillesByRegion(@PathVariable String region) {
        return ResponseEntity.ok(villeService.getVillesByRegion(region));
    }

    // POST /api/villes → Créer une nouvelle ville
    @PostMapping
    public ResponseEntity<Ville> creerVille(@RequestBody Ville ville) {
        Ville nouvelleVille = villeService.creerVille(ville);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleVille);
    }

    // PUT /api/villes/{id} → Mettre à jour une ville
    @PutMapping("/{id}")
    public ResponseEntity<Ville> updateVille(@PathVariable Long id, @RequestBody Ville ville) {
        return ResponseEntity.ok(villeService.updateVille(id, ville));
    }

    // DELETE /api/villes/{id} → Supprimer une ville
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerVille(@PathVariable Long id) {
        villeService.supprimerVille(id);
        return ResponseEntity.noContent().build();
    }
}