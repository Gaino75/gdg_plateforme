package com.gdg.service_agences.controller;

import com.gdg.service_agences.model.Enseigne;
import com.gdg.service_agences.service.EnseigneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enseignes")
@RequiredArgsConstructor
public class EnseigneController {

    private final EnseigneService enseigneService;

    // GET /api/enseignes → Récupérer toutes les enseignes
    @GetMapping
    public ResponseEntity<List<Enseigne>> getAllEnseignes() {
        return ResponseEntity.ok(enseigneService.getAllEnseignes());
    }

    // GET /api/enseignes/actives → Récupérer les enseignes actives uniquement
    @GetMapping("/actives")
    public ResponseEntity<List<Enseigne>> getEnseignesActives() {
        return ResponseEntity.ok(enseigneService.getEnseignesActives());
    }

    // GET /api/enseignes/{id} → Récupérer une enseigne par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Enseigne> getEnseigneById(@PathVariable Long id) {
        return ResponseEntity.ok(enseigneService.getEnseigneById(id));
    }

    // POST /api/enseignes → Créer une nouvelle enseigne
    @PostMapping
    public ResponseEntity<Enseigne> creerEnseigne(@RequestBody Enseigne enseigne) {
        Enseigne nouvelleEnseigne = enseigneService.creerEnseigne(enseigne);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleEnseigne);
    }

    // PUT /api/enseignes/{id} → Mettre à jour une enseigne
    @PutMapping("/{id}")
    public ResponseEntity<Enseigne> updateEnseigne(@PathVariable Long id, @RequestBody Enseigne enseigne) {
        return ResponseEntity.ok(enseigneService.updateEnseigne(id, enseigne));
    }

    // DELETE /api/enseignes/{id} → Supprimer une enseigne
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEnseigne(@PathVariable Long id) {
        enseigneService.supprimerEnseigne(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /api/enseignes/{id}/desactiver → Désactiver une enseigne
    @PutMapping("/{id}/desactiver")
    public ResponseEntity<Void> desactiverEnseigne(@PathVariable Long id) {
        enseigneService.desactiverEnseigne(id);
        return ResponseEntity.ok().build();
    }
}