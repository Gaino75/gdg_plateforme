package com.gdg.service_agences.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "horaire_ouverture", schema = "agences_schema")
public class HoraireOuverture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agence_id", nullable = false)
    private Agence agence;

    @Column(name = "jour_semaine", nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private JourSemaine jourSemaine;

    @Column(name = "heure_ouverture")
    private LocalTime heureOuverture;

    @Column(name = "heure_fermeture")
    private LocalTime heureFermeture;

    @Column(nullable = false)
    private Boolean ferme = false;

    public enum JourSemaine {
        LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE
    }

    public HoraireOuverture() {}

    public HoraireOuverture(Agence agence, JourSemaine jourSemaine, LocalTime heureOuverture, 
                           LocalTime heureFermeture, Boolean ferme) {
        this.agence = agence;
        this.jourSemaine = jourSemaine;
        this.heureOuverture = heureOuverture;
        this.heureFermeture = heureFermeture;
        this.ferme = ferme != null ? ferme : false;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Agence getAgence() { return agence; }
    public void setAgence(Agence agence) { this.agence = agence; }

    public JourSemaine getJourSemaine() { return jourSemaine; }
    public void setJourSemaine(JourSemaine jourSemaine) { this.jourSemaine = jourSemaine; }

    public LocalTime getHeureOuverture() { return heureOuverture; }
    public void setHeureOuverture(LocalTime heureOuverture) { this.heureOuverture = heureOuverture; }

    public LocalTime getHeureFermeture() { return heureFermeture; }
    public void setHeureFermeture(LocalTime heureFermeture) { this.heureFermeture = heureFermeture; }

    public Boolean getFerme() { return ferme; }
    public void setFerme(Boolean ferme) { this.ferme = ferme; }
}