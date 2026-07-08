package com.gdg.admin.controller;

import com.gdg.admin.model.ParametrePlateforme;
import com.gdg.admin.service.ParametrePlateformeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/parametres")

public class ParametrePlateformeController {

    @Autowired
    private ParametrePlateformeService parametreService;

    // Lister tous les paramètres
    @GetMapping
    public ResponseEntity<List<ParametrePlateforme>> getAll() {
        return ResponseEntity.ok(parametreService.getAll());
    }

    // Voir un paramètre par clé
    @GetMapping("/{cle}")
    public ResponseEntity<ParametrePlateforme> getByCle(
            @PathVariable String cle) {
        return ResponseEntity.ok(parametreService.getByCle(cle));
    }

    // Modifier un paramètre
    @PutMapping("/{cle}")
    public ResponseEntity<ParametrePlateforme> modifier(
            @PathVariable String cle,
            @RequestBody Map<String, String> body,
            @RequestHeader("X-User-Id") Long adminId,
            HttpServletRequest request) {
        return ResponseEntity.ok(
            parametreService.modifier(
                cle, body.get("valeur"),
                adminId, request.getRemoteAddr()));
    }
}
