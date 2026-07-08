package com.gdg.service_notifications.service;

import com.gdg.service_notifications.dto.SignalementRequest;
import com.gdg.service_notifications.model.Signalement;
import com.gdg.service_notifications.repository.SignalementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignalementService {

    private final SignalementRepository signalementRepository;

    @Value("${app.notifications.nb-signalements-confirmation:2}")
    private int nbSignalementsConfirmation;

    @Value("${app.notifications.delai-signalement-heures:2}")
    private int delaiSignalementHeures;

    

    @Transactional
    public Signalement creerSignalement(SignalementRequest request) {
        // Vérifier si l'utilisateur n'a pas déjà signalé récemment
        LocalDateTime delai = LocalDateTime.now().minusHours(delaiSignalementHeures);
        boolean dejaSignale = signalementRepository.existsByConsommateurIdAndAgenceIdAndCategorieProduitIdAndDateSignalementAfter(
                request.getConsommateurId(),
                request.getAgenceId(),
                request.getCategorieProduitId(),
                delai
        );

        if (dejaSignale) {
            throw new RuntimeException("Vous avez déjà signalé cette agence récemment. Veuillez attendre.");
        }

        Signalement signalement = Signalement.builder()
                .consommateurId(request.getConsommateurId())
                .agenceId(request.getAgenceId())
                .categorieProduitId(request.getCategorieProduitId())
                .typeSignalement(request.getTypeSignalement())
                .statut(Signalement.StatutSignalement.EN_ATTENTE)
                .commentaire(request.getCommentaire())
                .build();

        Signalement saved = signalementRepository.save(signalement);
        log.info("✅ Signalement créé avec ID: {}", saved.getId());

        // Vérifier si on a assez de signalements pour confirmer
        verifierEtConfirmerSignalement(saved);

        return saved;
    }

    @Transactional
    public void verifierEtConfirmerSignalement(Signalement signalement) {
        LocalDateTime depuis = LocalDateTime.now().minusMinutes(30);

        long nbSignalements = signalementRepository.countDistinctConsommateursSignalant(
                signalement.getAgenceId(),
                signalement.getCategorieProduitId(),
                signalement.getTypeSignalement(),
                depuis
        );

        if (nbSignalements >= nbSignalementsConfirmation) {
            // Confirmer tous les signalements en attente
            List<Signalement> signalementsEnAttente = signalementRepository
                    .findSignalementsRecentsEnAttente(
                            signalement.getAgenceId(),
                            signalement.getCategorieProduitId(),
                            signalement.getTypeSignalement(),
                            depuis
                    );

            for (Signalement s : signalementsEnAttente) {
                s.setStatut(Signalement.StatutSignalement.CONFIRME);
                s.setDateTraitement(LocalDateTime.now());
                signalementRepository.save(s);
            }

            log.info("✅ Signalements confirmés pour agence {}, catégorie {}",
                    signalement.getAgenceId(), signalement.getCategorieProduitId());
        }
    }

    public List<Signalement> getSignalementsByAgence(Long agenceId) {
        return signalementRepository.findByAgenceId(agenceId);
    }

    public List<Signalement> getSignalementsByAgenceAndType(
            Long agenceId, Signalement.TypeSignalement type) {
        return signalementRepository.findByAgenceIdAndTypeSignalement(agenceId, type);
    }

    public List<Signalement> getSignalementsByAgenceAndStatut(
            Long agenceId, Signalement.StatutSignalement statut) {
        return signalementRepository.findByAgenceIdAndStatut(agenceId, statut);
    }

    public Signalement getSignalementById(Long id) {
        return signalementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
    }

    @Transactional
    public Signalement traiterSignalement(
            Long id, Signalement.StatutSignalement statut, Long traitePar) {
        Signalement signalement = getSignalementById(id);
        signalement.setStatut(statut);
        signalement.setTraitePar(traitePar);
        signalement.setDateTraitement(LocalDateTime.now());
        
        Signalement updated = signalementRepository.save(signalement);
        log.info("Signalement {} traité avec le statut: {}", id, statut);
        return updated;
    }

    public long countPendingSignalementsByAgence(Long agenceId) {
        return signalementRepository.countByAgenceIdAndStatut(
                agenceId, Signalement.StatutSignalement.EN_ATTENTE
        );
    }

    public List<Signalement>getAllSignalements(){
        return signalementRepository.findAll();
    }
}