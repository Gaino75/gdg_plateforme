package com.gdg.service_agences.service;

import com.gdg.service_agences.model.Enseigne;
import com.gdg.service_agences.repository.EnseigneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnseigneService {

    private final EnseigneRepository enseigneRepository;

    // Créer une nouvelle enseigne
    @Transactional
    public Enseigne creerEnseigne(Enseigne enseigne) {
        return enseigneRepository.save(enseigne);
    }

    // Récupérer toutes les enseignes
    public List<Enseigne> getAllEnseignes() {
        return enseigneRepository.findAll();
    }

    // Récupérer les enseignes actives uniquement
    public List<Enseigne> getEnseignesActives() {
        return enseigneRepository.findByStatut(Enseigne.StatutEnseigne.ACTIF);
    }

    // Récupérer une enseigne par son ID
    public Enseigne getEnseigneById(Long id) {
        return enseigneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseigne non trouvée avec l'id : " + id));
    }

    // Récupérer une enseigne par son nom
    public Enseigne getEnseigneByNom(String nom) {
        return enseigneRepository.findByNom(nom)
                .orElseThrow(() -> new RuntimeException("Enseigne non trouvée : " + nom));
    }

    // Mettre à jour une enseigne
    @Transactional
    public Enseigne updateEnseigne(Long id, Enseigne enseigneDetails) {
        Enseigne enseigne = getEnseigneById(id);
        enseigne.setNom(enseigneDetails.getNom());
        enseigne.setLogo(enseigneDetails.getLogo());
        enseigne.setDescription(enseigneDetails.getDescription());
        return enseigneRepository.save(enseigne);
    }

    // Désactiver une enseigne
    @Transactional
    public void desactiverEnseigne(Long id) {
        Enseigne enseigne = getEnseigneById(id);
        enseigne.setStatut(Enseigne.StatutEnseigne.INACTIF);
        enseigneRepository.save(enseigne);
    }

    // Supprimer une enseigne (si elle n'a pas d'agences)
    @Transactional
    public void supprimerEnseigne(Long id) {
        Enseigne enseigne = getEnseigneById(id);
        if (!enseigne.getAgences().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer : cette enseigne a des agences");
        }
        enseigneRepository.delete(enseigne);
    }
}