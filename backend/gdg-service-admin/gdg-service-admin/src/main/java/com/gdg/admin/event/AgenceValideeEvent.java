package com.gdg.admin.event;

public class AgenceValideeEvent {
    private Long agenceId;
    private String nomAgence;
    private Long adminId;
    private Long distributeurId;
    private String emailDistributeur;

    public AgenceValideeEvent() {}

    public AgenceValideeEvent(Long agenceId, String nomAgence, Long adminId,
                              Long distributeurId, String emailDistributeur) {
        this.agenceId = agenceId;
        this.nomAgence = nomAgence;
        this.adminId = adminId;
        this.distributeurId = distributeurId;
        this.emailDistributeur = emailDistributeur;
    }

    public Long getAgenceId() { return agenceId; }
    public String getNomAgence() { return nomAgence; }
    public Long getAdminId() { return adminId; }
    public Long getDistributeurId() { return distributeurId; }
    public String getEmailDistributeur() { return emailDistributeur; }

    public void setAgenceId(Long agenceId) { this.agenceId = agenceId; }
    public void setNomAgence(String nomAgence) { this.nomAgence = nomAgence; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public void setDistributeurId(Long distributeurId) { this.distributeurId = distributeurId; }
    public void setEmailDistributeur(String emailDistributeur) { this.emailDistributeur = emailDistributeur; }
}
