package com.gdg.admin.dto;

public class DashboardDTO {

    private Integer nbAgencesActives;
    private Integer nbAgencesEnAttente;
    private Integer nbAgencesSuspendues;
    private Integer nbUtilisateursTotal;
    private Integer nbConsommateurs;
    private Integer nbDistributeurs;
    private Integer nbVentesJour;
    private Double montantVentesJour;
    private Integer nbReservationsEnAttente;
    private Integer nbAlertesStockCritique;
    private Integer nbSignalementsEnAttente;
    private Integer nbDemandesEnAttente;

    // GETTERS
    public Integer getNbAgencesActives() { return nbAgencesActives; }
    public Integer getNbAgencesEnAttente() { return nbAgencesEnAttente; }
    public Integer getNbAgencesSuspendues() { return nbAgencesSuspendues; }
    public Integer getNbUtilisateursTotal() { return nbUtilisateursTotal; }
    public Integer getNbConsommateurs() { return nbConsommateurs; }
    public Integer getNbDistributeurs() { return nbDistributeurs; }
    public Integer getNbVentesJour() { return nbVentesJour; }
    public Double getMontantVentesJour() { return montantVentesJour; }
    public Integer getNbReservationsEnAttente() { return nbReservationsEnAttente; }
    public Integer getNbAlertesStockCritique() { return nbAlertesStockCritique; }
    public Integer getNbSignalementsEnAttente() { return nbSignalementsEnAttente; }
    public Integer getNbDemandesEnAttente() { return nbDemandesEnAttente; }

    // SETTERS
    public void setNbAgencesActives(Integer v) { this.nbAgencesActives = v; }
    public void setNbAgencesEnAttente(Integer v) { this.nbAgencesEnAttente = v; }
    public void setNbAgencesSuspendues(Integer v) { this.nbAgencesSuspendues = v; }
    public void setNbUtilisateursTotal(Integer v) { this.nbUtilisateursTotal = v; }
    public void setNbConsommateurs(Integer v) { this.nbConsommateurs = v; }
    public void setNbDistributeurs(Integer v) { this.nbDistributeurs = v; }
    public void setNbVentesJour(Integer v) { this.nbVentesJour = v; }
    public void setMontantVentesJour(Double v) { this.montantVentesJour = v; }
    public void setNbReservationsEnAttente(Integer v) { this.nbReservationsEnAttente = v; }
    public void setNbAlertesStockCritique(Integer v) { this.nbAlertesStockCritique = v; }
    public void setNbSignalementsEnAttente(Integer v) { this.nbSignalementsEnAttente = v; }
    public void setNbDemandesEnAttente(Integer v) { this.nbDemandesEnAttente = v; }
}

