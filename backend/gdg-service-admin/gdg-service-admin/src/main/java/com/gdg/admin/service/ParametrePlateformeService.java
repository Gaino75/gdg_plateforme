package com.gdg.admin.service;

import com.gdg.admin.model.ParametrePlateforme;
import com.gdg.admin.repository.ParametrePlateformeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParametrePlateformeService {

    @Autowired
    private ParametrePlateformeRepository parametreRepository;

    @Autowired
    private JournalAuditService journalAuditService;

    public List<ParametrePlateforme> getAll() {
        return parametreRepository.findAll();
    }

    public ParametrePlateforme getByCle(String cle) {
        return parametreRepository.findByCle(cle)
            .orElseThrow(() -> new RuntimeException(
                "Paramètre introuvable : " + cle));
    }

    public ParametrePlateforme modifier(String cle,
            String nouvelleValeur, Long adminId, String ip) {
        ParametrePlateforme param = getByCle(cle);
        String ancien = param.getValeur();
        param.setValeur(nouvelleValeur);
        param.setModifiePar(adminId);
        param.setDateModification(LocalDateTime.now());
        ParametrePlateforme saved =
            parametreRepository.save(param);
        journalAuditService.logAction(
            adminId, "ADMIN", "MODIFIER_PARAMETRE",
            "PARAMETRE", param.getId(),
            "Clé: " + cle + " | Avant: " + ancien
            + " | Après: " + nouvelleValeur, ip);
        return saved;
    }
}
