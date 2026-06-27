package com.gdg.service_agences.service;

import com.gdg.service_agences.model.Ville;
import com.gdg.service_agences.repository.VilleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VilleService {

    private final VilleRepository villeRepository;

    // Créer une nouvelle ville
    @Transactional
    public Ville creerVille(Ville ville) {
        return villeRepository.save(ville);
    }

    // Récupérer toutes les villes
    public List<Ville> getAllVilles() {
        return villeRepository.findAll();
    }

    // Récupérer une ville par son ID
    public Ville getVilleById(Long id) {
        return villeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ville non trouvée avec l'id : " + id));
    }

    // Récupérer les villes d'une région
    public List<Ville> getVillesByRegion(String region) {
        return villeRepository.findByRegion(region);
    }

    // Mettre à jour une ville
    @Transactional
    public Ville updateVille(Long id, Ville villeDetails) {
        Ville ville = getVilleById(id);
        ville.setNom(villeDetails.getNom());
        ville.setRegion(villeDetails.getRegion());
        ville.setPays(villeDetails.getPays());
        return villeRepository.save(ville);
    }

    // Supprimer une ville (si elle n'a pas d'agences)
    @Transactional
    public void supprimerVille(Long id) {
        Ville ville = getVilleById(id);
        if (!ville.getAgences().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer : cette ville a des agences");
        }
        villeRepository.delete(ville);
    }
}