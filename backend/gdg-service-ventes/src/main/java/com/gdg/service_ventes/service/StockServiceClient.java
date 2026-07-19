package com.gdg.service_ventes.service;

import com.gdg.service_ventes.dto.ApprovisionnementRequest;
import com.gdg.service_ventes.dto.StockUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

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
        log.info(" Mise à jour du stock via le service Stock");

    
            webClient.post()
                    .uri(stockServiceUrl + "/api/stocks/decrementer")
                    .body(Mono.just(request), StockUpdateRequest.class)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            log.info(" Stock mis à jour avec succès");
      
    }
    public void approvisionnerStock(ApprovisionnementRequest request){
        log.info("Approvisionnement du stock via service stock");

        Map<String, Object>body = new HashMap<>();
        body.put("agenceId", request.getAgenceId());
         body.put("categorieProduitId", request.getCategorieProduitId());
         body.put("quantite", request.getQuantite());
          body.put("fournisseur", request.getFournisseur());
           body.put("numeroBonLivraison", request.getNumeroBonLivraison());
            body.put("effectuePar", request.getDistributeurId());
    

            webClient.post()
                       .uri(stockServiceUrl + "/api/stocks/approvisionner")
                       .body(Mono.just(body),Map.class)
                       .retrieve()
                       .bodyToMono(Void.class)
                       .block();
             
        log.info("Approvisionnement effectuer avec succes");               
    
    }
}