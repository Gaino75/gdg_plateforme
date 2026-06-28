package com.gdg.admin.service;

import com.gdg.admin.config.RabbitMQConfig;
import com.gdg.admin.dto.AgenceDTO;
import com.gdg.admin.event.AgenceValideeEvent;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class AdminAgenceService {

    @Autowired
private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JournalAuditService journalAuditService;

    private static final String AGENCES_URL =
        "http://localhost:8082";

    // Récupérer agences en attente de validation
    public List<AgenceDTO> getAgencesEnAttente() {
        AgenceDTO[] agences = restTemplate.getForObject(
            AGENCES_URL + "/agences?statut=EN_ATTENTE",
            AgenceDTO[].class);
        return agences != null ?
            Arrays.asList(agences) : List.of();
    }

    // Valider une agence
    public void validerAgence(Long agenceId, Long adminId,
                               String ip) {
        restTemplate.put(
            AGENCES_URL + "/agences/" + agenceId + "/valider",
            null);
        journalAuditService.logAction(
            adminId, "ADMIN", "VALIDER_AGENCE",
            "AGENCE", agenceId,
            "Validation agence ID: " + agenceId, ip);

            rabbitTemplate.convertAndSend(
    RabbitMQConfig.EXCHANGE,
    RabbitMQConfig.KEY_AGENCE_VALIDEE,
    new AgenceValideeEvent(agenceId, "", adminId)
);
    }

    // Rejeter une agence
    public void rejeterAgence(Long agenceId, String motif,
                               Long adminId, String ip) {
        restTemplate.put(
            AGENCES_URL + "/agences/" + agenceId + "/rejeter",
            motif);
        journalAuditService.logAction(
            adminId, "ADMIN", "REJETER_AGENCE",
            "AGENCE", agenceId,
            "Rejet agence ID: " + agenceId
            + " | Motif: " + motif, ip);
    }

    // Suspendre une agence
    public void suspendreAgence(Long agenceId, String motif,
                                 Long adminId, String ip) {
        restTemplate.put(
            AGENCES_URL + "/agences/" + agenceId + "/suspendre",
            motif);
        journalAuditService.logAction(
            adminId, "ADMIN", "SUSPENDRE_AGENCE",
            "AGENCE", agenceId,
            "Suspension agence ID: " + agenceId
            + " | Motif: " + motif, ip);
    }

    // Réactiver une agence
    public void reactiverAgence(Long agenceId, Long adminId,
                                 String ip) {
        restTemplate.put(
            AGENCES_URL + "/agences/" + agenceId + "/reactiver",
            null);
        journalAuditService.logAction(
            adminId, "ADMIN", "REACTIVER_AGENCE",
            "AGENCE", agenceId,
            "Réactivation agence ID: " + agenceId, ip);
    }
}
