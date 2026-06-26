// model/Notification.java
package com.gdg.service_notifications.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "utilisateur_id", nullable = false)
    private Long utilisateurId;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "type_notification", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeNotification typeNotification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Canal canal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutNotification statut;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "reference_type")
    private String referenceType;

    @CreationTimestamp
    @Column(name = "date_envoi", nullable = false)
    private LocalDateTime dateEnvoi;

    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    public enum TypeNotification {
        STOCK_DISPONIBLE,
        SEUIL_CRITIQUE,
        RESERVATION_CONFIRMEE,
        RESERVATION_ANNULEE,
        RESERVATION_EXPIREE,
        PAIEMENT_CONFIRME,
        PAIEMENT_ECHOUE,
        REMBOURSEMENT,
        SIGNALEMENT_TRAITE,
        COMPTE_VALIDE,
        AGENCE_VALIDEE
    }

    public enum Canal {
        IN_APP, EMAIL, SMS
    }

    public enum StatutNotification {
        NON_LU, LU, ENVOYE, ECHOUE
    }
}
