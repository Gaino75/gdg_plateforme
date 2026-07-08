package com.gazstation1.payement_service1.controller;

import com.gazstation1.payement_service1.dto.PaiementDTO;
import com.gazstation1.payement_service1.service.PaiementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;

    @PostMapping
    public ResponseEntity<PaiementDTO.Response> creerPaiement(
            @Valid @RequestBody PaiementDTO.CreationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paiementService.creerPaiement(request));
    }

    @PostMapping("/initier")
    public ResponseEntity<PaiementDTO.Response> initierPaiement(
            @Valid @RequestBody PaiementDTO.InitierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paiementService.initierPaiement(request));
    }

    @PostMapping("/callback/orange")
    public ResponseEntity<PaiementDTO.Response> callbackOrange(
            @RequestBody PaiementDTO.CallbackRequest request) {
        return ResponseEntity.ok(paiementService.traiterCallbackOrange(request));
    }

    @PostMapping("/callback/mtn")
    public ResponseEntity<PaiementDTO.Response> callbackMtn(
            @RequestBody PaiementDTO.CallbackRequest request) {
        return ResponseEntity.ok(paiementService.traiterCallbackMtn(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaiementDTO.Response> getPaiement(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.getPaiementById(id));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<PaiementDTO.Response> getPaiementByReference(@PathVariable String reference) {
        return ResponseEntity.ok(paiementService.getPaiementByReference(reference));
    }

    @GetMapping
    public ResponseEntity<List<PaiementDTO.Response>> getAllPaiements() {
        return ResponseEntity.ok(paiementService.getAllPaiements());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PaiementDTO.Response>> getPaiementsByClient(@PathVariable Long clientId) {
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

    @PostMapping("/callback/orange/simuler")
    public ResponseEntity<PaiementDTO.Response> simulerCallbackOrange(
            @RequestBody Map<String, String> body) {
        PaiementDTO.CallbackRequest req = PaiementDTO.CallbackRequest.builder()
                .referenceTransaction(body.get("referenceTransaction"))
                .referenceOperateur(body.getOrDefault("referenceOperateur", "OM-SIM-" + System.currentTimeMillis()))
                .statut(body.getOrDefault("statut", "SUCCESS"))
                .build();
        return ResponseEntity.ok(paiementService.traiterCallbackOrange(req));
    }

    @PostMapping("/callback/mtn/simuler")
    public ResponseEntity<PaiementDTO.Response> simulerCallbackMtn(
            @RequestBody Map<String, String> body) {
        PaiementDTO.CallbackRequest req = PaiementDTO.CallbackRequest.builder()
                .referenceTransaction(body.get("referenceTransaction"))
                .referenceOperateur(body.getOrDefault("referenceOperateur", "MTN-SIM-" + System.currentTimeMillis()))
                .statut(body.getOrDefault("statut", "SUCCESS"))
                .build();
        return ResponseEntity.ok(paiementService.traiterCallbackMtn(req));
    }

    @GetMapping("/a-verifier")
    public ResponseEntity<List<PaiementDTO.Response>>getPaiementsAVerifier(){
         return ResponseEntity.ok(paiementService.getPaiementsAVerifier());
    }

   @PutMapping("/{id}/resoudre")
   public ResponseEntity<PaiementDTO.Response> resoudrePaiement(
              @PathVariable Long id,
              @RequestParam boolean confirmerQuandMeme){
                  return ResponseEntity.ok(paiementService.resoudrePaiementAVerifier(id,confirmerQuandMeme));
              }
    
}
