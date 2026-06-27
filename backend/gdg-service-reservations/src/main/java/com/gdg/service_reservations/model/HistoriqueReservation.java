package com.gdg.service_reservations.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_reservation", schema = "reservations_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Column(name = "ancien_statut", length = 20)
    private String ancienStatut;

    @Column(name = "nouveau_statut", nullable = false, length = 20)
    private String nouveauStatut;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "effectue_par")
    private Long effectuePar;

    @Column(name = "date_changement", nullable = false)
    private LocalDateTime dateChangement = LocalDateTime.now();

    // Constructeur pratique
    public HistoriqueReservation(Long reservationId, String ancienStatut, String nouveauStatut,
                                 String commentaire, Long effectuePar) {
        this.reservationId = reservationId;
        this.ancienStatut = ancienStatut;
        this.nouveauStatut = nouveauStatut;
        this.commentaire = commentaire;
        this.effectuePar = effectuePar;
        this.dateChangement = LocalDateTime.now();
    }
}