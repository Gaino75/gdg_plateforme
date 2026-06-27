package com.gdg.service_agences.dto;

/**
 * DTO pour les statistiques des agences
 */
public class AgenceStatistiquesResponse {

    private long totalAgences;
    private long agencesActives;
    private long agencesEnAttente;
    private long agencesSuspendues;
    private long totalEnseignes;
    private long totalVilles;

    // Constructeur
    public AgenceStatistiquesResponse(long totalAgences, long agencesActives,
                                       long agencesEnAttente, long agencesSuspendues,
                                       long totalEnseignes, long totalVilles) {
        this.totalAgences = totalAgences;
        this.agencesActives = agencesActives;
        this.agencesEnAttente = agencesEnAttente;
        this.agencesSuspendues = agencesSuspendues;
        this.totalEnseignes = totalEnseignes;
        this.totalVilles = totalVilles;
    }

    // Getters
    public long getTotalAgences() { return totalAgences; }
    public long getAgencesActives() { return agencesActives; }
    public long getAgencesEnAttente() { return agencesEnAttente; }
    public long getAgencesSuspendues() { return agencesSuspendues; }
    public long getTotalEnseignes() { return totalEnseignes; }
    public long getTotalVilles() { return totalVilles; }
}