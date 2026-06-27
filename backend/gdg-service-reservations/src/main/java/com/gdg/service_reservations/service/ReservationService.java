package com.gdg.service_reservations.service;

import com.gdg.service_reservations.model.HistoriqueReservation;
import com.gdg.service_reservations.model.Reservation;
import com.gdg.service_reservations.model.Reservation.ModePaiement;
import com.gdg.service_reservations.model.Reservation.StatutReservation;
import com.gdg.service_reservations.repository.HistoriqueReservationRepository;
import com.gdg.service_reservations.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HistoriqueReservationRepository historiqueReservationRepository;

    private static final DateTimeFormatter REF_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");

    // ============================================================
    // CONSTRUCTEUR (remplace @RequiredArgsConstructor)
    // ============================================================

    public ReservationService(ReservationRepository reservationRepository,
                              HistoriqueReservationRepository historiqueReservationRepository) {
        this.reservationRepository = reservationRepository;
        this.historiqueReservationRepository = historiqueReservationRepository;
    }

    // ============================================================
    // MÉTHODES
    // ============================================================

    private String genererReference() {
        long count = reservationRepository.count() + 1;
        return String.format("RES-%d-%06d", LocalDateTime.now().getYear(), count);
    }


    @Transactional
    public Reservation creerReservation(Long agenceId, Long consommateurId,
                                    Long categorieProduitId, Integer quantite,
                                    ModePaiement modePaiement) {

    if (reservationRepository.hasActiveReservation(consommateurId, agenceId, categorieProduitId)) {
        throw new RuntimeException("Vous avez déjà une réservation active pour ce produit dans cette agence");
    }

    // ⚠️ ICI tu dois récupérer le prix unitaire depuis le service Stock
    // Pour l'instant, on utilise une valeur par défaut
    // Plus tard, tu feras un appel HTTP au service Stock
    Double prixUnitaire = 2500.0;  // ← À remplacer par appel au service Stock
    Double montantTotal = prixUnitaire * quantite;

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

    HistoriqueReservation historique = new HistoriqueReservation(
        saved.getId(),
        null,
        StatutReservation.EN_ATTENTE.name(),
        "Création de la réservation",
        consommateurId
    );
    historiqueReservationRepository.save(historique);

    System.out.println("✅ Réservation créée : " + reference + " pour le consommateur " + consommateurId);
    return saved;
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

        HistoriqueReservation historique = new HistoriqueReservation(
            saved.getId(),
            ancienStatut,
            StatutReservation.PAYEE.name(),
            "Paiement confirmé - Réf: " + referencePaiement,
            reservation.getConsommateurId()
        );
        historiqueReservationRepository.save(historique);

        System.out.println("✅ Paiement confirmé pour la réservation " + reservation.getReferenceReservation());
        return saved;
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

        HistoriqueReservation historique = new HistoriqueReservation(
            saved.getId(),
            ancienStatut,
            StatutReservation.CONFIRMEE.name(),
            "Disponibilité confirmée par le distributeur",
            distributeurId
        );
        historiqueReservationRepository.save(historique);

        System.out.println("✅ Disponibilité confirmée pour la réservation " + reservation.getReferenceReservation());
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

        HistoriqueReservation historique = new HistoriqueReservation(
            saved.getId(),
            ancienStatut,
            StatutReservation.RECUPEREE.name(),
            "Gaz récupéré par le client",
            consommateurId
        );
        historiqueReservationRepository.save(historique);

        System.out.println("✅ Gaz récupéré pour la réservation " + reservation.getReferenceReservation());
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

        HistoriqueReservation historique = new HistoriqueReservation(
            saved.getId(),
            ancienStatut,
            StatutReservation.ANNULEE.name(),
            "Annulation: " + motif,
            effectuePar
        );
        historiqueReservationRepository.save(historique);

        System.out.println("❌ Réservation annulée : " + reservation.getReferenceReservation());
        return saved;
    }

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void expirerReservations() {
        LocalDateTime now = LocalDateTime.now();
        int nbExpirees = reservationRepository.updateExpiredReservations(now);

        if (nbExpirees > 0) {
            System.out.println("⏰ " + nbExpirees + " réservations expirées automatiquement");
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