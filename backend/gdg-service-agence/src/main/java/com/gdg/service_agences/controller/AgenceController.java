package com.gdg.service_agences.controller;

import com.gdg.service_agences.dto.*;
import com.gdg.service_agences.model.Agence;
import com.gdg.service_agences.model.Enseigne;
import com.gdg.service_agences.model.HoraireOuverture;
import com.gdg.service_agences.model.Ville;
import com.gdg.service_agences.service.AgenceService;
import com.gdg.service_agences.service.EnseigneService;
import com.gdg.service_agences.service.VilleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agences")
public class AgenceController {

    private final AgenceService agenceService;
    private final EnseigneService enseigneService;
    private final VilleService villeService;

    // ============================================================
    // CONSTRUCTEUR (remplace @RequiredArgsConstructor)
    // ============================================================

    public AgenceController(AgenceService agenceService, EnseigneService enseigneService, VilleService villeService) {
        this.agenceService = agenceService;
        this.enseigneService = enseigneService;
        this.villeService = villeService;
    }

    // ============================================================
    // LISTES
    // ============================================================

    @GetMapping
    public ResponseEntity<List<AgenceSummary>> getAllAgences() {
        List<Agence> agences = agenceService.getAllAgences();
        List<AgenceSummary> summaries = agences.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/actives")
    public ResponseEntity<List<AgenceSummary>> getAgencesActives() {
        List<Agence> agences = agenceService.getAgencesActives();
        List<AgenceSummary> summaries = agences.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/en-attente")
    public ResponseEntity<List<AgenceSummary>> getAgencesEnAttente() {
        List<Agence> agences = agenceService.getAgencesEnAttente();
        List<AgenceSummary> summaries = agences.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    // ============================================================
    // DÉTAIL
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<AgenceResponse> getAgenceById(@PathVariable Long id) {
        Agence agence = agenceService.getAgenceByIdWithHoraires(id);
        return ResponseEntity.ok(mapToResponse(agence));
    }

    // ============================================================
    // RECHERCHES
    // ============================================================

    @GetMapping("/enseigne/{enseigneId}")
    public ResponseEntity<List<AgenceSummary>> getAgencesByEnseigne(@PathVariable Long enseigneId) {
        List<Agence> agences = agenceService.getAgencesByEnseigne(enseigneId);
        List<AgenceSummary> summaries = agences.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/enseigne/{enseigneId}/actives")
    public ResponseEntity<List<AgenceSummary>> getAgencesActivesByEnseigne(@PathVariable Long enseigneId) {
        List<Agence> agences = agenceService.getAgencesActivesByEnseigne(enseigneId);
        List<AgenceSummary> summaries = agences.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/ville/{villeId}")
    public ResponseEntity<List<AgenceSummary>> getAgencesByVille(@PathVariable Long villeId) {
        List<Agence> agences = agenceService.getAgencesByVille(villeId);
        List<AgenceSummary> summaries = agences.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/ville/{villeId}/actives")
    public ResponseEntity<List<AgenceSummary>> getAgencesActivesByVille(@PathVariable Long villeId) {
        List<Agence> agences = agenceService.getAgencesActivesByVille(villeId);
        List<AgenceSummary> summaries = agences.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/enseigne/{enseigneId}/ville/{villeId}")
    public ResponseEntity<List<AgenceSummary>> getAgencesByEnseigneAndVille(
            @PathVariable Long enseigneId,
            @PathVariable Long villeId) {
        List<Agence> agences = agenceService.getAgencesByEnseigneAndVille(enseigneId, villeId);
        List<AgenceSummary> summaries = agences.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<AgenceSummary>> rechercherParNom(@RequestParam String nom) {
        List<Agence> agences = agenceService.rechercherParNom(nom);
        List<AgenceSummary> summaries = agences.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    // ============================================================
    // CRÉATION
    // ============================================================

    @PostMapping
    public ResponseEntity<AgenceResponse> creerAgence(@Valid @RequestBody AgenceRequest request) {
        Enseigne enseigne = enseigneService.getEnseigneById(request.getEnseigneId());
        Ville ville = villeService.getVilleById(request.getVilleId());

        Agence agence = new Agence(
            request.getNom(),
            request.getAdresse(),
            enseigne,
            ville,
            request.getTelephone(),
            request.getEmail()
        );
        agence.setLatitude(request.getLatitude());
        agence.setLongitude(request.getLongitude());

        Agence nouvelleAgence = agenceService.creerAgence(agence, request.getEnseigneId(), request.getVilleId());

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(nouvelleAgence));
    }

    // ============================================================
    // GESTION DES STATUTS
    // ============================================================

    @PutMapping("/{id}/valider")
    public ResponseEntity<AgenceResponse> validerAgence(
            @PathVariable Long id,
            @RequestParam Long adminId) {
        Agence agence = agenceService.validerAgence(id, adminId);
        return ResponseEntity.ok(mapToResponse(agence));
    }

    @PutMapping("/{id}/suspendre")
    public ResponseEntity<AgenceResponse> suspendreAgence(@PathVariable Long id) {
        Agence agence = agenceService.suspendreAgence(id);
        return ResponseEntity.ok(mapToResponse(agence));
    }

    @PutMapping("/{id}/reactiver")
    public ResponseEntity<AgenceResponse> reactiverAgence(@PathVariable Long id) {
        Agence agence = agenceService.reactiverAgence(id);
        return ResponseEntity.ok(mapToResponse(agence));
    }

    // ============================================================
    // MISE À JOUR
    // ============================================================

    @PutMapping("/{id}")
    public ResponseEntity<AgenceResponse> updateAgence(
            @PathVariable Long id,
            @Valid @RequestBody AgenceUpdateRequest request) {
        Agence agenceDetails = new Agence();
        agenceDetails.setNom(request.getNom());
        agenceDetails.setAdresse(request.getAdresse());
        agenceDetails.setLatitude(request.getLatitude());
        agenceDetails.setLongitude(request.getLongitude());
        agenceDetails.setTelephone(request.getTelephone());
        agenceDetails.setEmail(request.getEmail());
        agenceDetails.setLogoFacture(request.getLogoFacture());
        agenceDetails.setEnteteFacture(request.getEnteteFacture());
        agenceDetails.setPiedFacture(request.getPiedFacture());

        Agence updated = agenceService.updateAgence(id, agenceDetails);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    // ============================================================
    // STATISTIQUES
    // ============================================================

    @GetMapping("/statistiques")
    public ResponseEntity<AgenceStatistiquesResponse> getStatistiques() {
        AgenceStatistiquesResponse stats = new AgenceStatistiquesResponse(
            agenceService.compterAgences(),
            agenceService.compterAgencesActives(),
            agenceService.compterAgencesEnAttente(),
            agenceService.compterAgences() - agenceService.compterAgencesActives() - agenceService.compterAgencesEnAttente(),
            enseigneService.getAllEnseignes().size(),
            villeService.getAllVilles().size()
        );
        return ResponseEntity.ok(stats);
    }

    // ============================================================
    // MÉTHODES DE MAPPING
    // ============================================================

    private AgenceSummary mapToSummary(Agence agence) {
        return new AgenceSummary(
            agence.getId(),
            agence.getNom(),
            agence.getAdresse(),
            agence.getTelephone(),
            agence.getEmail(),
            agence.getStatut(),
            agence.getEnseigne() != null ? agence.getEnseigne().getNom() : null,
            agence.getVille() != null ? agence.getVille().getNom() : null,
            agence.getVille() != null ? agence.getVille().getRegion() : null
        );
    }

    private AgenceResponse mapToResponse(Agence agence) {
        EnseigneSummary enseigneSummary = null;
        if (agence.getEnseigne() != null) {
            enseigneSummary = new EnseigneSummary(
                agence.getEnseigne().getId(),
                agence.getEnseigne().getNom(),
                agence.getEnseigne().getLogo(),
                agence.getEnseigne().getStatut(),
                agence.getEnseigne().getAgences() != null ? (long) agence.getEnseigne().getAgences().size() : 0L
            );
        }

        VilleSummary villeSummary = null;
        if (agence.getVille() != null) {
            villeSummary = new VilleSummary(
                agence.getVille().getId(),
                agence.getVille().getNom(),
                agence.getVille().getRegion(),
                agence.getVille().getPays()
            );
        }

        List<HoraireOuvertureDTO> horairesDTO = new ArrayList<>();
        if (agence.getHoraires() != null) {
            horairesDTO = agence.getHoraires().stream()
                .map(h -> new HoraireOuvertureDTO(
                    h.getJourSemaine(),
                    h.getHeureOuverture(),
                    h.getHeureFermeture(),
                    h.getFerme()
                ))
                .collect(Collectors.toList());
        }

        return AgenceResponse.builder()
                .id(agence.getId())
                .nom(agence.getNom())
                .adresse(agence.getAdresse())
                .latitude(agence.getLatitude())
                .longitude(agence.getLongitude())
                .telephone(agence.getTelephone())
                .email(agence.getEmail())
                .logoFacture(agence.getLogoFacture())
                .enteteFacture(agence.getEnteteFacture())
                .piedFacture(agence.getPiedFacture())
                .statut(agence.getStatut())
                .dateCreation(agence.getDateCreation())
                .dateValidation(agence.getDateValidation())
                .validePar(agence.getValidePar())
                .enseigne(enseigneSummary)
                .ville(villeSummary)
                .horaires(horairesDTO)
                .build();
    }
}