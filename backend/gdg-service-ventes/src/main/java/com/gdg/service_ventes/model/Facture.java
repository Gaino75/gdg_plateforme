package com.gdg.service_ventes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "facture", schema = "ventes_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_facture", nullable = false, unique = true)
    private String numeroFacture;

    @OneToOne
    @JoinColumn(name = "vente_id", nullable = false, unique = true)
    private Vente vente;

    @CreationTimestamp
    @Column(name = "date_emission", nullable = false)
    private LocalDateTime dateEmission;

    @Column(name = "url_pdf")
    private String urlPdf;

    @Column(name = "logo_agence")
    private String logoAgence;

    @Column(name = "entete_agence")
    private String enteteAgence;

    @Column(name = "pied_agence")
    private String piedAgence;

    @Column(name = "nom_client")
    private String nomClient;

    @Column(name = "telephone_client")
    private String telephoneClient;

    @Column(name = "montant_ht")
    private Double montantHt;

    @Column(name = "taux_tva")
    private Double tauxTva;

    @Column(name = "montant_tva")
    private Double montantTva;

    @Column(name = "montant_ttc", nullable = false)
    private Double montantTtc;
}
