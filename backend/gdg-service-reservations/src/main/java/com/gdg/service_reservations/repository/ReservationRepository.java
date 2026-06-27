package com.gdg.service_reservations.repository;

import com.gdg.service_reservations.model.Reservation;
import com.gdg.service_reservations.model.Reservation.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Trouver une réservation par sa référence
    Optional<Reservation> findByReferenceReservation(String reference);

    // Trouver les réservations d'un consommateur
    List<Reservation> findByConsommateurIdOrderByDateReservationDesc(Long consommateurId);

    // Trouver les réservations d'une agence
    List<Reservation> findByAgenceIdOrderByDateReservationDesc(Long agenceId);

    // Trouver les réservations par statut
    List<Reservation> findByStatut(StatutReservation statut);

    // Trouver les réservations actives (non annulées, non expirées, non récupérées)
    List<Reservation> findByStatutIn(List<StatutReservation> statuts);

    // Trouver les réservations d'un consommateur par statut
    List<Reservation> findByConsommateurIdAndStatut(Long consommateurId, StatutReservation statut);

    // Trouver les réservations expirées (date_expiration < maintenant et statut EN_ATTENTE)
    @Query("SELECT r FROM Reservation r WHERE r.statut = 'EN_ATTENTE' AND r.dateExpiration < :now")
    List<Reservation> findReservationsExpirees(@Param("now") LocalDateTime now);

    // Compter les réservations par statut
    long countByStatut(StatutReservation statut);

    // Compter les réservations d'une agence
    long countByAgenceId(Long agenceId);

    // Compter les réservations d'un consommateur
    long countByConsommateurId(Long consommateurId);

    // Vérifier si un consommateur a déjà une réservation active pour une agence/produit
    @Query("SELECT COUNT(r) > 0 FROM Reservation r " +
           "WHERE r.consommateurId = :consommateurId " +
           "AND r.agenceId = :agenceId " +
           "AND r.categorieProduitId = :categorieProduitId " +
           "AND r.statut IN ('EN_ATTENTE', 'PAYEE', 'CONFIRMEE')")
    boolean hasActiveReservation(@Param("consommateurId") Long consommateurId,
                                 @Param("agenceId") Long agenceId,
                                 @Param("categorieProduitId") Long categorieProduitId);

    // Mettre à jour le statut des réservations expirées
    @Modifying
    @Query("UPDATE Reservation r SET r.statut = 'EXPIREE', r.motifAnnulation = 'Délai de paiement dépassé' " +
           "WHERE r.statut = 'EN_ATTENTE' AND r.dateExpiration < :now")
    int updateExpiredReservations(@Param("now") LocalDateTime now);
}