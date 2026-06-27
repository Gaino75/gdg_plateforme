package com.gdg.service_agences.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "horaire_ouverture", schema = "agences_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public HoraireOuverture(Agence agence, JourSemaine jourSemaine, LocalTime heureOuverture, LocalTime heureFermeture, Boolean ferme) {
        this.agence = agence;
        this.jourSemaine = jourSemaine;
        this.heureOuverture = heureOuverture;
        this.heureFermeture = heureFermeture;
        this.ferme = ferme != null ? ferme : false;
    }
}