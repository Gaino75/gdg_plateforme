package com.gdg.service_ventes.service;

import com.gdg.service_ventes.dto.FactureResponse;
import com.gdg.service_ventes.dto.VenteRequest;
import com.gdg.service_ventes.model.Facture;
import com.gdg.service_ventes.model.Vente;
import com.gdg.service_ventes.repository.FactureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepository factureRepository;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public Facture genererFacture(Vente vente, VenteRequest request) {
        log.info("📄 Génération de la facture pour la vente: {}", vente.getReferenceVente());

        String numeroFacture = genererNumeroFacture();
        double montantTtc = vente.getPrixTotal();

        Facture facture = Facture.builder()
                .numeroFacture(numeroFacture)
                .vente(vente)
                .nomClient(request.getNomClient())
                .telephoneClient(request.getTelephoneClient())
                .montantHt(vente.getPrixTotal())
                .tauxTva(0.0)
                .montantTva(0.0)
                .montantTtc(montantTtc)
                .build();

        Facture saved = factureRepository.save(facture);
        log.info("✅ Facture générée: {}", saved.getNumeroFacture());

        return saved;
    }

    public Facture getFactureById(Long id) {
        return factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée avec l'ID: " + id));
    }

    public Facture getFactureByVenteId(Long venteId) {
        return factureRepository.findByVenteId(venteId)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée pour la vente: " + venteId));
    }

    public List<Facture> getFacturesByAgence(Long agenceId) {
        return factureRepository.findByVenteAgenceId(agenceId);
    }

    public FactureResponse buildFactureResponse(Facture facture) {
        return FactureResponse.builder()
                .id(facture.getId())
                .numeroFacture(facture.getNumeroFacture())
                .venteId(facture.getVente().getId())
                .dateEmission(facture.getDateEmission())
                .urlPdf(facture.getUrlPdf())
                .logoAgence(facture.getLogoAgence())
                .enteteAgence(facture.getEnteteAgence())
                .piedAgence(facture.getPiedAgence())
                .nomClient(facture.getNomClient())
                .telephoneClient(facture.getTelephoneClient())
                .montantHt(facture.getMontantHt())
                .tauxTva(facture.getTauxTva())
                .montantTva(facture.getMontantTva())
                .montantTtc(facture.getMontantTtc())
                .build();
    }

    private String genererNumeroFacture() {
        String datePart = LocalDateTime.now().format(DATE_FORMAT);
        long count = factureRepository.count() + 1;
        String seqPart = String.format("%05d", count);
        return "FAC-" + datePart + "-" + seqPart;
    }
}