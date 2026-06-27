package com.gdg.service_reservations.service;

import com.gdg.service_reservations.model.HistoriqueReservation;
import com.gdg.service_reservations.model.Reservation;
import com.gdg.service_reservations.model.Reservation.ModePaiement;
import com.gdg.service_reservations.model.Reservation.StatutReservation;
import com.gdg.service_reservations.repository.HistoriqueReservationRepository;
import com.gdg.service_reservations.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HistoriqueReservationRepository historiqueReservationRepository;

    private static final DateTimeFormatter REF_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");

    /**
     * Génère une référence unique pour une réservation
     * Format: RES-2025-000001
     */
    private String genererReference() {
        long count = reservationRepository.count() + 1;
        return String.format("RES-%d-%06d", LocalDateTime.now().getYear(), count);
    }

    /**
     * Créer une nouvelle réservation
     */
    @Transactional
    public Reservation creerReservation(Long agenceId, Long consommateurId,
                                        Long categorieProduitId, Integer quantite,
                                        Double prixUnitaire, Double montantTotal,
                                        ModePaiement modePaiement) {

        // Vérifier si le consommateur a déjà une réservation active
        if (reservationRepository.hasActiveReservation(consommateurId, agenceId, categorieProduitId)) {
            throw new RuntimeException("Vous avez déjà une réservation active pour ce produit dans cette agence");
        }

        // Créer la réservation
        String reference = genererReference();
        Reservation reservation = new Reservation(
            reference,
            agenceId,
            consommateurId,
            categorieProduitId,
            quantite,
            prixUnitaire,
            montantTotal,
            modePaiement
        );

        Reservation saved = reservationRepository.save(reservation);

        // Enregistrer l'historique
        HistoriqueReservation historique = new HistoriqueReservation(
            saved.getId(),
            null,
            StatutReservation.EN_ATTENTE.name(),
            "Création de la réservation",
            consommateurId
        );
        historiqueReservationRepository.save(historique);

        log.info("✅ Réservation créée : {} pour le consommateur {}", reference, consommateurId);
        return saved;
    }

    /**
     * Récupérer une réservation par son ID
     */
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'id : " + id));
    }

    /**
     * Récupérer une réservation par sa référence
     */
    public Reservation getReservationByReference(String reference) {
        return reservationRepository.findByReferenceReservation(reference)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec la référence : " + reference));
    }

    /**
     * Récupérer toutes les réservations d'un consommateur
     */
    public List<Reservation> getReservationsByConsommateur(Long consommateurId) {
        return reservationRepository.findByConsommateurIdOrderByDateReservationDesc(consommateurId);
    }

    /**
     * Récupérer les réservations actives d'un consommateur
     */
    public List<Reservation> getReservationsActivesByConsommateur(Long consommateurId) {
        return reservationRepository.findByConsommateurIdAndStatut(consommateurId, StatutReservation.EN_ATTENTE);
    }

    /**
     * Récupérer toutes les réservations d'une agence
     */
    public List<Reservation> getReservationsByAgence(Long agenceId) {
        return reservationRepository.findByAgenceIdOrderByDateReservationDesc(agenceId);
    }

    /**
     * Récupérer les réservations par statut
     */
    public List<Reservation> getReservationsByStatut(StatutReservation statut) {
        return reservationRepository.findByStatut(statut);
    }

    /**
     * Confirmer le paiement d'une réservation
     */
    @Transactional
    public Reservation confirmerPaiement(Long reservationId, String referencePaiement) {
        Reservation reservation = getReservationById(reservationId);

        if (reservation.getStatut() == StatutReservation.EXPIREE) {
            throw new RuntimeException("Cette réservation a expiré");
        }
        if (reservation.getStatut() == StatutReservation.ANNULEE) {
            throw new RuntimeException("Cette réservation a été annulée");
        }
        if (reservation.getStatut() == StatutReservation.PAYEE || 
            reservation.getStatut() == StatutReservation.CONFIRMEE) {
            throw new RuntimeException("Cette réservation a déjà été payée");
        }

        String ancienStatut = reservation.getStatut().name();
        reservation.setStatut(StatutReservation.PAYEE);
        reservation.setReferencePaiement(referencePaiement);
        reservation.setDateConfirmation(LocalDateTime.now());

        Reservation saved = reservationRepository.save(reservation);

        // Enregistrer l'historique
        HistoriqueReservation historique = new HistoriqueReservation(
            saved.getId(),
            ancienStatut,
            StatutReservation.PAYEE.name(),
            "Paiement confirmé - Réf: " + referencePaiement,
            reservation.getConsommateurId()
        );
        historiqueReservationRepository.save(historique);

        log.info("✅ Paiement confirmé pour la réservation {}", reservation.getReferenceReservation());
        return saved;
    }

    /**
     * Confirmer la disponibilité (par le distributeur)
     */
    @Transactional
    public Reservation confirmerDisponibilite(Long reservationId, Long distributeurId) {
        Reservation reservation = getReservationById(reservationId);

        if (reservation.getStatut() != StatutReservation.PAYEE) {
            throw new RuntimeException("La réservation doit être payée avant confirmation");
        }

        String ancienStatut = reservation.getStatut().name();
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation saved = reservationRepository.save(reservation);

        HistoriqueReservation historique = new HistoriqueReservation(
            saved.getId(),
            ancienStatut,
            StatutReservation.CONFIRMEE.name(),
            "Disponibilité confirmée par le distributeur",
            distributeurId
        );
        historiqueReservationRepository.save(historique);

        log.info("✅ Disponibilité confirmée pour la réservation {}", reservation.getReferenceReservation());
        return saved;
    }

    /**
     * Marquer une réservation comme récupérée (par le client)
     */
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

        HistoriqueReservation historique = new HistoriqueReservation(
            saved.getId(),
            ancienStatut,
            StatutReservation.RECUPEREE.name(),
            "Gaz récupéré par le client",
            consommateurId
        );
        historiqueReservationRepository.save(historique);

        log.info("✅ Gaz récupéré pour la réservation {}", reservation.getReferenceReservation());
        return saved;
    }

    /**
     * Annuler une réservation (par le client ou le système)
     */
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

        HistoriqueReservation historique = new HistoriqueReservation(
            saved.getId(),
            ancienStatut,
            StatutReservation.ANNULEE.name(),
            "Annulation: " + motif,
            effectuePar
        );
        historiqueReservationRepository.save(historique);

        log.info("❌ Réservation annulée : {}", reservation.getReferenceReservation());
        return saved;
    }

    /**
     * Tâche programmée : Expirer les réservations en attente
     * S'exécute toutes les 5 minutes
     */
    @Scheduled(fixedDelay = 300000) // 5 minutes
    @Transactional
    public void expirerReservations() {
        LocalDateTime now = LocalDateTime.now();
        int nbExpirees = reservationRepository.updateExpiredReservations(now);

        if (nbExpirees > 0) {
            log.info("⏰ {} réservations expirées automatiquement", nbExpirees);
        }
    }

    /**
     * Récupérer l'historique d'une réservation
     */
    public List<HistoriqueReservation> getHistoriqueReservation(Long reservationId) {
        return historiqueReservationRepository.findByReservationIdOrderByDateChangementDesc(reservationId);
    }

    /**
     * Statistiques
     */
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
