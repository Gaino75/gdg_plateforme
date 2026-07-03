package com.gdg.admin.service;

import com.gdg.admin.config.RabbitMQConfig;
import com.gdg.admin.dto.AgenceDTO;
import com.gdg.admin.dto.AgenceDetailResponse;
import com.gdg.admin.dto.AgenceSummaryResponse;
import com.gdg.admin.dto.DistributeurInfoDTO;
import com.gdg.admin.event.AgenceValideeEvent;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminAgenceService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JournalAuditService journalAuditService;

    @Value("${gdg.agences.url:http://localhost:8082}")
    private String agencesBaseUrl;

    @Value("${gdg.auth.url:http://localhost:8081}")
    private String authBaseUrl;

    public List<AgenceDTO> getAgencesEnAttente() {
        AgenceSummaryResponse[] agences = restTemplate.getForObject(
            agencesBaseUrl + "/api/agences/en-attente",
            AgenceSummaryResponse[].class);
        if (agences == null) {
            return List.of();
        }
        return Arrays.stream(agences)
            .map(this::toAgenceDTO)
            .collect(Collectors.toList());
    }

    public void validerAgence(Long agenceId, Long adminId, String ip) {
        String url = UriComponentsBuilder
            .fromHttpUrl(agencesBaseUrl + "/api/agences/" + agenceId + "/valider")
            .queryParam("adminId", adminId)
            .toUriString();

        restTemplate.put(url, null);

        AgenceDetailResponse agence = restTemplate.getForObject(
            agencesBaseUrl + "/api/agences/" + agenceId,
            AgenceDetailResponse.class);

        DistributeurInfoDTO distributeur = restTemplate.getForObject(
            authBaseUrl + "/auth/internal/distributeurs/by-agence/" + agenceId,
            DistributeurInfoDTO.class);

        journalAuditService.logAction(
            adminId, "ADMIN", "VALIDER_AGENCE",
            "AGENCE", agenceId,
            "Validation agence ID: " + agenceId, ip);

        String nomAgence = agence != null ? agence.getNom() : "";
        Long distributeurId = distributeur != null ? distributeur.getId() : null;
        String email = distributeur != null ? distributeur.getEmail() : null;

        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.KEY_AGENCE_VALIDEE,
            new AgenceValideeEvent(
                agenceId, nomAgence, adminId, distributeurId, email));
    }

    public void rejeterAgence(Long agenceId, String motif, Long adminId, String ip) {
        restTemplate.put(
            agencesBaseUrl + "/api/agences/" + agenceId + "/rejeter",
            null);
        journalAuditService.logAction(
            adminId, "ADMIN", "REJETER_AGENCE",
            "AGENCE", agenceId,
            "Rejet agence ID: " + agenceId + " | Motif: " + motif, ip);
    }

    public void suspendreAgence(Long agenceId, String motif, Long adminId, String ip) {
        restTemplate.put(
            agencesBaseUrl + "/api/agences/" + agenceId + "/suspendre",
            null);
        journalAuditService.logAction(
            adminId, "ADMIN", "SUSPENDRE_AGENCE",
            "AGENCE", agenceId,
            "Suspension agence ID: " + agenceId + " | Motif: " + motif, ip);
    }

    public void reactiverAgence(Long agenceId, Long adminId, String ip) {
        restTemplate.put(
            agencesBaseUrl + "/api/agences/" + agenceId + "/reactiver",
            null);
        journalAuditService.logAction(
            adminId, "ADMIN", "REACTIVER_AGENCE",
            "AGENCE", agenceId,
            "Réactivation agence ID: " + agenceId, ip);
    }

    private AgenceDTO toAgenceDTO(AgenceSummaryResponse summary) {
        AgenceDTO dto = new AgenceDTO();
        dto.setId(summary.getId());
        dto.setNom(summary.getNom());
        dto.setAdresse(summary.getAdresse());
        dto.setStatut(summary.getStatut());
        dto.setTelephone(summary.getTelephone());
        return dto;
    }
}
