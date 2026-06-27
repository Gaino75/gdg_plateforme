package com.gdg.service_reservations.controller;

import com.gdg.service_reservations.model.HistoriqueReservation;
import com.gdg.service_reservations.model.Reservation;
import com.gdg.service_reservations.model.Reservation.ModePaiement;
import com.gdg.service_reservations.model.Reservation.StatutReservation;
import com.gdg.service_reservations.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // ============================================================
    // CRÉATION
    // ============================================================

    /**
     * POST /api/reservations
     * Créer une nouvelle réservation
     */
    @PostMapping
    public ResponseEntity<Reservation> creerReservation(@Valid @RequestBody CreerReservationRequest request) {
        Reservation reservation = reservationService.creerReservation(
            request.getAgenceId(),
            request.getConsommateurId(),
            request.getCategorieProduitId(),
            request.getQuantite(),
            request.getPrixUnitaire(),
            request.getMontantTotal(),
            request.getModePaiement()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    // ============================================================
    // CONSULTATION
    // ============================================================

    /**
     * GET /api/reservations/{id}
     * Récupérer une réservation par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    /**
     * GET /api/reservations/reference/{reference}
     * Récupérer une réservation par sa référence
     */
    @GetMapping("/reference/{reference}")
    public ResponseEntity<Reservation> getReservationByReference(@PathVariable String reference) {
        return ResponseEntity.ok(reservationService.getReservationByReference(reference));
    }

    /**
     * GET /api/reservations/consommateur/{consommateurId}
     * Récupérer toutes les réservations d'un consommateur
     */
    @GetMapping("/consommateur/{consommateurId}")
    public ResponseEntity<List<Reservation>> getReservationsByConsommateur(@PathVariable Long consommateurId) {
        return ResponseEntity.ok(reservationService.getReservationsByConsommateur(consommateurId));
    }

    /**
     * GET /api/reservations/consommateur/{consommateurId}/actives
     * Récupérer les réservations actives d'un consommateur
     */
    @GetMapping("/consommateur/{consommateurId}/actives")
    public ResponseEntity<List<Reservation>> getReservationsActivesByConsommateur(@PathVariable Long consommateurId) {
        return ResponseEntity.ok(reservationService.getReservationsActivesByConsommateur(consommateurId));
    }

    /**
     * GET /api/reservations/agence/{agenceId}
     * Récupérer toutes les réservations d'une agence
     */
    @GetMapping("/agence/{agenceId}")
    public ResponseEntity<List<Reservation>> getReservationsByAgence(@PathVariable Long agenceId) {
        return ResponseEntity.ok(reservationService.getReservationsByAgence(agenceId));
    }

    /**
     * GET /api/reservations/statut/{statut}
     * Récupérer les réservations par statut
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Reservation>> getReservationsByStatut(@PathVariable StatutReservation statut) {
        return ResponseEntity.ok(reservationService.getReservationsByStatut(statut));
    }

    /**
     * GET /api/reservations/{id}/historique
     * Récupérer l'historique d'une réservation
     */
    @GetMapping("/{id}/historique")
    public ResponseEntity<List<HistoriqueReservation>> getHistoriqueReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getHistoriqueReservation(id));
    }

    // ============================================================
    // GESTION DES STATUTS
    // ============================================================

    /**
     * PUT /api/reservations/{id}/paiement
     * Confirmer le paiement d'une réservation
     */
    @PutMapping("/{id}/paiement")
    public ResponseEntity<Reservation> confirmerPaiement(
            @PathVariable Long id,
            @RequestParam String referencePaiement) {
        return ResponseEntity.ok(reservationService.confirmerPaiement(id, referencePaiement));
    }

    /**
     * PUT /api/reservations/{id}/confirmer-disponibilite
     * Confirmer la disponibilité (par le distributeur)
     */
    @PutMapping("/{id}/confirmer-disponibilite")
    public ResponseEntity<Reservation> confirmerDisponibilite(
            @PathVariable Long id,
            @RequestParam Long distributeurId) {
        return ResponseEntity.ok(reservationService.confirmerDisponibilite(id, distributeurId));
    }

    /**
     * PUT /api/reservations/{id}/recuperer
     * Marquer une réservation comme récupérée
     */
    @PutMapping("/{id}/recuperer")
    public ResponseEntity<Reservation> marquerRecuperee(
            @PathVariable Long id,
            @RequestParam Long consommateurId) {
        return ResponseEntity.ok(reservationService.marquerRecuperee(id, consommateurId));
    }

    /**
     * PUT /api/reservations/{id}/annuler
     * Annuler une réservation
     */
    @PutMapping("/{id}/annuler")
    public ResponseEntity<Reservation> annulerReservation(
            @PathVariable Long id,
            @RequestParam String motif,
            @RequestParam Long effectuePar) {
        return ResponseEntity.ok(reservationService.annulerReservation(id, motif, effectuePar));
    }

    // ============================================================
    // STATISTIQUES
    // ============================================================

    /**
     * GET /api/reservations/statistiques
     * Statistiques globales des réservations
     */
    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Long>> getStatistiques() {
        return ResponseEntity.ok(Map.of(
            "total", reservationService.compterReservations(),
            "enAttente", reservationService.compterReservationsParStatut(StatutReservation.EN_ATTENTE),
            "payees", reservationService.compterReservationsParStatut(StatutReservation.PAYEE),
            "confirmees", reservationService.compterReservationsParStatut(StatutReservation.CONFIRMEE),
            "annulees", reservationService.compterReservationsParStatut(StatutReservation.ANNULEE),
            "expirees", reservationService.compterReservationsParStatut(StatutReservation.EXPIREE),
            "recuperees", reservationService.compterReservationsParStatut(StatutReservation.RECUPEREE)
        ));
    }

    /**
     * GET /api/reservations/agence/{agenceId}/statistiques
     * Statistiques des réservations d'une agence
     */
    @GetMapping("/agence/{agenceId}/statistiques")
    public ResponseEntity<Map<String, Long>> getStatistiquesParAgence(@PathVariable Long agenceId) {
        return ResponseEntity.ok(Map.of(
            "total", reservationService.compterReservationsParAgence(agenceId)
        ));
    }

    // ============================================================
    // DTO DE CRÉATION
    // ============================================================

    public static class CreerReservationRequest {
        @NotNull(message = "L'ID de l'agence est obligatoire")
        private Long agenceId;

        @NotNull(message = "L'ID du consommateur est obligatoire")
        private Long consommateurId;

        @NotNull(message = "L'ID de la catégorie produit est obligatoire")
        private Long categorieProduitId;

        @NotNull(message = "La quantité est obligatoire")
        @Min(value = 1, message = "La quantité doit être supérieure à 0")
        private Integer quantite;

        @NotNull(message = "Le prix unitaire est obligatoire")
        @Min(value = 0, message = "Le prix unitaire doit être positif")
        private Double prixUnitaire;

        @NotNull(message = "Le montant total est obligatoire")
        @Min(value = 0, message = "Le montant total doit être positif")
        private Double montantTotal;

        @NotNull(message = "Le mode de paiement est obligatoire")
        private ModePaiement modePaiement;

        // Getters et Setters
        public Long getAgenceId() { return agenceId; }
        public void setAgenceId(Long agenceId) { this.agenceId = agenceId; }
        public Long getConsommateurId() { return consommateurId; }
        public void setConsommateurId(Long consommateurId) { this.consommateurId = consommateurId; }
        public Long getCategorieProduitId() { return categorieProduitId; }
        public void setCategorieProduitId(Long categorieProduitId) { this.categorieProduitId = categorieProduitId; }
        public Integer getQuantite() { return quantite; }
        public void setQuantite(Integer quantite) { this.quantite = quantite; }
        public Double getPrixUnitaire() { return prixUnitaire; }
        public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
        public Double getMontantTotal() { return montantTotal; }
        public void setMontantTotal(Double montantTotal) { this.montantTotal = montantTotal; }
        public ModePaiement getModePaiement() { return modePaiement; }
        public void setModePaiement(ModePaiement modePaiement) { this.modePaiement = modePaiement; }
    }
}