package com.gdg.admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Service distant indisponible
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>>
            handleServiceIndisponible(
            ResourceAccessException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 503);
        error.put("error", "Service indisponible");
        error.put("message",
            "Un service distant ne répond pas. "
            + "Réessayez plus tard.");
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(error);
    }

    // Ressource non trouvée ou erreur métier
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>>
            handleRuntimeException(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 400);
        error.put("error", "Erreur");
        error.put("message", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
    }

    // Accès non autorisé
    @ExceptionHandler(
        org.springframework.security
        .access.AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>>
            handleAccessDenied(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", 403);
        error.put("error", "Accès refusé");
        error.put("message",
            "Vous n'avez pas les droits nécessaires");
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(error);
    }
}
