package com.gdg.service_notifications.listener;

import com.gdg.service_notifications.dto.NotificationRequest;
import com.gdg.service_notifications.model.Notification;
import com.gdg.service_notifications.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationListenerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationListener notificationListener;

    @Test
    void onAgenceValideeEnvoieEmailAuDistributeur() {
        notificationListener.onAgenceValidee(Map.of(
            "agenceId", 3,
            "nomAgence", "Total Bafoussam",
            "distributeurId", 7,
            "emailDistributeur", "dist@example.com",
            "adminId", 1
        ));

        ArgumentCaptor<NotificationRequest> captor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationService).createAndSendNotification(captor.capture());

        NotificationRequest request = captor.getValue();
        assertEquals(7L, request.getUtilisateurId());
        assertTrue(request.getEnvoyerEmail());
        assertEquals("dist@example.com", request.getEmailDestinataire());
        assertEquals(Notification.TypeNotification.AGENCE_VALIDEE, request.getTypeNotification());
        assertTrue(request.getMessage().contains("Total Bafoussam"));
    }
}
