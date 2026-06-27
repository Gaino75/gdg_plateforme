package com.gdg.service_agences.service;

import com.gdg.service_agences.model.Ville;
import com.gdg.service_agences.repository.VilleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VilleService {

    private final VilleRepository villeRepository;

    // ============================================================
    // CONSTRUCTEUR (remplace @RequiredArgsConstructor)
    // ============================================================

    public VilleService(VilleRepository villeRepository) {
        this.villeRepository = villeRepository;
    }

    // ============================================================
    // MÉTHODES
    // ============================================================

    @Transactional
    public Ville creerVille(Ville ville) {
        return villeRepository.save(ville);
    }

    public List<Ville> getAllVilles() {
        return villeRepository.findAll();
    }

    public Ville getVilleById(Long id) {
        return villeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ville non trouvée avec l'id : " + id));
    }

    public List<Ville> getVillesByRegion(String region) {
        return villeRepository.findByRegion(region);
    }

    @Transactional
    public Ville updateVille(Long id, Ville villeDetails) {
        Ville ville = getVilleById(id);
        ville.setNom(villeDetails.getNom());
        ville.setRegion(villeDetails.getRegion());
        ville.setPays(villeDetails.getPays());
        return villeRepository.save(ville);
    }

    @Transactional
    public void supprimerVille(Long id) {
        Ville ville = getVilleById(id);
        if (ville.getAgences() != null && !ville.getAgences().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer : cette ville a des agences");
        }
        villeRepository.delete(ville);
    }
}