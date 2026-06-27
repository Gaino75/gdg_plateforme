package com.gdg.service_agences.service;

import com.gdg.service_agences.model.Agence;
import com.gdg.service_agences.model.Agence.StatutAgence;
import com.gdg.service_agences.model.Enseigne;
import com.gdg.service_agences.model.Ville;
import com.gdg.service_agences.repository.AgenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgenceService {

    private final AgenceRepository agenceRepository;
    private final EnseigneService enseigneService;
    private final VilleService villeService;

    // ============================================================
    // CONSTRUCTEUR (remplace @RequiredArgsConstructor)
    // ============================================================

    public AgenceService(AgenceRepository agenceRepository, EnseigneService enseigneService, VilleService villeService) {
        this.agenceRepository = agenceRepository;
        this.enseigneService = enseigneService;
        this.villeService = villeService;
    }

    // ============================================================
    // MÉTHODES
    // ============================================================

    @Transactional
    public Agence creerAgence(Agence agence, Long enseigneId, Long villeId) {
        Enseigne enseigne = enseigneService.getEnseigneById(enseigneId);
        Ville ville = villeService.getVilleById(villeId);

        agence.setEnseigne(enseigne);
        agence.setVille(ville);
        agence.setStatut(StatutAgence.EN_ATTENTE);
        agence.setDateCreation(LocalDateTime.now());

        return agenceRepository.save(agence);
    }

    public List<Agence> getAllAgences() {
        return agenceRepository.findAll();
    }

    public List<Agence> getAllAgencesWithHoraires() {
        return agenceRepository.findAll();
    }

    public List<Agence> getAgencesActives() {
        return agenceRepository.findByStatut(StatutAgence.ACTIF);
    }

    public List<Agence> getAgencesActivesWithHoraires() {
        return agenceRepository.findAllByStatutWithHoraires(StatutAgence.ACTIF);
    }

    public Agence getAgenceById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée avec l'id : " + id));
    }

    public Agence getAgenceByIdWithHoraires(Long id) {
        return agenceRepository.findByIdWithHoraires(id)
                .orElseThrow(() -> new RuntimeException("Agence non trouvée avec l'id : " + id));
    }

    public List<Agence> getAgencesByEnseigne(Long enseigneId) {
        Enseigne enseigne = enseigneService.getEnseigneById(enseigneId);
        return agenceRepository.findByEnseigne(enseigne);
    }

    public List<Agence> getAgencesActivesByEnseigne(Long enseigneId) {
        Enseigne enseigne = enseigneService.getEnseigneById(enseigneId);
        return agenceRepository.findByStatutAndEnseigne(StatutAgence.ACTIF, enseigne);
    }

    public List<Agence> getAgencesByVille(Long villeId) {
        Ville ville = villeService.getVilleById(villeId);
        return agenceRepository.findByVille(ville);
    }

    public List<Agence> getAgencesActivesByVille(Long villeId) {
        Ville ville = villeService.getVilleById(villeId);
        return agenceRepository.findByStatutAndVille(StatutAgence.ACTIF, ville);
    }

    public List<Agence> getAgencesByEnseigneAndVille(Long enseigneId, Long villeId) {
        Enseigne enseigne = enseigneService.getEnseigneById(enseigneId);
        Ville ville = villeService.getVilleById(villeId);
        return agenceRepository.findByEnseigneAndVille(enseigne, ville);
    }

    public List<Agence> getAgencesEnAttente() {
        return agenceRepository.findByStatutOrderByDateCreationAsc(StatutAgence.EN_ATTENTE);
    }

    @Transactional
    public Agence validerAgence(Long id, Long adminId) {
        Agence agence = getAgenceById(id);
        agence.setStatut(StatutAgence.ACTIF);
        agence.setDateValidation(LocalDateTime.now());
        agence.setValidePar(adminId);
        return agenceRepository.save(agence);
    }

    @Transactional
    public Agence suspendreAgence(Long id) {
        Agence agence = getAgenceById(id);
        agence.setStatut(StatutAgence.SUSPENDU);
        return agenceRepository.save(agence);
    }

    @Transactional
    public Agence reactiverAgence(Long id) {
        Agence agence = getAgenceById(id);
        agence.setStatut(StatutAgence.ACTIF);
        return agenceRepository.save(agence);
    }

    @Transactional
    public Agence updateAgence(Long id, Agence agenceDetails) {
        Agence agence = getAgenceById(id);
        agence.setNom(agenceDetails.getNom());
        agence.setAdresse(agenceDetails.getAdresse());
        agence.setLatitude(agenceDetails.getLatitude());
        agence.setLongitude(agenceDetails.getLongitude());
        agence.setTelephone(agenceDetails.getTelephone());
        agence.setEmail(agenceDetails.getEmail());
        agence.setLogoFacture(agenceDetails.getLogoFacture());
        agence.setEnteteFacture(agenceDetails.getEnteteFacture());
        agence.setPiedFacture(agenceDetails.getPiedFacture());
        return agenceRepository.save(agence);
    }

    public List<Agence> rechercherParNom(String nom) {
        return agenceRepository.findByNomContainingIgnoreCase(nom);
    }

    public long compterAgences() {
        return agenceRepository.count();
    }

    public long compterAgencesActives() {
        return agenceRepository.countByStatut(StatutAgence.ACTIF);
    }

    public long compterAgencesEnAttente() {
        return agenceRepository.countByStatut(StatutAgence.EN_ATTENTE);
    }

    public long compterAgencesParEnseigne(Long enseigneId) {
        Enseigne enseigne = enseigneService.getEnseigneById(enseigneId);
        return agenceRepository.countByEnseigne(enseigne);
    }
}