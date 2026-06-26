package com.gdg.service_notifications.service;

import com.gdg.service_notifications.dto.NotificationRequest;
import com.gdg.service_notifications.model.Abonnement;
import com.gdg.service_notifications.model.Notification;
import com.gdg.service_notifications.repository.AbonnementRepository;
import com.gdg.service_notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AbonnementRepository abonnementRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    @Transactional
    public Notification createAndSendNotification(NotificationRequest request) {
        try {
            log.info("📨 Début création notification: utilisateurId={}, titre={}", 
                    request.getUtilisateurId(), request.getTitre());

            // Créer la notification
            Notification notification = Notification.builder()
                    .utilisateurId(request.getUtilisateurId())
                    .titre(request.getTitre())
                    .message(request.getMessage())
                    .typeNotification(request.getTypeNotification())
                    .canal(Notification.Canal.IN_APP)
                    .statut(Notification.StatutNotification.NON_LU)
                    .referenceId(request.getReferenceId())
                    .referenceType(request.getReferenceType())
                    .build();

            log.info("📝 Notification créée, sauvegarde en cours...");

            // Sauvegarder en base
            Notification saved = notificationRepository.save(notification);
            log.info("✅ Notification sauvegardée avec ID: {}", saved.getId());

            // Envoyer par email si demandé
            if (request.getEnvoyerEmail() != null && request.getEnvoyerEmail() 
                    && request.getEmailDestinataire() != null && !request.getEmailDestinataire().isEmpty()) {
                try {
                    emailService.sendEmail(
                            request.getEmailDestinataire(),
                            request.getTitre(),
                            request.getMessage(),
                            request.getTypeNotification()
                    );
                    log.info("📧 Email envoyé à {}", request.getEmailDestinataire());
                } catch (Exception e) {
                    log.error("❌ Erreur email: {}", e.getMessage());
                }
            }

            // Envoyer par SMS si demandé
            if (request.getEnvoyerSms() != null && request.getEnvoyerSms() 
                    && request.getTelephoneDestinataire() != null && !request.getTelephoneDestinataire().isEmpty()) {
                try {
                    smsService.sendSms(
                            request.getTelephoneDestinataire(),
                            request.getMessage()
                    );
                    log.info("📱 SMS envoyé à {}", request.getTelephoneDestinataire());
                } catch (Exception e) {
                    log.error("❌ Erreur SMS: {}", e.getMessage());
                }
            }

            return saved;

        } catch (Exception e) {
            log.error("❌ ERREUR dans createAndSendNotification: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la création de la notification: " + e.getMessage(), e);
        }
    }

    public List<Notification> getNotificationsByUser(Long utilisateurId) {
        return notificationRepository.findByUtilisateurIdOrderByDateEnvoiDesc(utilisateurId);
    }

    public Page<Notification> getNotificationsByUserPaginated(Long utilisateurId, Pageable pageable) {
        return notificationRepository.findByUtilisateurId(utilisateurId, pageable);
    }

    public List<Notification> getNonLuesByUser(Long utilisateurId) {
        return notificationRepository.findByUtilisateurIdAndStatut(
                utilisateurId, Notification.StatutNotification.NON_LU
        );
    }

    public long countNonLuesByUser(Long utilisateurId) {
        return notificationRepository.countByUtilisateurIdAndStatut(
                utilisateurId, Notification.StatutNotification.NON_LU
        );
    }

    @Transactional
    public Notification marquerCommeLu(Long notificationId) {
        notificationRepository.updateStatut(notificationId, Notification.StatutNotification.LU);
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
    }

    @Transactional
    public void marquerToutesCommeLues(Long utilisateurId) {
        notificationRepository.marquerToutesCommeLues(utilisateurId);
        log.info("📚 Toutes les notifications marquées comme lues pour l'utilisateur {}", utilisateurId);
    }

    @Transactional
    public void envoyerNotificationStockDisponible(Long agenceId, Long categorieProduitId, 
                                                   String libelleProduit, int quantite) {
        try {
            List<Abonnement> abonnes = abonnementRepository.findAbonnesPourAgenceEtCategorie(
                    agenceId, categorieProduitId
            );

            log.info("📦 Envoi de notification de stock disponible à {} abonnés", abonnes.size());

            for (Abonnement abonnement : abonnes) {
                NotificationRequest request = NotificationRequest.builder()
                        .utilisateurId(abonnement.getConsommateurId())
                        .titre("📦 Stock disponible !")
                        .message(String.format(
                                "Le produit %s est disponible à l'agence. Quantité: %d unités",
                                libelleProduit, quantite
                        ))
                        .typeNotification(Notification.TypeNotification.STOCK_DISPONIBLE)
                        .referenceId(agenceId)
                        .referenceType("AGENCE")
                        .envoyerEmail(true)
                        .envoyerSms(false)
                        .build();

                createAndSendNotification(request);
            }
        } catch (Exception e) {
            log.error("❌ Erreur dans envoyerNotificationStockDisponible: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public void envoyerAlerteSeuilCritique(Long agenceId, Long categorieProduitId, 
                                          String libelleProduit, int quantiteRestante, 
                                          Long distributeurId, String emailDistributeur) {
        try {
            NotificationRequest request = NotificationRequest.builder()
                    .utilisateurId(distributeurId)
                    .titre("⚠️ ALERTE - Seuil critique atteint")
                    .message(String.format(
                            "Le stock de %s est à %d unités. Veuillez réapprovisionner immédiatement.",
                            libelleProduit, quantiteRestante
                    ))
                    .typeNotification(Notification.TypeNotification.SEUIL_CRITIQUE)
                    .referenceId(agenceId)
                    .referenceType("AGENCE")
                    .envoyerEmail(true)
                    .envoyerSms(true)
                    .emailDestinataire(emailDistributeur)
                    .telephoneDestinataire(null)
                    .build();

            createAndSendNotification(request);
        } catch (Exception e) {
            log.error("❌ Erreur dans envoyerAlerteSeuilCritique: {}", e.getMessage(), e);
        }
    }
}