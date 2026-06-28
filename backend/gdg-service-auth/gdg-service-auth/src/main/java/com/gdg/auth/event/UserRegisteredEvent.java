package com.gdg.auth.event;

public class UserRegisteredEvent {
    private String email;
    private String nom;
    private String prenom;
    private String role;
    private String tokenVerification;

    public UserRegisteredEvent() {}

    public UserRegisteredEvent(String email, String nom,
            String prenom, String role, String tokenVerification) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.tokenVerification = tokenVerification;
    }

    // GETTERS & SETTERS
    public String getEmail() { return email; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getRole() { return role; }
    public String getTokenVerification() { return tokenVerification; }
    public void setEmail(String email) { this.email = email; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setRole(String role) { this.role = role; }
    public void setTokenVerification(String t) { this.tokenVerification = t; }
}