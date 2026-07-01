package com.gazstation1.payement_service1.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PutExchange;

@FeignClient(name = "commande-service")
public interface CommandeClient {
    @GetMapping("/api/commandes/{id}/existe")
    Boolean commandeExiste(@PathVariable("id") Long id);

    @PutMapping("/api/commandes/{id}/statut")
    void mettreAJourStatutCommande(@PathVariable("id") Long id,
                                   @RequestParam("statut") String statut);
}
