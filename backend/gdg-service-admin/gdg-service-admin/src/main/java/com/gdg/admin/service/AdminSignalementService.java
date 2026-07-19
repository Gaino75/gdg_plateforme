package com.gdg.admin.service;

import com.gdg.admin.dto.SignalementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class AdminSignalementService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JournalAuditService journalAuditService;

    // @Value("${gdg.notifications.url:http://localhost:8087}")
    // private String notificationsBaseUrl; 
    private static final String NOTIF_URL =
        "http://localhost:8087";

    // Lister tous les signalements
    public List<SignalementDTO> getAllSignalements() {
        SignalementDTO[] s = restTemplate.getForObject(
            NOTIF_URL + "/api/signalements",
            SignalementDTO[].class);
        return s != null ? Arrays.asList(s) : List.of();
    }

    // Valider un signalement
    public void validerSignalement(Long id, Long adminId,
                                    String ip) {
        restTemplate.put(
            NOTIF_URL + "/api/signalements/" + id + "/traiter?statut=CONFIRME&traitePar=" + adminId,
            null);
        journalAuditService.logAction(
            adminId, "ADMIN", "VALIDER_SIGNALEMENT",
            "SIGNALEMENT", id,
            "Validation signalement ID: " + id, ip);
    }

    // Rejeter un signalement
    public void rejeterSignalement(Long id, Long adminId,
                                    String ip) {
        restTemplate.put(
            NOTIF_URL + "/api/signalements/" + id + "/traiter?statut=REJETE&traitePar=" +adminId,
            null);
        journalAuditService.logAction(
            adminId, "ADMIN", "REJETER_SIGNALEMENT",
            "SIGNALEMENT", id,
            "Rejet signalement ID: " + id, ip);
    }
    
}
