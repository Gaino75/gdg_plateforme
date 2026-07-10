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
public class StockServiceClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.stock.url:http://localhost:8083}")
    private String stockServiceUrl;

    public Map<String, Object> getStockPrecis(Long agenceId, Long categorieId) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(stockServiceUrl + "/api/stocks/agence/{agenceId}/categorie/{categorieId}",
                            agenceId, categorieId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Stock introuvable pour agence=" + agenceId
                    + " categorie=" + categorieId + " — vérifiez que le stock existe (scripts/init-stock.sql)");
        }
    }

    public void decrementerStock(Long agenceId, Long categorieProduitId, Integer quantite,
                               String referenceExterne, Long effectuePar) {
        Map<String, Object> body = Map.of(
                "agenceId", agenceId,
                "categorieProduitId", categorieProduitId,
                "quantite", quantite,
                "referenceExterne", referenceExterne != null ? referenceExterne : "",
                "effectuePar", effectuePar != null ? effectuePar : 0L
        );

        webClientBuilder.build()
                .post()
                .uri(stockServiceUrl + "/api/stocks/decrementer")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public void verifierDisponibilite(Long agenceId, Long categorieId, Integer quantiteDemandee) {
        Map<String, Object> stock = getStockPrecis(agenceId, categorieId);
        if (stock == null) {
            throw new RuntimeException("Stock introuvable pour cette agence et catégorie");
        }
        Number disponible = (Number) stock.get("quantiteDisponible");
        if (disponible == null || disponible.intValue() < quantiteDemandee) {
            throw new RuntimeException("Stock insuffisant : disponible="
                    + (disponible != null ? disponible.intValue() : 0)
                    + ", demandé=" + quantiteDemandee);
        }
    }

    public Double getPrixUnitaire(Long agenceId, Long categorieId) {
        Map<String, Object> stock = getStockPrecis(agenceId, categorieId);
        if (stock == null || stock.get("prixUnitaire") == null) {
            throw new RuntimeException("Impossible de récupérer le prix unitaire");
        }
        return ((Number) stock.get("prixUnitaire")).doubleValue();
    }
}
