package com.gdg.auth.controller;

import com.gdg.auth.dto.AuthResponse;
import com.gdg.auth.dto.LoginRequest;
import com.gdg.auth.dto.RegisterRequest;
import com.gdg.auth.dto.VerifyEmailRequest;
import com.gdg.auth.dto.RefreshTokenRequest;
import com.gdg.auth.dto.ForgotPasswordRequest;
import com.gdg.auth.dto.ResetPasswordRequest;
import com.gdg.auth.dto.MessageResponse;
import com.gdg.auth.model.Utilisateur;
import com.gdg.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Inscription
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // Connexion
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Valider token
    @GetMapping("/validate")  
    public ResponseEntity<Boolean> validate(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "").trim();
        boolean isvalid = authService.validateToken(jwt);
        return ResponseEntity.ok(isvalid);
    }    

    // Vérifier l'adresse email après inscription
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam(required = false) String token, @RequestBody(required = false) VerifyEmailRequest request) {
        if (request == null) {
            request = new VerifyEmailRequest();
        }
        if (token != null) {
            request.setToken(token);
        }
        if (request.getToken() == null) {
            return ResponseEntity.badRequest().body("Le token est obligatoire");
        }
        try {
            MessageResponse response = authService.verifyEmail(request.getToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Renouveler le token JWT
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Demande de réinitialisation de mot de passe (oublie)
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            MessageResponse response = authService.forgotPassword(request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Enregistrer le nouveau mot de passe
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            MessageResponse response = authService.resetPassword(request.getToken(), request.getNewMotDePasse());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- ENDPOINTS POUR COMMUNICATION MICROSERVICES (SERVICE ADMIN) ---
    @GetMapping("/admin/users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PutMapping("/admin/users/{id}/suspend")
    public ResponseEntity<?> suspendUser(@PathVariable Long id) {
        try {
            Utilisateur user = authService.suspendUser(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            authService.deleteUser(id);
            return ResponseEntity.ok().body("Utilisateur supprimé avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
