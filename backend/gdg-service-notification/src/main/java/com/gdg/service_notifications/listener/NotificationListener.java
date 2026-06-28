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
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(1L)
            .titre("Bienvenue sur GPG !")
            .message("Votre compte a été créé. Vérifiez votre email.")
            .typeNotification(Notification.TypeNotification.COMPTE_VALIDE)
            .envoyerEmail(true)
            .emailDestinataire((String) event.get("email"))
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PASSWORD_RESET)
    public void onPasswordReset(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(1L)
            .titre("Réinitialisation mot de passe")
            .message("Token reset : " + event.get("resetToken"))
            .typeNotification(Notification.TypeNotification.COMPTE_VALIDE)
            .envoyerEmail(true)
            .emailDestinataire((String) event.get("email"))
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_USER_SUSPENDED)
    public void onUserSuspended(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId((Long) event.get("userId"))
            .titre("Compte suspendu")
            .message("Votre compte a été suspendu. Motif : " + event.get("motif"))
            .typeNotification(Notification.TypeNotification.COMPTE_VALIDE)
            .envoyerEmail(false)
            .build();
        notificationService.createAndSendNotification(request);
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_AGENCE_VALIDEE)
    public void onAgenceValidee(Map<String, Object> event) {
        NotificationRequest request = NotificationRequest.builder()
            .utilisateurId(1L)
            .titre("Agence validée !")
            .message("Votre agence " + event.get("nomAgence") + " a été validée.")
            .typeNotification(Notification.TypeNotification.AGENCE_VALIDEE)
            .envoyerEmail(false)
            .build();
        notificationService.createAndSendNotification(request);
    }
}