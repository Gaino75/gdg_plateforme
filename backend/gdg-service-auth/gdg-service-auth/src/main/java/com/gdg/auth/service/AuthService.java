package com.gdg.auth.service;

import com.gdg.auth.config.RabbitMQConfig;
import com.gdg.auth.dto.*;
import com.gdg.auth.event.PasswordResetEvent;
import com.gdg.auth.event.UserRegisteredEvent;
import com.gdg.auth.model.*;
import com.gdg.auth.repository.*;
import com.gdg.auth.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    private TentativeConnexionRepository tentativeConnexionRepository;

    @Autowired
private RabbitTemplate rabbitTemplate;


    // ── INSCRIPTION ──────────────────────────────
    public AuthResponse register(RegisterRequest request) {

        // Vérifier si email déjà utilisé
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Utilisateur utilisateur;

        // Créer selon le rôle
        switch (request.getRole()) {
            case CONSOMMATEUR:
                Consommateur consommateur = new Consommateur();
                consommateur.setVilleResidence(request.getVilleResidence());
                consommateur.setDateNaissance(request.getDateNaissance());
                utilisateur = consommateur;
                break;

            case DISTRIBUTEUR:
                Distributeur distributeur = new Distributeur();
                distributeur.setPoste(request.getPoste());
                // agenceId null au départ
                utilisateur = distributeur;
                break;

            default: // ADMIN
                Administrateur admin = new Administrateur();
                if (request.getNiveauAcces() != null) {
                    admin.setNiveauAcces(request.getNiveauAcces());
                }
                utilisateur = admin;
        }

        // Attributs communs
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(
            passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setRole(request.getRole());
        utilisateur.setStatut(Utilisateur.Statut.ACTIF);
        utilisateur.setDateInscription(LocalDateTime.now());

        // Générer token de vérification email
        String tokenVerif = UUID.randomUUID().toString();
        utilisateur.setEmailVerifie(false);
        utilisateur.setTokenVerification(tokenVerif);
        utilisateur.setDateExpirationToken(
            LocalDateTime.now().plusHours(24));

        // Sauvegarder
        utilisateurRepository.save(utilisateur);

        //  Envoyer email via Service Notifications
        // notificationService.envoyerEmailVerification(email, tokenVerif)
        // Publier événement inscription
       rabbitTemplate.convertAndSend(
             RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.KEY_USER_REGISTERED,
            new UserRegisteredEvent(
              utilisateur.getId(),
              utilisateur.getEmail(),
            utilisateur.getNom(),
             utilisateur.getPrenom(),
            utilisateur.getRole().name(),
            utilisateur.getTokenVerification()
    )
);

        // Générer JWT
        String token = jwtUtil.generateToken(
            utilisateur.getId(),
            utilisateur.getRole().name()
        );

        return new AuthResponse(
            token,
            utilisateur.getRole().name(),
            utilisateur.getNom(),
            utilisateur.getEmail()
        );
    }

    
    // ── CONNEXION ────────────────────────────────
    public AuthResponse login(LoginRequest request,
                              String adresseIp) {

        // Vérifier si IP bloquée (5 échecs en 1 min)
        LocalDateTime ilYaUneMinute = LocalDateTime.now().minusMinutes(1);
        long nbEchecs = tentativeConnexionRepository
            .countByAdresseIpAndSuccesFalseAndDateTentativeAfter(
                adresseIp, ilYaUneMinute);

        if (nbEchecs >= 5) {
            throw new RuntimeException(
                "Trop de tentatives. Réessayez dans 1 minute.");
        }

        try {
            // Chercher utilisateur
            Utilisateur utilisateur = utilisateurRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException(
                    "Email ou mot de passe incorrect"));

            // Vérifier mot de passe
            if (!passwordEncoder.matches(
                    request.getMotDePasse(),
                    utilisateur.getMotDePasse())) {
                // Enregistrer échec
                enregistrerTentative(request.getEmail(),
                    adresseIp, false);
                throw new RuntimeException(
                    "Email ou mot de passe incorrect");
            }

            // Vérifier statut
            if (utilisateur.getStatut() == Utilisateur.Statut.SUSPENDU) {
                throw new RuntimeException("Compte suspendu");
            }

            // Enregistrer succès
            enregistrerTentative(request.getEmail(), adresseIp, true);

            // Générer JWT
            String token = jwtUtil.generateToken(
                utilisateur.getId(),
                utilisateur.getRole().name()
            );

            return new AuthResponse(
                token,
                utilisateur.getRole().name(),
                utilisateur.getNom(),
                utilisateur.getEmail()
            );

        } catch (RuntimeException e) {
            // Enregistrer échec si erreur
            enregistrerTentative(request.getEmail(), adresseIp, false);
            throw e;
        }
    }

    // ── DECONNEXION ──────────────────────────────
    public String logout(String token, Long utilisateurId) {

        // Blacklister le token
        TokenBlacklist blacklist = new TokenBlacklist();
        blacklist.setToken(token);
        blacklist.setUtilisateurId(utilisateurId);
        blacklist.setDateRevocation(LocalDateTime.now());
        blacklist.setRaison("DECONNEXION");
        tokenBlacklistRepository.save(blacklist);

        return "Déconnexion réussie";
    }

    // ── VALIDER TOKEN ────────────────────────────
    public boolean validateToken(String token) {

        // Vérifier blacklist
        if (tokenBlacklistRepository.existsByToken(token)) {
            return false;
        }

        return jwtUtil.isTokenValid(token);
    }

    // ── VERIFY EMAIL ─────────────────────────────
    public String verifyEmail(String token) {

        // Chercher utilisateur avec ce token
        Utilisateur utilisateur = utilisateurRepository
            .findByTokenVerification(token)
            .orElseThrow(() -> new RuntimeException(
                "Token invalide"));

      // Vérifier expiration
        if (utilisateur.getDateExpirationToken()
                .isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expiré");
        }

        // Activer email
        utilisateur.setEmailVerifie(true);
        utilisateur.setTokenVerification(null);
        utilisateur.setDateExpirationToken(null);
        utilisateurRepository.save(utilisateur);

        return "Email vérifié avec succès";
    }

    // ── PROFIL ───────────────────────────────────
    public ProfilResponse getProfilById(Long Id) {

        Utilisateur u = utilisateurRepository
            .findById(Id)
            .orElseThrow(() -> new RuntimeException(
                "Utilisateur non trouvé"));

        return new ProfilResponse(
            u.getId(),
            u.getNom(),
            u.getPrenom(),
            u.getEmail(),
            u.getTelephone(),
            u.getRole().name(),
            u.getStatut().name(),
            u.getDateInscription(),
            u.getEmailVerifie()
        );
    }

    // ── FORGOT PASSWORD ──────────────────────────
    public String forgotPassword(ForgotPasswordRequest request) {

        Utilisateur utilisateur = utilisateurRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException(
                "Email non trouvé"));

        // Générer token reset
        String resetToken = UUID.randomUUID().toString();

        // Stocker dans tokenVerification (réutilisation)
        utilisateur.setTokenVerification(resetToken);
        utilisateur.setDateExpirationToken(
            LocalDateTime.now().plusMinutes(15));
        utilisateurRepository.save(utilisateur);

        //  Envoyer email via Service Notifications
        // Ex: http://gpg.cm/reset-password?token=resetToken

        // Publier événement reset password
rabbitTemplate.convertAndSend(
    RabbitMQConfig.EXCHANGE,
    RabbitMQConfig.KEY_PASSWORD_RESET,
    new PasswordResetEvent(
        utilisateur.getEmail(),
        utilisateur.getNom(),
        resetToken
    )
);

        return "Token de reset : " + resetToken;
    }

    // ── RESET PASSWORD ───────────────────────────
    public String resetPassword(ResetPasswordRequest request) {

        // Chercher utilisateur avec ce token
        Utilisateur utilisateur = utilisateurRepository
            .findByTokenVerification(request.getResetToken())
            .orElseThrow(() -> new RuntimeException(
                "Token invalide ou expiré"));

        // Vérifier expiration
        if (utilisateur.getDateExpirationToken()
                .isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expiré");
        }

        // Mettre à jour mot de passe
        utilisateur.setMotDePasse(
            passwordEncoder.encode(request.getNouveauMotDePasse()));
        utilisateur.setTokenVerification(null);
        utilisateur.setDateExpirationToken(null);
        utilisateurRepository.save(utilisateur);

        return "Mot de passe réinitialisé avec succès";
    }

    // ── EXTRAIRE EMAIL DU TOKEN ───────────────────
   

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }
       // ── METHODE PRIVEE : Enregistrer tentative ────
    private void enregistrerTentative(String email,
                                       String ip,
                                       boolean succes) {
        TentativeConnexion tentative = new TentativeConnexion();
        tentative.setEmail(email);
        tentative.setAdresseIp(ip);
        tentative.setSucces(succes);
        tentative.setDateTentative(LocalDateTime.now());
        tentativeConnexionRepository.save(tentative);
    }


    public String extractRoleFromToken(String token) {
        return jwtUtil.extractRole(token);
    }

//suspendre un utilisateur
    public String suspendreUtilisateur(long id, String motif) {
        Utilisateur utilisateur=utilisateurRepository.findById(id)
                                                      .orElseThrow(()->new RuntimeException("utilisateur non trouver"));
        utilisateur.setStatut(Utilisateur.Statut.SUSPENDU);
        utilisateurRepository.save(utilisateur);
        return "Utilisateur suspendu.Motif:" + motif ;
        
    }

//Reactive un utilisateur
    public String reactiverUtilisateur(long id) {
         Utilisateur utilisateur=utilisateurRepository.findById(id)
                                                      .orElseThrow(()->new RuntimeException("utilisateur non trouver"));
        utilisateur.setStatut(Utilisateur.Statut.ACTIF);
        utilisateurRepository.save(utilisateur);
        return "Utilisateur reactiver avec succes";
        
        
    }


    public String supprimerUtilisateur(Long id) {
         Utilisateur utilisateur=utilisateurRepository.findById(id)
                                                      .orElseThrow(()->new RuntimeException("utilisateur non trouver"));
        utilisateur.setStatut(Utilisateur.Statut.ACTIF);
        utilisateurRepository.delete(utilisateur);
        return "Utilisateur supprimer avec succes";
        
        
    }

    
}       