package com.gdg.service_reservations.dto;

/**
 * DTO pour les statistiques des réservations
 */
public class ReservationStatistiquesResponse {

    private long total;
    private long enAttente;
    private long payees;
    private long confirmees;
    private long annulees;
    private long expirees;
    private long recuperees;

    // Constructeur
    public ReservationStatistiquesResponse(long total, long enAttente, long payees,
                                            long confirmees, long annulees,
                                            long expirees, long recuperees) {
        this.total = total;
        this.enAttente = enAttente;
        this.payees = payees;
        this.confirmees = confirmees;
        this.annulees = annulees;
        this.expirees = expirees;
        this.recuperees = recuperees;
    }

    // Getters
    public long getTotal() { return total; }
    public long getEnAttente() { return enAttente; }
    public long getPayees() { return payees; }
    public long getConfirmees() { return confirmees; }
    public long getAnnulees() { return annulees; }
    public long getExpirees() { return expirees; }
    public long getRecuperees() { return recuperees; }
}