package com.gdg.admin.service;

import com.gdg.admin.config.RabbitMQConfig;
import com.gdg.admin.dto.UtilisateurDTO;
import com.gdg.admin.event.UserSuspendedEvent;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminUtilisateurService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JournalAuditService journalAuditService;

    @Value("${gdg.auth.url:http://localhost:8081}")
    private String authBaseUrl;

    public List<UtilisateurDTO> getAllUtilisateurs() {
        UtilisateurDTO[] users = restTemplate.getForObject(
            authBaseUrl + "/auth/admin/users",
            UtilisateurDTO[].class);
        return users != null ? Arrays.asList(users) : List.of();
    }

    public void suspendreUtilisateur(Long userId, String motif, Long adminId, String ip) {
        Map<String, String> body = new HashMap<>();
        body.put("motif", motif);
        restTemplate.put(
            authBaseUrl + "/auth/admin/utilisateurs/" + userId + "/suspendre",
            body);

        String email = "";
        try {
            UtilisateurDTO[] users = restTemplate.getForObject(
                authBaseUrl + "/auth/admin/users",
                UtilisateurDTO[].class);
            if (users != null) {
                email = Arrays.stream(users)
                    .filter(u -> userId.equals(u.getId()))
                    .map(UtilisateurDTO::getEmail)
                    .findFirst()
                    .orElse("");
            }
        } catch (Exception ignored) {
            // Email optionnel pour la notification
        }

        journalAuditService.logAction(
            adminId, "ADMIN", "SUSPENDRE_UTILISATEUR",
            "UTILISATEUR", userId,
            "Suspension ID: " + userId + " | Motif: " + motif, ip);

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.KEY_USER_SUSPENDED,
            new UserSuspendedEvent(userId, email, motif, adminId));
    }

    public void reactiverUtilisateur(Long userId, Long adminId, String ip) {
        restTemplate.put(
            authBaseUrl + "/auth/admin/utilisateurs/" + userId + "/reactiver",
            null);
        journalAuditService.logAction(
            adminId, "ADMIN", "REACTIVER_UTILISATEUR",
            "UTILISATEUR", userId,
            "Réactivation ID: " + userId, ip);
    }

    public void supprimerUtilisateur(Long userId, Long adminId, String ip) {
        restTemplate.delete(
            authBaseUrl + "/auth/admin/utilisateurs/" + userId);
        journalAuditService.logAction(
            adminId, "ADMIN", "SUPPRIMER_UTILISATEUR",
            "UTILISATEUR", userId,
            "Suppression ID: " + userId, ip);
    }
}
