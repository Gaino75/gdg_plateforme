package com.gdg.service_stock.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdg.service_stock.entity.CategorieProduit;
import com.gdg.service_stock.repository.CategorieProduitRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieProduitController {

    private final CategorieProduitRepository categorieRepo;

    @GetMapping
    public ResponseEntity<List<CategorieProduit>> listerCategories() {
        return ResponseEntity.ok(categorieRepo.findByActifTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieProduit> getCategorie(@PathVariable Long id) {
        return categorieRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
