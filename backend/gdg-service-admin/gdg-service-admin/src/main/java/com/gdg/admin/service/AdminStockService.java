package com.gdg.admin.service;

import com.gdg.admin.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class AdminStockService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String STOCK_URL =
        "http://localhost:8083";

    // Voir stock global de toutes les agences
    public List<StockDTO> getStockGlobal() {
        StockDTO[] stocks = restTemplate.getForObject(
            STOCK_URL + "/admin/stocks/global",
            StockDTO[].class);
        return stocks != null ?
            Arrays.asList(stocks) : List.of();
    }

    // Voir stock d'une agence spécifique
    public List<StockDTO> getStockParAgence(Long agenceId) {
        StockDTO[] stocks = restTemplate.getForObject(
            STOCK_URL + "/stock/agence/" + agenceId,
            StockDTO[].class);
        return stocks != null ?
            Arrays.asList(stocks) : List.of();
    }
}
