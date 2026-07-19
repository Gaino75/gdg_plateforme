package com.gdg.service_notifications.listener;

import com.gdg.service_notifications.config.RabbitMQConfig;
import com.gdg.service_notifications.dto.NotificationRequest;
import com.gdg.service_notifications.model.Notification;
import com.gdg.service_notifications.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationListener {

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_USER_REGISTERED)
    public void onUserRegistered(Map<String, Object> event) {
        String token = stringValue(event.get("tokenVerification"));
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(longValue(event.get("userId")))
            .titre("Bienvenue sur GDG !")
            .message("Votre compte a été créé. Vérifiez votre email avec ce lien : "
                + "http://localhost:3000/verify-email?token=" + token)
            .typeNotification(Notification.TypeNotification.COMPTE_VALIDE)
            .envoyerEmail(true)
            .emailDestinataire(stringValue(event.get("email")))
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PASSWORD_RESET)
    public void onPasswordReset(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(1L)
            .titre("Réinitialisation mot de passe")
            .message("Utilisez ce lien pour réinitialiser votre mot de passe : "
                + "http://localhost:3000/reset-password?token="
                + stringValue(event.get("resetToken")))
            .typeNotification(Notification.TypeNotification.COMPTE_VALIDE)
            .envoyerEmail(true)
            .emailDestinataire(stringValue(event.get("email")))
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_USER_SUSPENDED)
    public void onUserSuspended(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(longValue(event.get("userId")))
            .titre("Compte suspendu")
            .message("Votre compte a été suspendu. Motif : " + stringValue(event.get("motif")))
            .typeNotification(Notification.TypeNotification.COMPTE_VALIDE)
            .envoyerEmail(true)
            .emailDestinataire(stringValue(event.get("email")))
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_AGENCE_VALIDEE)
    public void onAgenceValidee(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(longValue(event.get("distributeurId")))
            .titre("Agence validée !")
            .message("Félicitations ! Votre agence « "
                + stringValue(event.get("nomAgence"))
                + " » a été validée par l'administrateur. "
                + "Elle est maintenant visible sur la plateforme GPG.")
            .typeNotification(Notification.TypeNotification.AGENCE_VALIDEE)
            .referenceId(longValue(event.get("agenceId")))
            .referenceType("AGENCE")
            .envoyerEmail(true)
            .emailDestinataire(stringValue(event.get("emailDistributeur")))
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_STOCK_CRITIQUE)
    public void onStockCritique(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(longValue(event.get("distributeurId")))
            .titre("Alerte stock critique")
            .message("Stock critique agence " + stringValue(event.get("agenceId"))
                + " — " + stringValue(event.get("categorieLibelle"))
                + " : " + stringValue(event.get("quantiteDisponible")) + " unités restantes")
            .typeNotification(Notification.TypeNotification.SEUIL_CRITIQUE)
            .referenceId(longValue(event.get("agenceId")))
            .referenceType("STOCK")
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_STOCK_DISPONIBLE)
    public void onStockDisponible(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(longValue(event.get("distributeurId")))
            .titre("Produit disponible")
            .message("Le produit " + stringValue(event.get("categorieLibelle"))
                + " est de nouveau disponible à l'agence " + stringValue(event.get("agenceId")))
            .typeNotification(Notification.TypeNotification.STOCK_DISPONIBLE)
            .referenceId(longValue(event.get("agenceId")))
            .referenceType("STOCK")
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_RESERVATION_CONFIRMEE)
    public void onReservationConfirmee(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(longValue(event.get("consommateurId")))
            .titre("Nouvelle réservation payée")
            .message("Réservation " + stringValue(event.get("referenceReservation"))
                + " confirmée — montant " + stringValue(event.get("montantTotal")) + " XAF")
            .typeNotification(Notification.TypeNotification.RESERVATION_CONFIRMEE)
            .referenceId(longValue(event.get("reservationId")))
            .referenceType("RESERVATION")
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PAIEMENT_CONFIRME)
    public void onPaiementConfirme(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(longValue(event.get("consommateurId")))
            .titre("Paiement confirmé")
            .message("Votre paiement " + stringValue(event.get("referenceTransaction"))
                + " a été confirmé avec succès.")
            .typeNotification(Notification.TypeNotification.PAIEMENT_CONFIRME)
            .referenceId(longValue(event.get("paiementId")))
            .referenceType("PAIEMENT")
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PAIEMENT_ECHOUE)
    public void onPaiementEchoue(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(longValue(event.get("consommateurId")))
            .titre("Paiement échoué")
            .message("Le paiement de la réservation " + stringValue(event.get("reservationId"))
                + " a échoué : " + stringValue(event.get("message")))
            .typeNotification(Notification.TypeNotification.PAIEMENT_ECHOUE)
            .referenceId(longValue(event.get("reservationId")))
            .referenceType("RESERVATION")
            .build();
        notificationService.createAndSendNotification(request);
    }

    private Long longValue(Object value) {
        if (value == null) {
            return 1L;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.parseLong(value.toString());
    }

    private String stringValue(Object value) {
        return value != null ? value.toString() : "";
    }
}
