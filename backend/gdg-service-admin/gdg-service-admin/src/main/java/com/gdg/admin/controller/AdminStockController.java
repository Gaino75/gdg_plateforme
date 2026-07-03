package com.gdg.admin.controller;

import com.gdg.admin.dto.StockDTO;
import com.gdg.admin.service.AdminStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/stocks")
@CrossOrigin(origins = "*")
public class AdminStockController {

    @Autowired
    private AdminStockService stockService;

    // Stock global de toutes les agences
    @GetMapping("/global")
    public ResponseEntity<List<StockDTO>> getGlobal() {
        return ResponseEntity.ok(stockService.getStockGlobal());
    }

    // Stock d'une agence spécifique
    @GetMapping("/agence/{agenceId}")
    public ResponseEntity<List<StockDTO>> getByAgence(
            @PathVariable Long agenceId) {
        return ResponseEntity.ok(
            stockService.getStockParAgence(agenceId));
    }
}
