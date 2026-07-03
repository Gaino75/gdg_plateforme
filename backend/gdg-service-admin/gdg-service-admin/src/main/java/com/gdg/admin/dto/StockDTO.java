package com.gdg.admin.dto;

public class StockDTO {
    private Long id;
    private Long agenceId;
    private Long categorieProduitId;
    private Integer quantiteDisponible;
    private Integer seuilCritique;

    public Long getId() { return id; }
    public Long getAgenceId() { return agenceId; }
    public Long getCategorieProduitId() { return categorieProduitId; }
    public Integer getQuantiteDisponible() { return quantiteDisponible; }
    public Integer getSeuilCritique() { return seuilCritique; }

    public void setId(Long id) { this.id = id; }
    public void setAgenceId(Long v) { this.agenceId = v; }
    public void setCategorieProduitId(Long v) { this.categorieProduitId = v; }
    public void setQuantiteDisponible(Integer v) { this.quantiteDisponible = v; }
    public void setSeuilCritique(Integer v) { this.seuilCritique = v; }
}
