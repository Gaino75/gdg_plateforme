package com.gazstation1.payement_service1.model;

import com.gazstation1.payement_service1.enums.StatutPaiement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long commandeId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private String modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statut;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateMiseAJour;

    private String referenceTransaction;

    @PrePersist
    public void  prePersist(){
        this.dateCreation = LocalDateTime.now();
        this.dateMiseAJour = LocalDateTime.now();
        if(this.statut == null){
            this.statut = StatutPaiement.EN_ATTENTE;
        }
    }

    @PreUpdate
    public void preUpdate(){
        this.dateMiseAJour = LocalDateTime.now();
    }
}
