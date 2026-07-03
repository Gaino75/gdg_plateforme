package com.gdg.auth.event;

public class PasswordResetEvent {
    private String email;
    private String nom;
    private String resetToken;

    public PasswordResetEvent() {}

    public PasswordResetEvent(String email, String nom,
            String resetToken) {
        this.email = email;
        this.nom = nom;
        this.resetToken = resetToken;
    }

    // GETTERS & SETTERS
    public String getEmail() { return email; }
    public String getNom() { return nom; }
    public String getResetToken() { return resetToken; }
    public void setEmail(String email) { this.email = email; }
    public void setNom(String nom) { this.nom = nom; }
    public void setResetToken(String t) { this.resetToken = t; }
}