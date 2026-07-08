package com.gdg.service_ventes.controller;

import com.gdg.service_ventes.dto.ApprovisionnementRequest;
import com.gdg.service_ventes.service.StockServiceClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/approvisionnements")
@RequiredArgsConstructor
public class ApprovisionnementController {

    private final StockServiceClient stockServiceClient;

    @PostMapping
    public ResponseEntity<Map<String, String>> enregistrerApprovisionnement(
            @Valid @RequestBody ApprovisionnementRequest request) {
        log.info("Approvisionnement agence={} categorie={} qte={}",
                request.getAgenceId(), request.getCategorieProduitId(), request.getQuantite());

        stockServiceClient.approvisionnerStock(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Approvisionnement enregistré avec succès"));
    }
}
