package com.gdg.service_stock.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorie_produit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorieProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String libelle;

    @Column(nullable = false)
    private Double poids;

    @Column(nullable = false)
    private Double prixUnitaire;

    @Column(nullable = false)
    @Builder.Default
    private Boolean actif = true;
}
