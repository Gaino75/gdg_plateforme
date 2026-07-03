package com.gdg.admin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal_audit", schema = "admin_schema")
public class JournalAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID de l'admin qui a effectué l'action
    @Column(name = "utilisateur_id")
    private Long utilisateurId;

    // Rôle de l'admin
    @Column(name = "role_utilisateur", length = 20)
    private String roleUtilisateur;

    // Action effectuée ex: VALIDER_AGENCE
    @Column(nullable = false)
    private String action;

    // Type d'entité concernée ex: AGENCE, UTILISATEUR
    @Column(name = "entite_type", length = 50)
    private String entiteType;

    // ID de l'entité concernée
    @Column(name = "entite_id")
    private Long entiteId;

    // Détails JSON avant/après modification
    @Column(columnDefinition = "TEXT")
    private String details;

    // Adresse IP de l'admin
    @Column(name = "adresse_ip", length = 50)
    private String adresseIp;

    // Date de l'action
    @Column(name = "date_action", nullable = false)
    private LocalDateTime dateAction = LocalDateTime.now();

    // GETTERS
    public Long getId() { return id; }
    public Long getUtilisateurId() { return utilisateurId; }
    public String getRoleUtilisateur() { return roleUtilisateur; }
    public String getAction() { return action; }
    public String getEntiteType() { return entiteType; }
    public Long getEntiteId() { return entiteId; }
    public String getDetails() { return details; }
    public String getAdresseIp() { return adresseIp; }
    public LocalDateTime getDateAction() { return dateAction; }

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }
    public void setRoleUtilisateur(String roleUtilisateur) { this.roleUtilisateur = roleUtilisateur; }
    public void setAction(String action) { this.action = action; }
    public void setEntiteType(String entiteType) { this.entiteType = entiteType; }
    public void setEntiteId(Long entiteId) { this.entiteId = entiteId; }
    public void setDetails(String details) { this.details = details; }
    public void setAdresseIp(String adresseIp) { this.adresseIp = adresseIp; }
    public void setDateAction(LocalDateTime dateAction) { this.dateAction = dateAction; }
}
