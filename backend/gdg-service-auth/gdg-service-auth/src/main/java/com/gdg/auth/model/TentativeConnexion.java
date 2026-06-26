package com.gdg.auth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tentative_connexion", schema = "auth_schema")
public class TentativeConnexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Email utilisé pour la tentative
    @Column(nullable = false, length = 150)
    private String email;

    // Adresse IP de la requête
    @Column(name = "adresse_ip", length = 50)
    private String adresseIp;

    // true = connexion réussie, false = échec
    @Column(nullable = false)
    private Boolean succes;

    // Date et heure de la tentative
    @Column(name = "date_tentative", nullable = false)
    private LocalDateTime dateTentative = LocalDateTime.now();

    // GETTERS
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getAdresseIp() { return adresseIp; }
    public Boolean getSucces() { return succes; }
    public LocalDateTime getDateTentative() { return dateTentative; }

    // SETTERS
    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setAdresseIp(String adresseIp) { this.adresseIp = adresseIp; }
    public void setSucces(Boolean succes) { this.succes = succes; }
    public void setDateTentative(LocalDateTime dateTentative) { this.dateTentative = dateTentative; }
}