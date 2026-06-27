package com.gdg.service_reservations.repository;

import com.gdg.service_reservations.model.HistoriqueReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueReservationRepository extends JpaRepository<HistoriqueReservation, Long> {

    // Trouver l'historique d'une réservation
    List<HistoriqueReservation> findByReservationIdOrderByDateChangementDesc(Long reservationId);

    // Trouver l'historique fait par un utilisateur
    List<HistoriqueReservation> findByEffectueParOrderByDateChangementDesc(Long effectuePar);
}