package com.gdg.service_paiement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.gdg.service_paiement.enums.StatutPaiement;

@Entity
@Table(name = "transaction_paiement", schema = "paiement_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference", nullable = false, unique = true, length = 100)
    private String referenceTransaction;

    @Column(name = "reservation_id")
    private Long commandeId;

    @Column(name = "vente_id")
    private Long venteId;

    @Column(name = "consommateur_id", nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Double montant;

    @Column(name = "mode_paiement", nullable = false, length = 20)
    private String modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutPaiement statut;

    @Column(name = "numero_telephone", length = 20)
    private String numeroTelephone;

    @Column(length = 10)
    private String operateur;

    @Column(name = "reference_operateur", length = 150)
    private String referenceOperateur;

    @Column(name = "date_initiation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_confirmation")
    private LocalDateTime dateMiseAJour;

    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration;

    @Column(name = "message_erreur", columnDefinition = "TEXT")
    private String messageErreur;

    @Column(name = "agence_id", nullable = false)
    private Long agenceId;

    @Column(name = "nombre_tentatives", nullable = false)
    @Builder.Default
    private Integer nombreTentatives = 0;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
        if (this.dateExpiration == null) {
            this.dateExpiration = LocalDateTime.now().plusMinutes(30);
        }
        if (this.statut == null) {
            this.statut = StatutPaiement.EN_ATTENTE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (this.statut == StatutPaiement.CONFIRME && this.dateMiseAJour == null) {
            this.dateMiseAJour = LocalDateTime.now();
        }
    }
}
