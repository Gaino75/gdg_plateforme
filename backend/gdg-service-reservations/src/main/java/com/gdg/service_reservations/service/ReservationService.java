package com.gdg.service_reservations.service;

import com.gdg.service_reservations.client.PaiementServiceClient;
import com.gdg.service_reservations.client.StockServiceClient;
import com.gdg.service_reservations.config.RabbitMQConfig;
import com.gdg.service_reservations.model.HistoriqueReservation;
import com.gdg.service_reservations.model.Reservation;
import com.gdg.service_reservations.model.Reservation.ModePaiement;
import com.gdg.service_reservations.model.Reservation.StatutReservation;
import com.gdg.service_reservations.repository.HistoriqueReservationRepository;
import com.gdg.service_reservations.repository.ReservationRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HistoriqueReservationRepository historiqueReservationRepository;
    private final StockServiceClient stockServiceClient;
    private final PaiementServiceClient paiementServiceClient;
    private final RabbitTemplate rabbitTemplate;

    public ReservationService(ReservationRepository reservationRepository,
                                HistoriqueReservationRepository historiqueReservationRepository,
                                StockServiceClient stockServiceClient,
                                PaiementServiceClient paiementServiceClient,
                                RabbitTemplate rabbitTemplate) {
        this.reservationRepository = reservationRepository;
        this.historiqueReservationRepository = historiqueReservationRepository;
        this.stockServiceClient = stockServiceClient;
        this.paiementServiceClient = paiementServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    private String genererReference() {
        long count = reservationRepository.count() + 1;
        return String.format("RES-%d-%06d", LocalDateTime.now().getYear(), count);
    }

    @Transactional
    public Reservation creerReservation(Long agenceId, Long consommateurId,
                                          Long categorieProduitId, Integer quantite,
                                          ModePaiement modePaiement, String numeroTelephone) {


        stockServiceClient.verifierDisponibilite(agenceId, categorieProduitId, quantite);
        Double prixUnitaire = stockServiceClient.getPrixUnitaire(agenceId, categorieProduitId);
        Double montantTotal = prixUnitaire * quantite;

        String reference = genererReference();
        Reservation reservation = new Reservation(
                reference, agenceId, consommateurId, categorieProduitId,
                quantite, prixUnitaire, montantTotal, modePaiement
        );

        Reservation saved = reservationRepository.save(reservation);
        enregistrerHistorique(saved.getId(), null, StatutReservation.EN_ATTENTE.name(),
                "Création de la réservation", consommateurId);

        if (modePaiement == ModePaiement.ORANGE_MONEY || modePaiement == ModePaiement.MTN_MOBILE_MONEY) {
            Map<String, Object> paiement = paiementServiceClient.initierPaiement(
                    saved.getId(), consommateurId, agenceId, montantTotal,
                    modePaiement.name(), numeroTelephone
            );
            if (paiement != null && paiement.get("referenceTransaction") != null) {
                saved.setReferencePaiement(paiement.get("referenceTransaction").toString());
                saved = reservationRepository.save(saved);
            }
        }

        return saved;
    }

    @Transactional
    public Reservation confirmerPaiement(Long reservationId, String referencePaiement) {
        Reservation reservation = getReservationById(reservationId);

        if (reservation.getStatut() == StatutReservation.EXPIREE) {
            throw new RuntimeException("Cette réservation a expiré");
        }
        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            throw new RuntimeException("Cette réservation a été annulée");
        }
        if (reservation.getStatut() == StatutReservation.PAYEE
                || reservation.getStatut() == StatutReservation.CONFIRMEE) {
            throw new RuntimeException("Cette réservation a déjà été payée");
        }

        String ancienStatut = reservation.getStatut().name();
        reservation.setStatut(StatutReservation.PAYEE);
        reservation.setReferencePaiement(referencePaiement);
        reservation.setDateConfirmation(LocalDateTime.now());

        stockServiceClient.decrementerStock(
                reservation.getAgenceId(),
                reservation.getCategorieProduitId(),
                reservation.getQuantite(),
                reservation.getReferenceReservation(),
                reservation.getConsommateurId()
        );

        Reservation saved = reservationRepository.save(reservation);
        enregistrerHistorique(saved.getId(), ancienStatut, StatutReservation.PAYEE.name(),
                "Paiement confirmé - Réf: " + referencePaiement, reservation.getConsommateurId());

        publierEvenementReservationConfirmee(saved);
        return saved;
    }

    private void publierEvenementReservationConfirmee(Reservation reservation) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("reservationId", reservation.getId());
            event.put("referenceReservation", reservation.getReferenceReservation());
            event.put("agenceId", reservation.getAgenceId());
            event.put("consommateurId", reservation.getConsommateurId());
            event.put("montantTotal", reservation.getMontantTotal());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.KEY_RESERVATION_CONFIRMEE, event);
        } catch (Exception e) {
            System.err.println("RabbitMQ indisponible — event reservation non publié: " + e.getMessage());
        }
    }

    private void enregistrerHistorique(Long reservationId, String ancienStatut,
                                       String nouveauStatut, String commentaire, Long effectuePar) {
        historiqueReservationRepository.save(new HistoriqueReservation(
                reservationId, ancienStatut, nouveauStatut, commentaire, effectuePar
        ));
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id : " + id));
    }

    public Reservation getReservationByReference(String reference) {
        return reservationRepository.findByReferenceReservation(reference)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec la référence : " + reference));
    }

    public List<Reservation> getReservationsByConsommateur(Long consommateurId) {
        return reservationRepository.findByConsommateurIdOrderByDateReservationDesc(consommateurId);
    }

    public List<Reservation> getReservationsActivesByConsommateur(Long consommateurId) {
        return reservationRepository.findByConsommateurIdAndStatut(consommateurId, StatutReservation.EN_ATTENTE);
    }

    public List<Reservation> getReservationsByAgence(Long agenceId) {
        return reservationRepository.findByAgenceIdOrderByDateReservationDesc(agenceId);
    }

    public List<Reservation> getReservationsByStatut(StatutReservation statut) {
        return reservationRepository.findByStatut(statut);
    }

    @Transactional
    public Reservation confirmerDisponibilite(Long reservationId, Long distributeurId) {
        Reservation reservation = getReservationById(reservationId);

        if (reservation.getStatut() != StatutReservation.PAYEE) {
            throw new RuntimeException("La réservation doit être payée avant confirmation");
        }

        String ancienStatut = reservation.getStatut().name();
        reservation.setStatut(StatutReservation.CONFIRMEE);
        Reservation saved = reservationRepository.save(reservation);
        enregistrerHistorique(saved.getId(), ancienStatut, StatutReservation.CONFIRMEE.name(),
                "Disponibilité confirmée par le distributeur", distributeurId);
        return saved;
    }

    @Transactional
    public Reservation marquerRecuperee(Long reservationId, Long consommateurId) {
        Reservation reservation = getReservationById(reservationId);

        if (reservation.getStatut() != StatutReservation.CONFIRMEE) {
            throw new RuntimeException("La réservation doit être confirmée avant récupération");
        }

        String ancienStatut = reservation.getStatut().name();
        reservation.setStatut(StatutReservation.RECUPEREE);
        reservation.setDateRecuperation(LocalDateTime.now());
        Reservation saved = reservationRepository.save(reservation);
        enregistrerHistorique(saved.getId(), ancienStatut, StatutReservation.RECUPEREE.name(),
                "Gaz récupéré par le client", consommateurId);
        return saved;
    }

    @Transactional
    public Reservation annulerReservation(Long reservationId, String motif, Long effectuePar) {
        Reservation reservation = getReservationById(reservationId);

        if (reservation.getStatut() == StatutReservation.RECUPEREE) {
            throw new RuntimeException("La réservation a déjà été récupérée");
        }
        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            throw new RuntimeException("La réservation est déjà annulée");
        }

        String ancienStatut = reservation.getStatut().name();
        reservation.setStatut(StatutReservation.ANNULEE);
        reservation.setMotifAnnulation(motif);
        Reservation saved = reservationRepository.save(reservation);
        enregistrerHistorique(saved.getId(), ancienStatut, StatutReservation.ANNULEE.name(),
                "Annulation: " + motif, effectuePar);
        return saved;
    }

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void expirerReservations() {
        LocalDateTime now = LocalDateTime.now();
        int nbExpirees = reservationRepository.updateExpiredReservations(now);
        if (nbExpirees > 0) {
            System.out.println(nbExpirees + " réservations expirées automatiquement");
        }
    }

    public List<HistoriqueReservation> getHistoriqueReservation(Long reservationId) {
        return historiqueReservationRepository.findByReservationIdOrderByDateChangementDesc(reservationId);
    }

    public long compterReservations() {
        return reservationRepository.count();
    }

    public long compterReservationsParStatut(StatutReservation statut) {
        return reservationRepository.countByStatut(statut);
    }

    public long compterReservationsParAgence(Long agenceId) {
        return reservationRepository.countByAgenceId(agenceId);
    }

    public long compterReservationsParConsommateur(Long consommateurId) {
        return reservationRepository.countByConsommateurId(consommateurId);
    }
}
