package com.gdg.admin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "demande_inscription_agence", schema = "admin_schema")
public class DemandeInscriptionAgence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_agence", nullable = false)
    private String nomAgence;

    @Column(name = "nom_enseigne", nullable = false)
    private String nomEnseigne;

    @Column(name = "nom_ville", nullable = false)
    private String nomVille;

    private String adresse;
    private String telephone;
    private String email;

    @Column(name = "nom_responsable", nullable = false)
    private String nomResponsable;

    @Column(name = "email_responsable", nullable = false)
    private String emailResponsable;

    @Column(name = "telephone_responsable")
    private String telephoneResponsable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutDemande statut = StatutDemande.EN_ATTENTE;

    // Motif si rejet
    @Column(name = "motif_rejet", columnDefinition = "TEXT")
    private String motifRejet;

    // ID admin qui a traité
    @Column(name = "traite_par")
    private Long traitePar;

    @Column(name = "date_demande", nullable = false)
    private LocalDateTime dateDemande = LocalDateTime.now();

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    public enum StatutDemande {
        EN_ATTENTE, APPROUVEE, REJETEE
    }

    // GETTERS
    public Long getId() { return id; }
    public String getNomAgence() { return nomAgence; }
    public String getNomEnseigne() { return nomEnseigne; }
    public String getNomVille() { return nomVille; }
    public String getAdresse() { return adresse; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getNomResponsable() { return nomResponsable; }
    public String getEmailResponsable() { return emailResponsable; }
    public String getTelephoneResponsable() { return telephoneResponsable; }
    public StatutDemande getStatut() { return statut; }
    public String getMotifRejet() { return motifRejet; }
    public Long getTraitePar() { return traitePar; }
    public LocalDateTime getDateDemande() { return dateDemande; }
    public LocalDateTime getDateTraitement() { return dateTraitement; }

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setNomAgence(String nomAgence) { this.nomAgence = nomAgence; }
    public void setNomEnseigne(String nomEnseigne) { this.nomEnseigne = nomEnseigne; }
    public void setNomVille(String nomVille) { this.nomVille = nomVille; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setEmail(String email) { this.email = email; }
    public void setNomResponsable(String nomResponsable) { this.nomResponsable = nomResponsable; }
    public void setEmailResponsable(String emailResponsable) { this.emailResponsable = emailResponsable; }
    public void setTelephoneResponsable(String telephoneResponsable) { this.telephoneResponsable = telephoneResponsable; }
    public void setStatut(StatutDemande statut) { this.statut = statut; }
    public void setMotifRejet(String motifRejet) { this.motifRejet = motifRejet; }
    public void setTraitePar(Long traitePar) { this.traitePar = traitePar; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }
    public void setDateTraitement(LocalDateTime dateTraitement) { this.dateTraitement = dateTraitement; }
}