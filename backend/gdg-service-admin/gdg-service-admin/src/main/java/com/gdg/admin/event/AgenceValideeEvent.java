package com.gdg.admin.event;

public class AgenceValideeEvent {
    private Long agenceId;
    private String nomAgence;
    private Long adminId;

    public AgenceValideeEvent() {}

    public AgenceValideeEvent(Long agenceId, String nomAgence,
            Long adminId) {
        this.agenceId = agenceId;
        this.nomAgence = nomAgence;
        this.adminId = adminId;
    }

    public Long getAgenceId() { return agenceId; }
    public String getNomAgence() { return nomAgence; }
    public Long getAdminId() { return adminId; }
    public void setAgenceId(Long agenceId) { this.agenceId = agenceId; }
    public void setNomAgence(String nomAgence) { this.nomAgence = nomAgence; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
}