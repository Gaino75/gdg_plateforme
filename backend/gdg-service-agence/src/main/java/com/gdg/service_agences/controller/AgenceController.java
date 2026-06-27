package com.gdg.service_agences.controller;

import com.gdg.service_agences.model.Agence;
import com.gdg.service_agences.model.Agence.StatutAgence;
import com.gdg.service_agences.service.AgenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agences")
@RequiredArgsConstructor
public class AgenceController {

    private final AgenceService agenceService;

    // GET /api/agences
    @GetMapping
    public ResponseEntity<List<Agence>> getAllAgences() {
        return ResponseEntity.ok(agenceService.getAllAgences());
    }

    // GET /api/agences/actives
    @GetMapping("/actives")
    public ResponseEntity<List<Agence>> getAgencesActives() {
        return ResponseEntity.ok(agenceService.getAgencesActives());
    }

    // GET /api/agences/actives/avec-horaires
    @GetMapping("/actives/avec-horaires")
    public ResponseEntity<List<Agence>> getAgencesActivesWithHoraires() {
        return ResponseEntity.ok(agenceService.getAgencesActivesWithHoraires());
    }

    // GET /api/agences/en-attente
    @GetMapping("/en-attente")
    public ResponseEntity<List<Agence>> getAgencesEnAttente() {
        return ResponseEntity.ok(agenceService.getAgencesEnAttente());
    }

    // GET /api/agences/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Agence> getAgenceById(@PathVariable Long id) {
        return ResponseEntity.ok(agenceService.getAgenceById(id));
    }

    // GET /api/agences/{id}/avec-horaires
    @GetMapping("/{id}/avec-horaires")
    public ResponseEntity<Agence> getAgenceByIdWithHoraires(@PathVariable Long id) {
        return ResponseEntity.ok(agenceService.getAgenceByIdWithHoraires(id));
    }

    // GET /api/agences/enseigne/{enseigneId}
    @GetMapping("/enseigne/{enseigneId}")
    public ResponseEntity<List<Agence>> getAgencesByEnseigne(@PathVariable Long enseigneId) {
        return ResponseEntity.ok(agenceService.getAgencesByEnseigne(enseigneId));
    }

    // GET /api/agences/enseigne/{enseigneId}/actives
    @GetMapping("/enseigne/{enseigneId}/actives")
    public ResponseEntity<List<Agence>> getAgencesActivesByEnseigne(@PathVariable Long enseigneId) {
        return ResponseEntity.ok(agenceService.getAgencesActivesByEnseigne(enseigneId));
    }

    // GET /api/agences/ville/{villeId}
    @GetMapping("/ville/{villeId}")
    public ResponseEntity<List<Agence>> getAgencesByVille(@PathVariable Long villeId) {
        return ResponseEntity.ok(agenceService.getAgencesByVille(villeId));
    }

    // GET /api/agences/ville/{villeId}/actives
    @GetMapping("/ville/{villeId}/actives")
    public ResponseEntity<List<Agence>> getAgencesActivesByVille(@PathVariable Long villeId) {
        return ResponseEntity.ok(agenceService.getAgencesActivesByVille(villeId));
    }

    // GET /api/agences/enseigne/{enseigneId}/ville/{villeId}
    @GetMapping("/enseigne/{enseigneId}/ville/{villeId}")
    public ResponseEntity<List<Agence>> getAgencesByEnseigneAndVille(
            @PathVariable Long enseigneId,
            @PathVariable Long villeId) {
        return ResponseEntity.ok(agenceService.getAgencesByEnseigneAndVille(enseigneId, villeId));
    }

    // GET /api/agences/recherche?nom=total
    @GetMapping("/recherche")
    public ResponseEntity<List<Agence>> rechercherParNom(@RequestParam String nom) {
        return ResponseEntity.ok(agenceService.rechercherParNom(nom));
    }

    // POST /api/agences
    @PostMapping
    public ResponseEntity<Agence> creerAgence(@Valid @RequestBody AgenceCreationRequest request) {
        Agence agence = new Agence(
            request.getNom(),
            request.getAdresse(),
            null,  // sera défini par le service
            null,  // sera défini par le service
            request.getTelephone(),
            request.getEmail()
        );
        Agence nouvelleAgence = agenceService.creerAgence(agence, request.getEnseigneId(), request.getVilleId());
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleAgence);
    }

    // PUT /api/agences/{id}/valider
    @PutMapping("/{id}/valider")
    public ResponseEntity<Agence> validerAgence(
            @PathVariable Long id,
            @RequestParam Long adminId) {
        return ResponseEntity.ok(agenceService.validerAgence(id, adminId));
    }

    // PUT /api/agences/{id}/suspendre
    @PutMapping("/{id}/suspendre")
    public ResponseEntity<Agence> suspendreAgence(@PathVariable Long id) {
        return ResponseEntity.ok(agenceService.suspendreAgence(id));
    }

    // PUT /api/agences/{id}/reactiver
    @PutMapping("/{id}/reactiver")
    public ResponseEntity<Agence> reactiverAgence(@PathVariable Long id) {
        return ResponseEntity.ok(agenceService.reactiverAgence(id));
    }

    // PUT /api/agences/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Agence> updateAgence(
            @PathVariable Long id,
            @Valid @RequestBody Agence agence) {
        return ResponseEntity.ok(agenceService.updateAgence(id, agence));
    }

    // GET /api/agences/statistiques
    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Long>> getStatistiques() {
        return ResponseEntity.ok(Map.of(
            "total", agenceService.compterAgences(),
            "actives", agenceService.compterAgencesActives(),
            "enAttente", agenceService.compterAgencesEnAttente()
        ));
    }
}

// DTO pour la création d'agence
class AgenceCreationRequest {
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private Long enseigneId;
    private Long villeId;

    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Long getEnseigneId() { return enseigneId; }
    public void setEnseigneId(Long enseigneId) { this.enseigneId = enseigneId; }
    public Long getVilleId() { return villeId; }
    public void setVilleId(Long villeId) { this.villeId = villeId; }
}