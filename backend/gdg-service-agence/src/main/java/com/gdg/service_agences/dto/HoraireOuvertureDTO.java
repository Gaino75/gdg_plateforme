package com.gdg.service_agences.dto;

import com.gdg.service_agences.model.HoraireOuverture.JourSemaine;

import java.time.LocalTime;

/**
 * DTO pour les horaires d'ouverture
 */
public class HoraireOuvertureDTO {

    private JourSemaine jourSemaine;
    private LocalTime heureOuverture;
    private LocalTime heureFermeture;
    private Boolean ferme;

    // Constructeurs
    public HoraireOuvertureDTO() {}

    public HoraireOuvertureDTO(JourSemaine jourSemaine, LocalTime heureOuverture,
                               LocalTime heureFermeture, Boolean ferme) {
        this.jourSemaine = jourSemaine;
        this.heureOuverture = heureOuverture;
        this.heureFermeture = heureFermeture;
        this.ferme = ferme != null ? ferme : false;
    }

    // Getters et Setters
    public JourSemaine getJourSemaine() { return jourSemaine; }
    public void setJourSemaine(JourSemaine jourSemaine) { this.jourSemaine = jourSemaine; }
    public LocalTime getHeureOuverture() { return heureOuverture; }
    public void setHeureOuverture(LocalTime heureOuverture) { this.heureOuverture = heureOuverture; }
    public LocalTime getHeureFermeture() { return heureFermeture; }
    public void setHeureFermeture(LocalTime heureFermeture) { this.heureFermeture = heureFermeture; }
    public Boolean getFerme() { return ferme; }
    public void setFerme(Boolean ferme) { this.ferme = ferme; }
}