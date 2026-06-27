package com.gdg.service_ventes.service;

import com.gdg.service_ventes.dto.StockUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceClient {

    private final WebClient webClient;

    @Value("${services.stock.url}")
    private String stockServiceUrl;

    public void updateStock(StockUpdateRequest request) {
        log.info("📦 Mise à jour du stock via le service Stock");

        try {
            webClient.post()
                    .uri(stockServiceUrl + "/api/stocks/mouvement")
                    .body(Mono.just(request), StockUpdateRequest.class)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            log.info("✅ Stock mis à jour avec succès");
        } catch (Exception e) {
            log.error("❌ Erreur lors de la mise à jour du stock: {}", e.getMessage());
        }
    }
}