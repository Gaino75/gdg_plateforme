package com.gdg.admin.service;

import com.gdg.admin.config.RabbitMQConfig;
import com.gdg.admin.dto.UtilisateurDTO;
import com.gdg.admin.event.UserSuspendedEvent;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class AdminUtilisateurService {

    @Autowired
private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JournalAuditService journalAuditService;

    private static final String AUTH_URL =
        "http://localhost:8081";

    // Lister tous les utilisateurs
    public List<UtilisateurDTO> getAllUtilisateurs() {
        UtilisateurDTO[] users = restTemplate.getForObject(
            AUTH_URL + "/admin/utilisateurs",
            UtilisateurDTO[].class);
        return users != null ?
            Arrays.asList(users) : List.of();
    }

    // Suspendre un utilisateur
    public void suspendreUtilisateur(Long userId, String motif,
                                      Long adminId, String ip) {
        restTemplate.put(
            AUTH_URL + "/admin/utilisateurs/"
            + userId + "/suspendre",
            motif);
        journalAuditService.logAction(
            adminId, "ADMIN", "SUSPENDRE_UTILISATEUR",
            "UTILISATEUR", userId,
            "Suspension ID: " + userId
            + " | Motif: " + motif, ip);
            rabbitTemplate.convertAndSend(
    RabbitMQConfig.EXCHANGE,
    RabbitMQConfig.KEY_USER_SUSPENDED,
    new UserSuspendedEvent(userId, "", motif, adminId)
);
    }

    // Réactiver un utilisateur
    public void reactiverUtilisateur(Long userId,
                                      Long adminId, String ip) {
        restTemplate.put(
            AUTH_URL + "/admin/utilisateurs/"
            + userId + "/reactiver",
            null);
        journalAuditService.logAction(
            adminId, "ADMIN", "REACTIVER_UTILISATEUR",
            "UTILISATEUR", userId,
            "Réactivation ID: " + userId, ip);
    }

    // Supprimer un utilisateur
    public void supprimerUtilisateur(Long userId,
                                      Long adminId, String ip) {
        restTemplate.delete(
            AUTH_URL + "/admin/utilisateurs/" + userId);
        journalAuditService.logAction(
            adminId, "ADMIN", "SUPPRIMER_UTILISATEUR",
            "UTILISATEUR", userId,
            "Suppression ID: " + userId, ip);
    }
}
