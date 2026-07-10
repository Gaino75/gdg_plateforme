package com.gdg.service_ventes.controller;

import com.gdg.service_ventes.dto.VenteRequest;
import com.gdg.service_ventes.dto.VenteResponse;
import com.gdg.service_ventes.model.Vente;
import com.gdg.service_ventes.service.VenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final VenteService venteService;

    @PostMapping
    public ResponseEntity<VenteResponse> enregistrerVente(@Valid @RequestBody VenteRequest request) {
        log.info("📝 Enregistrement d'une vente pour l'agence: {}", request.getAgenceId());
        VenteResponse response = venteService.enregistrerVente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/agence/{agenceId}")
    public ResponseEntity<List<Vente>> getVentesByAgence(@PathVariable Long agenceId) {
        log.info("📋 Récupération des ventes pour l'agence: {}", agenceId);
        List<Vente> ventes = venteService.getVentesByAgence(agenceId);
        return ResponseEntity.ok(ventes);
    }

    @GetMapping("/agence/{agenceId}/paginated")
    public ResponseEntity<Page<Vente>> getVentesByAgencePaginated(
            @PathVariable Long agenceId,
            @PageableDefault(size = 10, sort = "dateVente", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("📋 Récupération paginée des ventes pour l'agence: {}", agenceId);
        Page<Vente> ventes = venteService.getVentesByAgencePaginated(agenceId, pageable);
        return ResponseEntity.ok(ventes);
    }

    @GetMapping("/distributeur/{distributeurId}")
    public ResponseEntity<List<Vente>> getVentesByDistributeur(@PathVariable Long distributeurId) {
        log.info("📋 Récupération des ventes pour le distributeur: {}", distributeurId);
        List<Vente> ventes = venteService.getVentesByDistributeur(distributeurId);
        return ResponseEntity.ok(ventes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vente> getVenteById(@PathVariable Long id) {
        log.info("📋 Récupération de la vente: {}", id);
        Vente vente = venteService.getVenteById(id);
        return ResponseEntity.ok(vente);
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<Vente> getVenteByReference(@PathVariable String reference) {
        log.info("📋 Récupération de la vente par référence: {}", reference);
        Vente vente = venteService.getVenteByReference(reference);
        return ResponseEntity.ok(vente);
    }

    @GetMapping("/agence/{agenceId}/ca")
    public ResponseEntity<Double> getChiffreAffaire(@PathVariable Long agenceId) {
        log.info("💰 Récupération du CA pour l'agence: {}", agenceId);
        Double ca = venteService.getChiffreAffaire(agenceId);
        return ResponseEntity.ok(ca);
    }

    @GetMapping("/agence/{agenceId}/categorie/{categorieId}/quantite")
    public ResponseEntity<Integer> getQuantiteVendueParCategorie(
            @PathVariable Long agenceId,
            @PathVariable Long categorieId) {
        log.info("📊 Récupération de la quantité vendue pour l'agence: {}, catégorie: {}", agenceId, categorieId);
        Integer quantite = venteService.getQuantiteVendueParCategorie(agenceId, categorieId);
        return ResponseEntity.ok(quantite);
    }

    @GetMapping("/agence/{agenceId}/count")
    public ResponseEntity<Long> countVentesByAgence(@PathVariable Long agenceId) {
        log.info("🔢 Comptage des ventes pour l'agence: {}", agenceId);
        long count = venteService.countVentesByAgence(agenceId);
        return ResponseEntity.ok(count);
    }
}
