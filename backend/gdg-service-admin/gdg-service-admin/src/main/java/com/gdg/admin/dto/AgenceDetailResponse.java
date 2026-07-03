package com.gdg.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgenceDetailResponse {
    private Long id;
    private String nom;
    private String email;

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setEmail(String email) { this.email = email; }
}
