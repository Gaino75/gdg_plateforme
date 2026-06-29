package com.gdg.auth.service;

import com.gdg.auth.dto.DistributeurInfoResponse;
import com.gdg.auth.model.Distributeur;
import com.gdg.auth.model.Utilisateur;
import com.gdg.auth.repository.DistributeurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributeurService {

    @Autowired
    private DistributeurRepository distributeurRepository;

    public String assignerAgence(Long distributeurId, Long agenceId) {
        Distributeur distributeur = distributeurRepository.findById(distributeurId)
            .orElseThrow(() -> new RuntimeException("Distributeur non trouvé"));
        if (distributeur.getRole() != Utilisateur.Role.DISTRIBUTEUR) {
            throw new RuntimeException("L'utilisateur n'est pas un distributeur");
        }
        if (distributeur.getAgenceId() != null
                && !distributeur.getAgenceId().equals(agenceId)) {
            throw new RuntimeException("Ce distributeur est déjà lié à une agence");
        }
        distributeur.setAgenceId(agenceId);
        distributeurRepository.save(distributeur);
        return "Agence assignée au distributeur";
    }

    public DistributeurInfoResponse getByAgenceId(Long agenceId) {
        Distributeur distributeur = distributeurRepository.findByAgenceId(agenceId)
            .orElseThrow(() -> new RuntimeException(
                "Aucun distributeur pour l'agence " + agenceId));
        return toResponse(distributeur);
    }

    public DistributeurInfoResponse getByEmail(String email) {
        Distributeur distributeur = distributeurRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException(
                "Distributeur non trouvé pour l'email " + email));
        return toResponse(distributeur);
    }

    public String retirerAgence(Long agenceId) {
        Distributeur distributeur = distributeurRepository.findByAgenceId(agenceId)
            .orElseThrow(() -> new RuntimeException(
                "Aucun distributeur pour l'agence " + agenceId));
        distributeur.setAgenceId(null);
        distributeurRepository.save(distributeur);
        return "Lien agence retiré du distributeur";
    }

    private DistributeurInfoResponse toResponse(Distributeur distributeur) {
        return new DistributeurInfoResponse(
            distributeur.getId(),
            distributeur.getNom(),
            distributeur.getPrenom(),
            distributeur.getEmail(),
            distributeur.getAgenceId()
        );
    }
}
