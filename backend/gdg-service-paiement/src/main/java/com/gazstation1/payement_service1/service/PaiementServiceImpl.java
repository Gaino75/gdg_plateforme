package com.gazstation1.payement_service1.service;

import com.gazstation1.payement_service1.client.ReservationClient;
import com.gazstation1.payement_service1.config.RabbitMQConfig;
import com.gazstation1.payement_service1.dto.PaiementDTO;
import com.gazstation1.payement_service1.enums.StatutPaiement;
import com.gazstation1.payement_service1.exception.PaiementNotFoundException;
import com.gazstation1.payement_service1.model.Paiement;
import com.gazstation1.payement_service1.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository paiementRepository;
    private final ReservationClient reservationClient;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public PaiementDTO.Response creerPaiement(PaiementDTO.CreationRequest request) {
        Paiement paiement = Paiement.builder()
                .commandeId(request.getCommandeId())
                .clientId(request.getClientId())
                .agenceId(request.getAgenceId())
                .montant(request.getMontant())
                .modePaiement(request.getModePaiement())
                .statut(StatutPaiement.EN_ATTENTE)
                .referenceTransaction(genererReference())
                .build();

        return toResponse(paiementRepository.save(paiement));
    }

    @Override
    @Transactional
    public PaiementDTO.Response initierPaiement(PaiementDTO.InitierRequest request) {
        String operateur = resolveOperateur(request.getModePaiement());

        Paiement paiement = Paiement.builder()
                .commandeId(request.getReservationId())
                .clientId(request.getConsommateurId())
                .agenceId(request.getAgenceId())
                .montant(request.getMontant())
                .modePaiement(request.getModePaiement())
                .numeroTelephone(request.getNumeroTelephone())
                .operateur(operateur)
                .statut(StatutPaiement.EN_ATTENTE)
                .referenceTransaction(genererReference())
                .build();

        Paiement saved = paiementRepository.save(paiement);
        log.info("Paiement initié ref={} reservation={}", saved.getReferenceTransaction(), request.getReservationId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public PaiementDTO.Response traiterCallbackOrange(PaiementDTO.CallbackRequest request) {
        return traiterCallback(request, "ORANGE");
    }

    @Override
    @Transactional
    public PaiementDTO.Response traiterCallbackMtn(PaiementDTO.CallbackRequest request) {
        return traiterCallback(request, "MTN");
    }

    private PaiementDTO.Response traiterCallback(PaiementDTO.CallbackRequest request, String operateur) {
        Paiement paiement = paiementRepository.findByReferenceTransaction(request.getReferenceTransaction())
                .orElseThrow(() -> new PaiementNotFoundException(
                        "Paiement introuvable ref=" + request.getReferenceTransaction()));

        paiement.setOperateur(operateur);
        paiement.setReferenceOperateur(request.getReferenceOperateur());

        boolean succes = "SUCCESS".equalsIgnoreCase(request.getStatut())
                || "CONFIRME".equalsIgnoreCase(request.getStatut())
                || "SUCCES".equalsIgnoreCase(request.getStatut());

        if (succes) {
            confirmerPaiementInterne(paiement);
        } else {
            echouerPaiementInterne(paiement, "Callback " + operateur + " rejeté");
        }

        return toResponse(paiementRepository.save(paiement));
    }

    @Override
    @Transactional
    public PaiementDTO.Response validerPaiement(Long id) {
        Paiement paiement = findById(id);
        confirmerPaiementInterne(paiement);
        return toResponse(paiementRepository.save(paiement));
    }

    @Override
    @Transactional
    public PaiementDTO.Response annulerPaiement(Long id) {
        Paiement paiement = findById(id);
        echouerPaiementInterne(paiement, "Paiement annulé manuellement");
        return toResponse(paiementRepository.save(paiement));
    }

    private void confirmerPaiementInterne(Paiement paiement) {
        paiement.setDateMiseAJour(LocalDateTime.now());

        if (paiement.getCommandeId() != null) {
            try {
                reservationClient.confirmerPaiement(paiement.getCommandeId(), paiement.getReferenceTransaction());
                paiement.setStatut(StatutPaiement.CONFIRME);
            } catch (Exception e) {
                log.error("paiement recu mais reservation non confirmer (ref=()):{}",paiement.getReferenceTransaction(),e.getMessage());
                paiement.setStatut(StatutPaiement.CONFIRME_A_VERIFIER);
                paiement.setMessageErreur("Argent recu mais reservation non confirnee :" + e.getMessage());
            }
            }else{
                paiement.setStatut(StatutPaiement.CONFIRME);
            }

        Map<String, Object> event = new HashMap<>();
        event.put("paiementId", paiement.getId());
        event.put("referenceTransaction", paiement.getReferenceTransaction());
        event.put("reservationId", paiement.getCommandeId());
        event.put("consommateurId", paiement.getClientId());
        event.put("montant", paiement.getMontant());
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.KEY_PAIEMENT_CONFIRME, event);
        } catch (Exception e) {
            log.warn("RabbitMQ indisponible — event paiement non publié: {}", e.getMessage());
        }
    }

    private void echouerPaiementInterne(Paiement paiement, String message) {
        paiement.setStatut(StatutPaiement.ECHOUE);
        paiement.setMessageErreur(message);
        paiement.setDateMiseAJour(LocalDateTime.now());

        if (paiement.getCommandeId() != null) {
            try {
                reservationClient.annulerReservation(paiement.getCommandeId(), message, paiement.getClientId());
            } catch (Exception e) {
                log.error("Erreur annulation réservation: {}", e.getMessage());
            }
        }

        Map<String, Object> event = new HashMap<>();
        event.put("paiementId", paiement.getId());
        event.put("reservationId", paiement.getCommandeId());
        event.put("message", message);
        event.put("consommateurId",paiement.getClientId());
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.KEY_PAIEMENT_ECHOUE, event);
        } catch (Exception e) {
            log.warn("RabbitMQ indisponible — event paiement echoue non publié: {}", e.getMessage());
        }
    }

    @Override
    public PaiementDTO.Response getPaiementById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public PaiementDTO.Response getPaiementByReference(String reference) {
        return paiementRepository.findByReferenceTransaction(reference)
                .map(this::toResponse)
                .orElseThrow(() -> new PaiementNotFoundException("Paiement introuvable ref=" + reference));
    }

    @Override
    public List<PaiementDTO.Response> getAllPaiements() {
        return paiementRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<PaiementDTO.Response> getPaiementsByClient(Long clientId) {
        return paiementRepository.findByClientId(clientId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    private Paiement findById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new PaiementNotFoundException("Paiement introuvable, id=" + id));
    }

    private String genererReference() {
        return "PAY-" + LocalDateTime.now().getYear() + "-"
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String resolveOperateur(String modePaiement) {
        if (modePaiement == null) return null;
        if (modePaiement.contains("ORANGE")) return "ORANGE";
        if (modePaiement.contains("MTN")) return "MTN";
        return null;
    }

    private PaiementDTO.Response toResponse(Paiement p) {
        return PaiementDTO.Response.builder()
                .id(p.getId())
                .commandeId(p.getCommandeId())
                .clientId(p.getClientId())
                .agenceId(p.getAgenceId())
                .montant(p.getMontant())
                .modePaiement(p.getModePaiement())
                .statut(p.getStatut())
                .referenceTransaction(p.getReferenceTransaction())
                .numeroTelephone(p.getNumeroTelephone())
                .operateur(p.getOperateur())
                .dateCreation(p.getDateCreation())
                .dateMiseAJour(p.getDateMiseAJour())
                .build();
    }

    @Override
    public List<PaiementDTO.Response>getPaiementsAVerifier(){
        return paiementRepository.findByStatut(StatutPaiement.CONFIRME_A_VERIFIER)
                           .stream()
                           .map(this::toResponse)
                           .toList();
    }
    @Override
    @Transactional

    public PaiementDTO.Response resoudrePaiementAVerifier(Long id,boolean confirmerQuandMeme){
        Paiement paiement = findById(id);

        if(confirmerQuandMeme){
            try{
            reservationClient.confirmerPaiement(paiement.getCommandeId(), paiement.getReferenceTransaction());
            paiement.setStatut(StatutPaiement.CONFIRME);
            }catch (Exception e){
                paiement.setStatut(StatutPaiement.REMBOURSE);
                paiement.setMessageErreur("impossible de confirmer la reservation("+e.getMessage()+")-remborsement requis a la place");

            }
        }else{
            paiement.setStatut(StatutPaiement.REMBOURSE);
            paiement.setMessageErreur("resolu manuellement:remboursement a effectuer cote operateur");
        }
        paiement.setDateMiseAJour(LocalDateTime.now());
        return toResponse(paiementRepository.save(paiement));
    }
}
