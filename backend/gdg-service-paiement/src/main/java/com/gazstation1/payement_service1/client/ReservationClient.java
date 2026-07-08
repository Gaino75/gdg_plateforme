package com.gazstation1.payement_service1.client;

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
