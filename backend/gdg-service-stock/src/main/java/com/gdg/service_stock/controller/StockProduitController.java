package com.gdg.service_stock.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdg.service_stock.dtos.DtoStock.ApprovisionnerStockDTO;
import com.gdg.service_stock.dtos.DtoStock.DecrementStockDTO;
import com.gdg.service_stock.dtos.DtoMouvement.MouvementResultDTO;
import com.gdg.service_stock.dtos.DtoMouvement.MouvementStockDTO;
import com.gdg.service_stock.dtos.DtoStock.StockProduitDTO;
import com.gdg.service_stock.dtos.DtoStock.StockPublicDTO;
import com.gdg.service_stock.dtos.DtoStock.UpdateSeuilDTO;
import com.gdg.service_stock.service.StockProduitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Endpoints REST du Service Stock.
 * Base URL : http://localhost:8083/api/stocks
 *
 * Dans l'architecture finale, l'API Gateway redirige vers ce service
 * et vérifie le JWT avant de laisser passer la requête.
 */
@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockProduitController {

    private final StockProduitService stockService;

    // ─── Endpoints publics (visiteur non connecté) ────────────────────────

    /**
     * GET /api/stocks/public/{agenceId}/disponibilite
     * Retourne uniquement disponible=true|false (RG-08).
     * Pas de token requis — l'API Gateway laisse passer sans JWT.
     */
    @GetMapping("/public/{agenceId}/disponibilite")
    public ResponseEntity<StockPublicDTO> getDisponibilitePublique(
            @PathVariable Long agenceId) {
        return ResponseEntity.ok(stockService.getDisponibilitePublique(agenceId));
    }

    // ─── Endpoints consommateur (JWT requis) ──────────────────────────────

    /**
     * GET /api/stocks/agence/{agenceId}
     * Stock détaillé par catégorie (quantités + prix).
     */
    @GetMapping("/agence/{agenceId}")
    public ResponseEntity<List<StockProduitDTO>> getStockParAgence(
            @PathVariable Long agenceId) {
        return ResponseEntity.ok(stockService.getStockParAgence(agenceId));
    }

    /**
     * GET /api/stocks/agence/{agenceId}/categorie/{categorieId}
     * Stock d'un produit précis.
     */
    @GetMapping("/agence/{agenceId}/categorie/{categorieId}")
    public ResponseEntity<StockProduitDTO> getStockPrecis(
            @PathVariable Long agenceId,
            @PathVariable Long categorieId) {
        return ResponseEntity.ok(
            stockService.getStockParAgenceEtCategorie(agenceId, categorieId));
    }

    // ─── Endpoints distributeur ───────────────────────────────────────────

    /**
     * GET /api/stocks/agence/{agenceId}/critiques
     * Stocks en dessous du seuil critique — pour le dashboard distributeur.
     */
    @GetMapping("/agence/{agenceId}/critiques")
    public ResponseEntity<List<StockProduitDTO>> getStocksCritiques(
            @PathVariable Long agenceId) {
        return ResponseEntity.ok(stockService.getStocksCritiques(agenceId));
    }

    /**
     * POST /api/stocks/decrementer
     * Décrémente le stock après une vente (appelé par Service Ventes).
     * Body : { agenceId, categorieProduitId, quantite, referenceExterne, effectuePar }
     */
    @PostMapping("/decrementer")
    public ResponseEntity<MouvementResultDTO> decrementer(
            @Valid @RequestBody DecrementStockDTO dto) {
        return ResponseEntity.ok(stockService.decrementerStock(dto));
    }

    /**
     * POST /api/stocks/approvisionner
     * Incrémente le stock (approvisionnement entrant).
     * Body : { agenceId, categorieProduitId, quantite, fournisseur, numeroBonLivraison }
     */
    @PostMapping("/approvisionner")
    public ResponseEntity<MouvementResultDTO> approvisionner(
            @Valid @RequestBody ApprovisionnerStockDTO dto) {
        return ResponseEntity.ok(stockService.approvisionnerStock(dto));
    }

    /**
     * PATCH /api/stocks/agence/{agenceId}/categorie/{categorieId}/seuil
     * Met à jour le seuil critique (RG-07).
     * Body : { seuilCritique: 10 }
     */
    @PatchMapping("/agence/{agenceId}/categorie/{categorieId}/seuil")
    public ResponseEntity<StockProduitDTO> updateSeuil(
            @PathVariable Long agenceId,
            @PathVariable Long categorieId,
            @Valid @RequestBody UpdateSeuilDTO dto) {
        return ResponseEntity.ok(
            stockService.updateSeuilCritique(agenceId, categorieId, dto));
    }

    /**
     * GET /api/stocks/agence/{agenceId}/historique
     * Historique de tous les mouvements d'une agence (RG-15).
     */
    @GetMapping("/agence/{agenceId}/historique")
    public ResponseEntity<List<MouvementStockDTO>> getHistorique(
            @PathVariable Long agenceId) {
        return ResponseEntity.ok(stockService.getHistorique(agenceId));
    }
    @GetMapping("/global")
    public ResponseEntity<List<StockProduitDTO>>getStockGlobal(){
        return ResponseEntity.ok(stockService.getStockGlobal());
    }
    
    
}
