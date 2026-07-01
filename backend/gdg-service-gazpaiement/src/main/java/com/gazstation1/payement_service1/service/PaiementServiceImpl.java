package com.gazstation1.payement_service1.service;

import com.gazstation1.payement_service1.client.CommandeClient;
import com.gazstation1.payement_service1.dto.PaiementDTO;
import com.gazstation1.payement_service1.enums.StatutPaiement;
import com.gazstation1.payement_service1.exception.PaiementNotFoundException;
import com.gazstation1.payement_service1.model.Paiement;
import com.gazstation1.payement_service1.repository.PaiementRepository;
import com.gazstation1.payement_service1.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
   // private final CommandeClient commandeClient;

    @Override
    @Transactional
    public PaiementDTO.Response creerPaiement(PaiementDTO.CreationRequest request) {

        // Vérifie que la commande existe dans le microservice Commande
        //Boolean commandeExiste = commandeClient.commandeExiste(request.getCommandeId());
        //if (commandeExiste == null || !commandeExiste) {
          //  throw new IllegalArgumentException("La commande " + request.getCommandeId() + " n'existe pas");
        //}

        Paiement paiement = Paiement.builder()
                .commandeId(request.getCommandeId())
                .clientId(request.getClientId())
                .montant(request.getMontant())
                .modePaiement(request.getModePaiement())
                .statut(StatutPaiement.EN_ATTENTE)
                .referenceTransaction(genererReference())
                .build();

      Paiement saved =paiementRepository.save(paiement);

        // Simulation : validation immédiate du paiement
        saved.setStatut(StatutPaiement.REUSSI);
       saved = paiementRepository.save(saved);

        // Informe le microservice Commande que la commande est payée
      //  commandeClient.mettreAJourStatutCommande(request.getCommandeId(), "PAYEE");

        return toResponse(saved);
    }

    @Override
    public PaiementDTO.Response getPaiementById(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new PaiementNotFoundException("Paiement introuvable, id=" + id));
        return toResponse(paiement);
    }

    @Override
    public List<PaiementDTO.Response> getAllPaiements() {
        return paiementRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaiementDTO.Response> getPaiementsByClient(Long clientId) {
        return paiementRepository.findByClientId(clientId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaiementDTO.Response validerPaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new PaiementNotFoundException("Paiement introuvable, id=" + id));
        paiement.setStatut(StatutPaiement.REUSSI);
        return toResponse(paiementRepository.save(paiement));
    }

    @Override
    @Transactional
    public PaiementDTO.Response annulerPaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new PaiementNotFoundException("Paiement introuvable, id=" + id));
        paiement.setStatut(StatutPaiement.ECHEC);
        return toResponse(paiementRepository.save(paiement));
    }

    private String genererReference() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }

    private PaiementDTO.Response toResponse(Paiement p) {
        return PaiementDTO.Response.builder()
                .id(p.getId())
                .commandeId(p.getCommandeId())
                .clientId(p.getClientId())
                .montant(p.getMontant())
                .modePaiement(p.getModePaiement())
                .statut(p.getStatut())
                .referenceTransaction(p.getReferenceTransaction())
                .dateCreation(p.getDateCreation())
                .dateMiseAJour(p.getDateMiseAJour())
                .build();
    }
}