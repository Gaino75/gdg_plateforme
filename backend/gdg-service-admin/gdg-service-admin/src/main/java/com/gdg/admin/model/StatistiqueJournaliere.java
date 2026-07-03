package com.gdg.admin.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "statistique_journaliere",
    schema = "admin_schema",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"date_stat", "agence_id"})
    }
)
public class StatistiqueJournaliere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Date des statistiques
    @Column(name = "date_stat", nullable = false)
    private LocalDate dateStat;

    // null = statistique globale plateforme
    @Column(name = "agence_id")
    private Long agenceId;

    // Nombre de ventes ce jour
    @Column(nullable = false)
    private Integer nbVentes = 0;

    // Montant total des ventes
    @Column(nullable = false)
    private Double montantTotalVentes = 0.0;

    // Nombre de réservations
    @Column(nullable = false)
    private Integer nbReservations = 0;

    // Réservations annulées
    @Column(nullable = false)
    private Integer nbReservationsAnnulees = 0;

    // Nouveaux clients inscrits
    @Column(nullable = false)
    private Integer nbNouveauxClients = 0;

    // Signalements reçus
    @Column(nullable = false)
    private Integer nbSignalements = 0;

    // Alertes stock critique
    @Column(nullable = false)
    private Integer nbAlertesStock = 0;

    // GETTERS
    public Long getId() { return id; }
    public LocalDate getDateStat() { return dateStat; }
    public Long getAgenceId() { return agenceId; }
    public Integer getNbVentes() { return nbVentes; }
    public Double getMontantTotalVentes() { return montantTotalVentes; }
    public Integer getNbReservations() { return nbReservations; }
    public Integer getNbReservationsAnnulees() { return nbReservationsAnnulees; }
    public Integer getNbNouveauxClients() { return nbNouveauxClients; }
    public Integer getNbSignalements() { return nbSignalements; }
    public Integer getNbAlertesStock() { return nbAlertesStock; }

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setDateStat(LocalDate dateStat) { this.dateStat = dateStat; }
    public void setAgenceId(Long agenceId) { this.agenceId = agenceId; }
    public void setNbVentes(Integer nbVentes) { this.nbVentes = nbVentes; }
    public void setMontantTotalVentes(Double montantTotalVentes) { this.montantTotalVentes = montantTotalVentes; }
    public void setNbReservations(Integer nbReservations) { this.nbReservations = nbReservations; }
    public void setNbReservationsAnnulees(Integer nbReservationsAnnulees) { this.nbReservationsAnnulees = nbReservationsAnnulees; }
    public void setNbNouveauxClients(Integer nbNouveauxClients) { this.nbNouveauxClients = nbNouveauxClients; }
    public void setNbSignalements(Integer nbSignalements) { this.nbSignalements = nbSignalements; }
    public void setNbAlertesStock(Integer nbAlertesStock) { this.nbAlertesStock = nbAlertesStock; }
}

