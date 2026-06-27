package com.gdg.service_agences.controller;

import com.gdg.service_agences.model.Agence;
import com.gdg.service_agences.model.Enseigne;
import com.gdg.service_agences.service.AgenceService;
import com.gdg.service_agences.service.EnseigneService;
import com.gdg.service_agences.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService fileStorageService;
    private final EnseigneService enseigneService;
    private final AgenceService agenceService;

    // Upload du logo d'une enseigne
    @PutMapping(value = "/enseigne/{enseigneId}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadLogoEnseigne(
            @PathVariable Long enseigneId,
            @RequestParam("file") MultipartFile file) {

        String urlLogo = fileStorageService.sauvegarderFichier(file, "enseignes");
        Enseigne enseigne = enseigneService.getEnseigneById(enseigneId);

        if (enseigne.getLogo() != null) {
            fileStorageService.supprimerFichier(enseigne.getLogo());
        }

        enseigne.setLogo(urlLogo);
        enseigneService.updateEnseigne(enseigneId, enseigne);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logo uploadé avec succès");
        response.put("logoUrl", urlLogo);
        return ResponseEntity.ok(response);
    }

    // Upload du logo de facture d'une agence
    @PutMapping(value = "/agence/{agenceId}/logo-facture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadLogoAgence(
            @PathVariable Long agenceId,
            @RequestParam("file") MultipartFile file) {

        String urlLogo = fileStorageService.sauvegarderFichier(file, "agences");
        Agence agence = agenceService.getAgenceById(agenceId);

        if (agence.getLogoFacture() != null) {
            fileStorageService.supprimerFichier(agence.getLogoFacture());
        }

        agence.setLogoFacture(urlLogo);
        agenceService.updateAgence(agenceId, agence);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logo de facture uploadé avec succès");
        response.put("logoUrl", urlLogo);
        return ResponseEntity.ok(response);
    }
}