package com.gdg.auth.event;

public class UserRegisteredEvent {
    private Long userId;
    private String email;
    private String nom;
    private String prenom;
    private String role;
    private String tokenVerification;

    public UserRegisteredEvent() {}

    public UserRegisteredEvent(Long userId, String email, String nom,
            String prenom, String role, String tokenVerification) {
        this.userId = userId;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.tokenVerification = tokenVerification;
    }

    // GETTERS & SETTERS
    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getRole() { return role; }
    public String getTokenVerification() { return tokenVerification; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setRole(String role) { this.role = role; }
    public void setTokenVerification(String t) { this.tokenVerification = t; }
}