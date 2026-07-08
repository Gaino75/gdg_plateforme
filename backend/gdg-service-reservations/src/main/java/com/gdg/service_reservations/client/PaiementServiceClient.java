package com.gdg.service_reservations.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaiementServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.paiement.url:http://localhost:8086}")
    private String paiementServiceUrl;

    @SuppressWarnings("unchecked")
    public Map<String, Object> initierPaiement(Long reservationId, Long consommateurId, Long agenceId,
                                               Double montant, String modePaiement, String numeroTelephone) {
        Map<String, Object> body = Map.of(
                "reservationId", reservationId,
                "consommateurId", consommateurId,
                "agenceId", agenceId,
                "montant", montant,
                "modePaiement", modePaiement,
                "numeroTelephone", numeroTelephone != null ? numeroTelephone : ""
        );

        return webClientBuilder.build()
                .post()
                .uri(paiementServiceUrl + "/api/paiements/initier")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
