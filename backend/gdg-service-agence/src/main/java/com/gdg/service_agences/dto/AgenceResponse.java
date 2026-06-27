package com.gdg.service_agences.dto;

import com.gdg.service_agences.model.Agence.StatutAgence;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour l'affichage détaillé d'une agence
 */
public class AgenceResponse {

    private Long id;
    private String nom;
    private String adresse;
    private Double latitude;
    private Double longitude;
    private String telephone;
    private String email;
    private String logoFacture;
    private String enteteFacture;
    private String piedFacture;
    private StatutAgence statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateValidation;
    private Long validePar;
    private EnseigneSummary enseigne;
    private VilleSummary ville;
    private List<HoraireOuvertureDTO> horaires;

    private AgenceResponse() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final AgenceResponse dto = new AgenceResponse();

        public Builder id(Long id) { dto.id = id; return this; }
        public Builder nom(String nom) { dto.nom = nom; return this; }
        public Builder adresse(String adresse) { dto.adresse = adresse; return this; }
        public Builder latitude(Double latitude) { dto.latitude = latitude; return this; }
        public Builder longitude(Double longitude) { dto.longitude = longitude; return this; }
        public Builder telephone(String telephone) { dto.telephone = telephone; return this; }
        public Builder email(String email) { dto.email = email; return this; }
        public Builder logoFacture(String logoFacture) { dto.logoFacture = logoFacture; return this; }
        public Builder enteteFacture(String enteteFacture) { dto.enteteFacture = enteteFacture; return this; }
        public Builder piedFacture(String piedFacture) { dto.piedFacture = piedFacture; return this; }
        public Builder statut(StatutAgence statut) { dto.statut = statut; return this; }
        public Builder dateCreation(LocalDateTime dateCreation) { dto.dateCreation = dateCreation; return this; }
        public Builder dateValidation(LocalDateTime dateValidation) { dto.dateValidation = dateValidation; return this; }
        public Builder validePar(Long validePar) { dto.validePar = validePar; return this; }
        public Builder enseigne(EnseigneSummary enseigne) { dto.enseigne = enseigne; return this; }
        public Builder ville(VilleSummary ville) { dto.ville = ville; return this; }
        public Builder horaires(List<HoraireOuvertureDTO> horaires) { dto.horaires = horaires; return this; }

        public AgenceResponse build() {
            return dto;
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getAdresse() { return adresse; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getLogoFacture() { return logoFacture; }
    public String getEnteteFacture() { return enteteFacture; }
    public String getPiedFacture() { return piedFacture; }
    public StatutAgence getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateValidation() { return dateValidation; }
    public Long getValidePar() { return validePar; }
    public EnseigneSummary getEnseigne() { return enseigne; }
    public VilleSummary getVille() { return ville; }
    public List<HoraireOuvertureDTO> getHoraires() { return horaires; }
}