// service/AbonnementService.java
package com.gdg.service_notifications.service;

import com.gdg.service_notifications.dto.AbonnementRequest;
import com.gdg.service_notifications.model.Abonnement;
import com.gdg.service_notifications.repository.AbonnementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbonnementService {

    private final AbonnementRepository abonnementRepository;

    @Transactional
    public Abonnement creerAbonnement(AbonnementRequest request) {
        // Vérifier si l'abonnement existe déjà
        Optional<Abonnement> existing = abonnementRepository.findAbonnementActif(
                request.getConsommateurId(),
                request.getAgenceId(),
                request.getCategorieProduitId()
        );

        if (existing.isPresent()) {
            log.info("Abonnement déjà existant pour consommateur {}, agence {}, catégorie {}",
                    request.getConsommateurId(), request.getAgenceId(), request.getCategorieProduitId());
            return existing.get();
        }

        Abonnement abonnement = Abonnement.builder()
                .consommateurId(request.getConsommateurId())
                .agenceId(request.getAgenceId())
                .categorieProduitId(request.getCategorieProduitId())
                .actif(true)
                .build();

        Abonnement saved = abonnementRepository.save(abonnement);
        log.info("Abonnement créé avec ID: {}", saved.getId());
        return saved;
    }

    @Transactional
    public void desactiverAbonnement(Long consommateurId, Long agenceId, Long categorieProduitId) {
        abonnementRepository.desactiverAbonnement(consommateurId, agenceId, categorieProduitId);
        log.info("Abonnement désactivé pour consommateur {}, agence {}, catégorie {}",
                consommateurId, agenceId, categorieProduitId);
    }

    public List<Abonnement> getAbonnementsByConsommateur(Long consommateurId) {
        return abonnementRepository.findByConsommateurIdAndActifTrue(consommateurId);
    }

    public List<Abonnement> getAbonnesPourAgence(Long agenceId) {
        return abonnementRepository.findByAgenceIdAndActifTrue(agenceId);
    }

    public boolean isAbonne(Long consommateurId, Long agenceId) {
        Optional<Abonnement> abonnement = abonnementRepository.findAbonnementActif(
                consommateurId, agenceId, null
        );
        return abonnement.isPresent();
    }
}
