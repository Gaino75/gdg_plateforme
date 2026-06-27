package com.gdg.service_agences.service;

import com.gdg.service_agences.model.Enseigne;
import com.gdg.service_agences.repository.EnseigneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnseigneService {

    private final EnseigneRepository enseigneRepository;

    // ============================================================
    // CONSTRUCTEUR (remplace @RequiredArgsConstructor)
    // ============================================================

    public EnseigneService(EnseigneRepository enseigneRepository) {
        this.enseigneRepository = enseigneRepository;
    }

    // ============================================================
    // MÉTHODES
    // ============================================================

    @Transactional
    public Enseigne creerEnseigne(Enseigne enseigne) {
        return enseigneRepository.save(enseigne);
    }

    public List<Enseigne> getAllEnseignes() {
        return enseigneRepository.findAll();
    }

    public List<Enseigne> getEnseignesActives() {
        return enseigneRepository.findByStatut(Enseigne.StatutEnseigne.ACTIF);
    }

    public Enseigne getEnseigneById(Long id) {
        return enseigneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseigne non trouvée avec l'id : " + id));
    }

    public Enseigne getEnseigneByNom(String nom) {
        return enseigneRepository.findByNom(nom)
                .orElseThrow(() -> new RuntimeException("Enseigne non trouvée : " + nom));
    }

    @Transactional
    public Enseigne updateEnseigne(Long id, Enseigne enseigneDetails) {
        Enseigne enseigne = getEnseigneById(id);
        enseigne.setNom(enseigneDetails.getNom());
        enseigne.setLogo(enseigneDetails.getLogo());
        enseigne.setDescription(enseigneDetails.getDescription());
        enseigne.setSiteWeb(enseigneDetails.getSiteWeb());
        enseigne.setTelephone(enseigneDetails.getTelephone());
        enseigne.setEmailContact(enseigneDetails.getEmailContact());
        return enseigneRepository.save(enseigne);
    }

    @Transactional
    public void desactiverEnseigne(Long id) {
        Enseigne enseigne = getEnseigneById(id);
        enseigne.setStatut(Enseigne.StatutEnseigne.INACTIF);
        enseigneRepository.save(enseigne);
    }

    @Transactional
    public void supprimerEnseigne(Long id) {
        Enseigne enseigne = getEnseigneById(id);
        if (enseigne.getAgences() != null && !enseigne.getAgences().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer : cette enseigne a des agences");
        }
        enseigneRepository.delete(enseigne);
    }
}