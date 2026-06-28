package com.gdg.admin.dto;

import java.time.LocalDateTime;

public class SignalementDTO {
    private Long id;
    private Long consommateurId;
    private Long agenceId;
    private Long categorieProduitId;
    private String typeSignalement;
    private String statut;
    private String commentaire;
    private LocalDateTime dateSignalement;

    public Long getId() { return id; }
    public Long getConsommateurId() { return consommateurId; }
    public Long getAgenceId() { return agenceId; }
    public Long getCategorieProduitId() { return categorieProduitId; }
    public String getTypeSignalement() { return typeSignalement; }
    public String getStatut() { return statut; }
    public String getCommentaire() { return commentaire; }
    public LocalDateTime getDateSignalement() { return dateSignalement; }

    public void setId(Long id) { this.id = id; }
    public void setConsommateurId(Long v) { this.consommateurId = v; }
    public void setAgenceId(Long v) { this.agenceId = v; }
    public void setCategorieProduitId(Long v) { this.categorieProduitId = v; }
    public void setTypeSignalement(String v) { this.typeSignalement = v; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setCommentaire(String v) { this.commentaire = v; }
    public void setDateSignalement(LocalDateTime v) { this.dateSignalement = v; }
}
