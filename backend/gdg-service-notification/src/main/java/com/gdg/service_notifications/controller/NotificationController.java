package com.gdg.service_notifications.controller;

import com.gdg.service_notifications.dto.NotificationRequest;
import com.gdg.service_notifications.model.Notification;
import com.gdg.service_notifications.service.NotificationService;
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
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Créer et envoyer une notification
     * POST /api/notifications
     */
    @PostMapping
    // Créer et envoyer une notification
    public ResponseEntity<Notification> createNotification(
            @Valid @RequestBody NotificationRequest request) {
        log.info("📨 Création d'une notification pour l'utilisateur: {}", request.getUtilisateurId());
        Notification response = notificationService.createAndSendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Récupérer toutes les notifications d'un utilisateur
     * GET /api/notifications/user/{userId}
     */
    @GetMapping("/user/{userId}")
    // Récupérer toutes les notifications d'un utilisateur
    public ResponseEntity<List<Notification>> getNotificationsByUser(
            @PathVariable Long userId) {
        log.info("📋 Récupération des notifications pour l'utilisateur: {}", userId);
        List<Notification> notifications = notificationService.getNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Récupérer les notifications d'un utilisateur avec pagination
     * GET /api/notifications/user/{userId}/paginated
     */
    @GetMapping("/user/{userId}/paginated")
    // Récupérer les notifications d'un utilisateur avec pagination
    public ResponseEntity<Page<Notification>> getNotificationsByUserPaginated(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "dateEnvoi", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("📋 Récupération paginée des notifications pour l'utilisateur: {}", userId);
        Page<Notification> notifications = notificationService.getNotificationsByUserPaginated(userId, pageable);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Récupérer les notifications non lues d'un utilisateur
     * GET /api/notifications/user/{userId}/unread
     */
    @GetMapping("/user/{userId}/unread")
    // Récupérer les notifications non lues d'un utilisateur
    public ResponseEntity<List<Notification>> getNonLuesByUser(
            @PathVariable Long userId) {
        log.info("📋 Récupération des notifications non lues pour l'utilisateur: {}", userId);
        List<Notification> notifications = notificationService.getNonLuesByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Compter les notifications non lues d'un utilisateur
     * GET /api/notifications/user/{userId}/unread/count
     */
    @GetMapping("/user/{userId}/unread/count")
    // Compter les notifications non lues d'un utilisateur
    public ResponseEntity<Long> countNonLuesByUser(
            @PathVariable Long userId) {
        log.info("🔢 Comptage des notifications non lues pour l'utilisateur: {}", userId);
        long count = notificationService.countNonLuesByUser(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Marquer une notification comme lue
     * PUT /api/notifications/{notificationId}/read
     */
    @PutMapping("/{notificationId}/read")
    // Marquer une notification comme lue
    public ResponseEntity<Notification> marquerCommeLu(
            @PathVariable Long notificationId) {
        log.info("✅ Marquage de la notification {} comme lue", notificationId);
        Notification response = notificationService.marquerCommeLu(notificationId);
        return ResponseEntity.ok(response);
    }

    /**
     * Marquer toutes les notifications d'un utilisateur comme lues
     * PUT /api/notifications/user/{userId}/read-all
     */
    @PutMapping("/user/{userId}/read-all")
    // Marquer toutes les notifications d'un utilisateur comme lues
    public ResponseEntity<Void> marquerToutesCommeLues(
            @PathVariable Long userId) {
        log.info("✅ Marquage de toutes les notifications de l'utilisateur {} comme lues", userId);
        notificationService.marquerToutesCommeLues(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint pour recevoir les alertes de seuil critique depuis le service Stock
     * POST /api/notifications/alertes/seuil-critique
     */
    @PostMapping("/alertes/seuil-critique")
    // Recevoir une alerte de seuil critique (appelé par le service Stock)
    public ResponseEntity<Void> recevoirAlerteSeuilCritique(
            @RequestBody com.gdg.service_notifications.dto.AlerteStockRequest alerte) {
        log.info("⚠️ Réception d'une alerte seuil critique pour l'agence {}, produit {}",
                alerte.getAgenceId(), alerte.getCategorieProduitId());
        
        notificationService.envoyerAlerteSeuilCritique(
                alerte.getAgenceId(),
                alerte.getCategorieProduitId(),
                alerte.getLibelleProduit(),
                alerte.getQuantiteDisponible(),
                alerte.getDistributeurId(),
                alerte.getEmailDistributeur()
        );
        
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint pour recevoir les alertes de disponibilité de stock depuis le service Stock
     * POST /api/notifications/alertes/stock-disponible
     */
    @PostMapping("/alertes/stock-disponible")
    // Recevoir une alerte de stock disponible (appelé par le service Stock)
    public ResponseEntity<Void> recevoirAlerteStockDisponible(
            @RequestBody com.gdg.service_notifications.dto.AlerteStockRequest alerte) {
        log.info("📦 Réception d'une alerte stock disponible pour l'agence {}, produit {}",
                alerte.getAgenceId(), alerte.getCategorieProduitId());
        
        notificationService.envoyerNotificationStockDisponible(
                alerte.getAgenceId(),
                alerte.getCategorieProduitId(),
                alerte.getLibelleProduit(),
                alerte.getQuantiteDisponible()
        );
        
        return ResponseEntity.ok().build();
    }
}
