package com.gdg.service_paiement.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.reservations.url:http://localhost:8085}")
    private String reservationsUrl;

    public void confirmerPaiement(Long reservationId, String referencePaiement) {
        try {
            webClientBuilder.build()
                    .put()
                    .uri(reservationsUrl + "/api/reservations/{id}/paiement?referencePaiement={ref}",
                            reservationId, referencePaiement)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("✅ Paiement confirmé pour la réservation {}", reservationId);
        } catch (Exception e) {
            log.error("❌ Erreur lors de la confirmation du paiement pour la réservation {}: {}", reservationId, e.getMessage());
            throw new RuntimeException("Erreur lors de la confirmation du paiement", e);
        }
    }

    public void annulerReservation(Long reservationId, String motif, Long effectuePar) {
        try {
            webClientBuilder.build()
                    .put()
                    .uri(reservationsUrl + "/api/reservations/{id}/annuler?motif={motif}&effectuePar={par}",
                            reservationId, motif, effectuePar)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            log.info("✅ Réservation {} annulée", reservationId);
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'annulation de la réservation {}: {}", reservationId, e.getMessage());
        }
    }
}

/*package com.gdg.service_paiement.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ReservationClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.reservations.url:http://localhost:8085}")
    private String reservationsUrl;

    public void confirmerPaiement(Long reservationId, String referencePaiement) {
        webClientBuilder.build()
                .put()
                .uri(reservationsUrl + "/api/reservations/{id}/paiement?referencePaiement={ref}",
                        reservationId, referencePaiement)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void annulerReservation(Long reservationId, String motif, Long effectuePar) {
        webClientBuilder.build()
                .put()
                .uri(reservationsUrl + "/api/reservations/{id}/annuler?motif={motif}&effectuePar={par}",
                        reservationId, motif, effectuePar)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
*/