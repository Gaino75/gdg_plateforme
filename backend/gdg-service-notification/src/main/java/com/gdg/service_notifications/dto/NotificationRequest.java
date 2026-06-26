// dto/NotificationRequest.java
package com.gdg.service_notifications.dto;

import com.gdg.service_notifications.model.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotNull
    private Long utilisateurId;

    @NotBlank
    private String titre;

    @NotBlank
    private String message;

    @NotNull
    private Notification.TypeNotification typeNotification;

    private Long referenceId;

    private String referenceType;

    @NotNull
    private Boolean envoyerEmail;

    @NotNull
    private Boolean envoyerSms;

    private String emailDestinataire;

    private String telephoneDestinataire;
}
