package com.gdg.service_reservations.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_reservation", schema = "reservations_schema")
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

    // ============================================================
    // CONSTRUCTEURS
    // ============================================================

    public HistoriqueReservation() {}

    public HistoriqueReservation(Long reservationId, String ancienStatut, String nouveauStatut,
                                 String commentaire, Long effectuePar) {
        this.reservationId = reservationId;
        this.ancienStatut = ancienStatut;
        this.nouveauStatut = nouveauStatut;
        this.commentaire = commentaire;
        this.effectuePar = effectuePar;
        this.dateChangement = LocalDateTime.now();
    }

    // ============================================================
    // GETTERS ET SETTERS
    // ============================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public String getAncienStatut() { return ancienStatut; }
    public void setAncienStatut(String ancienStatut) { this.ancienStatut = ancienStatut; }

    public String getNouveauStatut() { return nouveauStatut; }
    public void setNouveauStatut(String nouveauStatut) { this.nouveauStatut = nouveauStatut; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public Long getEffectuePar() { return effectuePar; }
    public void setEffectuePar(Long effectuePar) { this.effectuePar = effectuePar; }

    public LocalDateTime getDateChangement() { return dateChangement; }
    public void setDateChangement(LocalDateTime dateChangement) { this.dateChangement = dateChangement; }
}