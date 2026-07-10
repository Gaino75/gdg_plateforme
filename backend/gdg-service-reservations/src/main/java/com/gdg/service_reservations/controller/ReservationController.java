package com.gdg.service_reservations.controller;

import com.gdg.service_reservations.dto.*;
import com.gdg.service_reservations.model.HistoriqueReservation;
import com.gdg.service_reservations.model.Reservation;
import com.gdg.service_reservations.model.Reservation.StatutReservation;
import com.gdg.service_reservations.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    // ============================================================
    // CONSTRUCTEUR (remplace @RequiredArgsConstructor)
    // ============================================================

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ============================================================
    // CRÉATION
    // ============================================================

    @PostMapping
    public ResponseEntity<ReservationResponse> creerReservation(@Valid @RequestBody ReservationRequest request) {
    Reservation reservation = reservationService.creerReservation(
        request.getAgenceId(),
        request.getConsommateurId(),
        request.getCategorieProduitId(),
        request.getQuantite(),
        request.getModePaiement(),
        request.getNumeroTelephone()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(reservation));
    }

    

    // ============================================================
    // CONSULTATION
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(mapToResponse(reservation));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<ReservationResponse> getReservationByReference(@PathVariable String reference) {
        Reservation reservation = reservationService.getReservationByReference(reference);
        return ResponseEntity.ok(mapToResponse(reservation));
    }

    @GetMapping("/consommateur/{consommateurId}")
    public ResponseEntity<List<ReservationSummary>> getReservationsByConsommateur(@PathVariable Long consommateurId) {
        List<Reservation> reservations = reservationService.getReservationsByConsommateur(consommateurId);
        List<ReservationSummary> summaries = reservations.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/consommateur/{consommateurId}/actives")
    public ResponseEntity<List<ReservationSummary>> getReservationsActivesByConsommateur(@PathVariable Long consommateurId) {
        List<Reservation> reservations = reservationService.getReservationsActivesByConsommateur(consommateurId);
        List<ReservationSummary> summaries = reservations.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/agence/{agenceId}")
    public ResponseEntity<List<ReservationSummary>> getReservationsByAgence(@PathVariable Long agenceId) {
        List<Reservation> reservations = reservationService.getReservationsByAgence(agenceId);
        List<ReservationSummary> summaries = reservations.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<ReservationSummary>> getReservationsByStatut(@PathVariable StatutReservation statut) {
        List<Reservation> reservations = reservationService.getReservationsByStatut(statut);
        List<ReservationSummary> summaries = reservations.stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/{id}/historique")
    public ResponseEntity<List<HistoriqueReservation>> getHistoriqueReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getHistoriqueReservation(id));
    }

    // ============================================================
    // GESTION DES STATUTS
    // ============================================================

    @PutMapping("/{id}/paiement")
    public ResponseEntity<ReservationResponse> confirmerPaiement(
            @PathVariable Long id,
            @RequestParam String referencePaiement) {
        Reservation reservation = reservationService.confirmerPaiement(id, referencePaiement);
        return ResponseEntity.ok(mapToResponse(reservation));
    }

    @PutMapping("/{id}/confirmer-disponibilite")
    public ResponseEntity<ReservationResponse> confirmerDisponibilite(
            @PathVariable Long id,
            @RequestParam Long distributeurId) {
        Reservation reservation = reservationService.confirmerDisponibilite(id, distributeurId);
        return ResponseEntity.ok(mapToResponse(reservation));
    }

    @PutMapping("/{id}/recuperer")
    public ResponseEntity<ReservationResponse> marquerRecuperee(
            @PathVariable Long id,
            @RequestParam Long consommateurId) {
        Reservation reservation = reservationService.marquerRecuperee(id, consommateurId);
        return ResponseEntity.ok(mapToResponse(reservation));
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<ReservationResponse> annulerReservation(
            @PathVariable Long id,
            @RequestParam String motif,
            @RequestParam Long effectuePar) {
        Reservation reservation = reservationService.annulerReservation(id, motif, effectuePar);
        return ResponseEntity.ok(mapToResponse(reservation));
    }

    // ============================================================
    // STATISTIQUES
    // ============================================================

    @GetMapping("/statistiques")
    public ResponseEntity<ReservationStatistiquesResponse> getStatistiques() {
        ReservationStatistiquesResponse stats = new ReservationStatistiquesResponse(
            reservationService.compterReservations(),
            reservationService.compterReservationsParStatut(StatutReservation.EN_ATTENTE),
            reservationService.compterReservationsParStatut(StatutReservation.PAYEE),
            reservationService.compterReservationsParStatut(StatutReservation.CONFIRMEE),
            reservationService.compterReservationsParStatut(StatutReservation.ANNULEE),
            reservationService.compterReservationsParStatut(StatutReservation.EXPIREE),
            reservationService.compterReservationsParStatut(StatutReservation.RECUPEREE)
        );
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/agence/{agenceId}/statistiques")
    public ResponseEntity<Long> getStatistiquesParAgence(@PathVariable Long agenceId) {
        return ResponseEntity.ok(reservationService.compterReservationsParAgence(agenceId));
    }

    // ============================================================
    // MÉTHODES DE MAPPING
    // ============================================================

    private ReservationResponse mapToResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .referenceReservation(reservation.getReferenceReservation())
                .agenceId(reservation.getAgenceId())
                .consommateurId(reservation.getConsommateurId())
                .categorieProduitId(reservation.getCategorieProduitId())
                .quantite(reservation.getQuantite())
                .prixUnitaire(reservation.getPrixUnitaire())
                .montantTotal(reservation.getMontantTotal())
                .statut(reservation.getStatut())
                .modePaiement(reservation.getModePaiement())
                .referencePaiement(reservation.getReferencePaiement())
                .dateReservation(reservation.getDateReservation())
                .dateExpiration(reservation.getDateExpiration())
                .dateConfirmation(reservation.getDateConfirmation())
                .dateRecuperation(reservation.getDateRecuperation())
                .motifAnnulation(reservation.getMotifAnnulation())
                .observations(reservation.getObservations())
                .build();
    }

    private ReservationSummary mapToSummary(Reservation reservation) {
        return new ReservationSummary(
            reservation.getId(),
            reservation.getReferenceReservation(),
            reservation.getAgenceId(),
            reservation.getCategorieProduitId(),
            reservation.getQuantite(),
            reservation.getMontantTotal(),
            reservation.getStatut(),
            reservation.getModePaiement(),
            reservation.getDateReservation(),
            reservation.getDateExpiration()
        );
    }
}