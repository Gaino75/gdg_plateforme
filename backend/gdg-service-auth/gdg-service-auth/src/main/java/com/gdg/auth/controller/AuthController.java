package com.gdg.auth.controller;

import com.gdg.auth.dto.*;
import com.gdg.auth.model.Utilisateur;
import com.gdg.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;





@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ── INSCRIPTION CONSOMMATEUR ──────────────────
    @PostMapping("/register/consommateur")
    public ResponseEntity<AuthResponse> registerConsommateur(
            @Valid @RequestBody RegisterRequest request) {
        request.setRole(Utilisateur.Role.CONSOMMATEUR);
        return ResponseEntity.ok(authService.register(request));
    }

    // ── INSCRIPTION DISTRIBUTEUR ──────────────────
    @PostMapping("/register/distributeur")
    public ResponseEntity<AuthResponse> registerDistributeur(
            @Valid @RequestBody RegisterRequest request) {
        request.setRole(Utilisateur.Role.DISTRIBUTEUR);
        return ResponseEntity.ok(authService.register(request));
    }

    // ── SETUP PREMIER ADMIN ───────────────────
@PostMapping("/register/setup")
public ResponseEntity<?> registerFirstAdmin(
        @Valid @RequestBody RegisterRequest request) {

    boolean hasAdmin = authService.getAllUtilisateurs()
        .stream()
        .anyMatch(u -> u.getRole().name().equals("ADMIN"));

    if (hasAdmin) {
        return ResponseEntity.status(403)
            .body("Un admin existe déjà");
    }
    request.setRole(Utilisateur.Role.ADMIN);
    return ResponseEntity.ok(authService.register(request));
}
// ── INSCRIPTION ADMIN (avec token) ───────
@PostMapping("/register/admin")
public ResponseEntity<?> registerAdmin(
        @RequestHeader("Authorization") String tokenHeader,
        @Valid @RequestBody RegisterRequest request) {

    String token = tokenHeader.replace("Bearer ", "");
    if (!authService.validateToken(token)) {
        return ResponseEntity.status(403).body("Token invalide");
    }
    String role = authService.extractRoleFromToken(token);
    if (!"ADMIN".equals(role)) {
        return ResponseEntity.status(403).body("Accès refusé");
    }
    request.setRole(Utilisateur.Role.ADMIN);
    return ResponseEntity.ok(authService.register(request));
}
    // ── CONNEXION ────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        // Récupérer l'IP du client
        String ip = httpRequest.getRemoteAddr();
        return ResponseEntity.ok(authService.login(request, ip));
    }

    // ── DECONNEXION ──────────────────────────────
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String token,
            @RequestHeader("UserId") Long userId) {
        String jwt = token.replace("Bearer ", "");
        return ResponseEntity.ok(authService.logout(jwt, userId));
    }

    // ── VALIDER TOKEN ────────────────────────────
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(
            @RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        return ResponseEntity.ok(authService.validateToken(jwt));
    }

    // ── VERIFY EMAIL ─────────────────────────────
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    // ── PROFIL ───────────────────────────────────
    @GetMapping("/profil")
    public ResponseEntity<ProfilResponse> getProfil(
            @RequestHeader("Authorization") String token) {
        String email = authService.extractEmailFromToken(
            token.replace("Bearer ", ""));
        return ResponseEntity.ok(authService.getProfil(email));
    }

    // ── FORGOT PASSWORD ──────────────────────────
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(
            authService.forgotPassword(request));
    }

    // ── RESET PASSWORD ───────────────────────────
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(
            authService.resetPassword(request));
    }

                               //-endpoint pour service admin

    //-lister tous les utilisateur
    @GetMapping("/admin/users")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs(){
        return ResponseEntity.ok(authService.getAllUtilisateurs());
    }

    //--suspendre un utilisateur
    @PutMapping("/admin/utilisateurs/{id}/suspendre")
    public ResponseEntity<String> suspendre(@PathVariable long id,
                                            @RequestBody Map<String,String> body){
                                                return ResponseEntity.ok(
                                                    authService.suspendreUtilisateur(id,body.get("motif"))
                                                );
                                            }
                                         
    //reactiver un utilisateur
    @PutMapping("/admin/utilisateurs/{id}/reactiver")
    public  ResponseEntity<String> reactiver(@PathVariable long id){
      return ResponseEntity.ok(
                    authService.reactiverUtilisateur(id)); 
    }

    //supprimer un utilisateur
    @DeleteMapping("/admin/utilisateurs/{id}")
    public  ResponseEntity<String> supprimer(
            @PathVariable Long id){
                return ResponseEntity.ok(
                    authService.supprimerUtilisateur(id));
            }
    
}