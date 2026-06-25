package com.gdg.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gdg.auth.model.Utilisateur;
import com.gdg.auth.dto.RegisterRequest;
import com.gdg.auth.dto.VerifyEmailRequest;
import com.gdg.auth.dto.LoginRequest;
import com.gdg.auth.dto.MessageResponse;
import com.gdg.auth.dto.AuthResponse;
import com.gdg.auth.security.JwtUtil;
import com.gdg.auth.repository.UtilisateurRepository;
import java.util.List;


@Service
public class AuthService {

    //encodeur de motdepasse
    @Autowired
    private PasswordEncoder passwordEncoder;

    //utilitaire jwt pour genere/valider les token
    @Autowired
    private JwtUtil jwtUtil;

    //repository pour acceder a la bd postgresql
    @Autowired
    private UtilisateurRepository utilisateurRepository;

        //inscription d'un utilisateur

    public AuthResponse register(RegisterRequest request) {

        //verifier si l'utilisateur existe déjà grace a l'email
       if(utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }
        // Créer un nouvel utilisateur 
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse())); // Encoder le mot de passe
        utilisateur.setRole(request.getRole()!=null ? request.getRole() : Utilisateur.Role.CONSOMMATEUR); // Définir le rôle par défaut à CONSOMMATEUR si null  
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setStatut(Utilisateur.Statut.ACTIF); // Définir le statut par défaut à ACTIF                
        
        utilisateurRepository.save(utilisateur);//stocker un utilisateur en bd 
        

       //generer un token JWT pour l'utilisateur
        String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole().name());
        
        // Créer la réponse d'authentification
       AuthResponse authResponse = new AuthResponse();
       authResponse.setToken(token);
       authResponse.setRole(utilisateur.getRole().name());
       authResponse.setNom(utilisateur.getNom());
       authResponse.setEmail(utilisateur.getEmail());
       authResponse.setPrenom(utilisateur.getPrenom());
       return authResponse;

    }
        //connexion d'un utilisateur
       public AuthResponse login(LoginRequest request) {

        // Rechercher l'utilisateur par email dans la bd
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        if(utilisateur.getStatut() == Utilisateur.Statut.INACTIF) {
            throw new IllegalArgumentException("Compte non vérifié. Vérifiez votre email");
        }

                 //verifier si l'utilisateur est n'est pas suspendu
        if (utilisateur.getStatut() == Utilisateur.Statut.SUSPEMDU) {
            throw new IllegalArgumentException("Utilisateur suspendu");
        }


        // Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }

       
        // Générer un token JWT pour l'utilisateur
        String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole().name());

        // Créer la réponse d'authentification
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setRole(utilisateur.getRole().name()); //verifier si l'utilisateur est n'est pas suspendu
        if (utilisateur.getStatut() == Utilisateur.Statut.SUSPEMDU) {
            throw new IllegalArgumentException("Utilisateur suspendu");
        }


        authResponse.setNom(utilisateur.getNom());
        authResponse.setEmail(utilisateur.getEmail());
        authResponse.setPrenom(utilisateur.getPrenom());
        return authResponse;

    }

    //valider token JWT
    public boolean validateToken(String token) {
        return jwtUtil.isTokenValid(token);
    }

    //profile d'un utilisateur
    public Utilisateur getProfile(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
    }
    //verifier l'adresse email après inscription
    public MessageResponse verifyEmail(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé")); 
       utilisateur.setStatut(Utilisateur.Statut.ACTIF);
        utilisateurRepository.save(utilisateur);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Email vérifié avec succès");
        return messageResponse;
    }
    //refresh token JWT
    public AuthResponse refreshToken(String Token) {
        //verifier si le token est valide
        if (!jwtUtil.isTokenValid(Token)) {
            throw new IllegalArgumentException("Token invalide");
        }
        //extraire l'email et le role de l'utilisateur du token
        String email = jwtUtil.extractEmail(Token);

        //chercher l'utilisateur par email dans la bd
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
            
         //generer un nouveau token JWT pour l'utilisateur
        String newToken = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole().name());
        // Créer la réponse d'authentification       
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(newToken);
        authResponse.setEmail(utilisateur.getEmail());
        authResponse.setRole(utilisateur.getRole().name());
        return authResponse;
    }
    //forget password
    public MessageResponse forgotPassword(String email) {
        //verifier si l'utilisateur existe dans la bd
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        //genere un token de réinitialisation de mot de passe
        String resetToken = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole().name());

        //stocker le token de réinitialisation dans l'utilisateur
        utilisateur.setTokenVerification(resetToken);
        utilisateurRepository.save(utilisateur);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Token de reset : " + resetToken);
        return messageResponse;
    }
    //reset password
    public MessageResponse resetPassword(String token, String newMotDePasse) {
        String email = jwtUtil.extractEmail(token);
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        if (utilisateur.getTokenVerification() == null || !utilisateur.getTokenVerification().equals(token)) {
            throw new IllegalArgumentException("Token de réinitialisation invalide");
        }

        utilisateur.setMotDePasse(passwordEncoder.encode(newMotDePasse));
        utilisateur.setTokenVerification(null);
        utilisateurRepository.save(utilisateur);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Mot de passe réinitialisé avec succès");
        return messageResponse;
    }
    //get allusers
        public List<Utilisateur> getAllUsers() {
            return utilisateurRepository.findAll();
    }
    //suspendre un utilisateur
    public Utilisateur suspendUser(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

                //changement du statut de l'utilisateur à suspendu
        utilisateur.setStatut(Utilisateur.Statut.SUSPEMDU);
        utilisateurRepository.save(utilisateur);
        return utilisateur;
    }
    //supprimer un utilisateur
    public void deleteUser(Long id) {
        //chercher l'utilisateur par id dans la bd
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        utilisateurRepository.delete(utilisateur);
    }
       
   
}
