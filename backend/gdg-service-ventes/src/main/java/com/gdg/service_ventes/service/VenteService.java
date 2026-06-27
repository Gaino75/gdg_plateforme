package com.gdg.service_ventes.service;

import com.gdg.service_ventes.dto.StockUpdateRequest;
import com.gdg.service_ventes.dto.VenteRequest;
import com.gdg.service_ventes.dto.VenteResponse;
import com.gdg.service_ventes.model.Facture;
import com.gdg.service_ventes.model.Vente;
import com.gdg.service_ventes.repository.VenteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VenteService {

    private final VenteRepository venteRepository;
    private final FactureService factureService;
    private final StockServiceClient stockServiceClient;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Transactional
    public VenteResponse enregistrerVente(VenteRequest request) {
        log.info("📝 Enregistrement d'une nouvelle vente");

        String reference = genererReferenceVente();
        Double prixTotal = request.getQuantite() * request.getPrixUnitaire();

        Vente vente = Vente.builder()
                .referenceVente(reference)
                .agenceId(request.getAgenceId())
                .distributeurId(request.getDistributeurId())
                .consommateurId(request.getConsommateurId())
                .categorieProduitId(request.getCategorieProduitId())
                .quantite(request.getQuantite())
                .prixUnitaire(request.getPrixUnitaire())
                .prixTotal(prixTotal)
                .modePaiement(request.getModePaiement())
                .typeVente(request.getTypeVente() != null ? request.getTypeVente() : Vente.TypeVente.HORS_LIGNE)
                .statut(Vente.StatutVente.CONFIRMEE)
                .referencePaiement(request.getReferencePaiement())
                .observations(request.getObservations())
                .build();

        Vente saved = venteRepository.save(vente);
        log.info("✅ Vente enregistrée avec ID: {}, Réf: {}", saved.getId(), saved.getReferenceVente());

        // Mettre à jour le stock
        try {
            StockUpdateRequest stockUpdate = StockUpdateRequest.builder()
                    .agenceId(request.getAgenceId())
                    .categorieProduitId(request.getCategorieProduitId())
                    .quantite(request.getQuantite())
                    .typeMouvement("SORTIE")
                    .referenceExterne(reference)
                    .effectuePar(request.getDistributeurId())
                    .build();

            stockServiceClient.updateStock(stockUpdate);
            log.info("📦 Stock mis à jour pour la vente {}", reference);
        } catch (Exception e) {
            log.error("❌ Erreur lors de la mise à jour du stock: {}", e.getMessage());
        }

        // Générer la facture
        Facture facture = factureService.genererFacture(saved, request);
        log.info("📄 Facture générée: {}", facture.getNumeroFacture());

        return buildResponse(saved, facture);
    }

    public List<Vente> getVentesByAgence(Long agenceId) {
        return venteRepository.findByAgenceIdOrderByDateVenteDesc(agenceId);
    }

    public Page<Vente> getVentesByAgencePaginated(Long agenceId, Pageable pageable) {
        return venteRepository.findByAgenceId(agenceId, pageable);
    }

    public List<Vente> getVentesByDistributeur(Long distributeurId) {
        return venteRepository.findByDistributeurIdOrderByDateVenteDesc(distributeurId);
    }

    public Vente getVenteById(Long id) {
        return venteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée avec l'ID: " + id));
    }

    public Vente getVenteByReference(String reference) {
        return venteRepository.findByReferenceVente(reference)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée avec la référence: " + reference));
    }

    public Double getChiffreAffaire(Long agenceId) {
        Double ca = venteRepository.sumChiffreAffaireParAgence(agenceId);
        return ca != null ? ca : 0.0;
    }

    public Integer getQuantiteVendueParCategorie(Long agenceId, Long categorieProduitId) {
        Integer quantite = venteRepository.sumQuantiteVendueParCategorie(agenceId, categorieProduitId);
        return quantite != null ? quantite : 0;
    }

    public long countVentesByAgence(Long agenceId) {
        return venteRepository.countByAgenceIdAndStatut(agenceId, Vente.StatutVente.CONFIRMEE);
    }

    private String genererReferenceVente() {
        String datePart = LocalDateTime.now().format(DATE_FORMAT);
        long count = venteRepository.count() + 1;
        String seqPart = String.format("%05d", count);
        return "VTE-" + datePart + "-" + seqPart;
    }

    private VenteResponse buildResponse(Vente vente, Facture facture) {
        return VenteResponse.builder()
                .id(vente.getId())
                .referenceVente(vente.getReferenceVente())
                .agenceId(vente.getAgenceId())
                .distributeurId(vente.getDistributeurId())
                .consommateurId(vente.getConsommateurId())
                .categorieProduitId(vente.getCategorieProduitId())
                .quantite(vente.getQuantite())
                .prixUnitaire(vente.getPrixUnitaire())
                .prixTotal(vente.getPrixTotal())
                .modePaiement(vente.getModePaiement())
                .typeVente(vente.getTypeVente())
                .statut(vente.getStatut())
                .referencePaiement(vente.getReferencePaiement())
                .dateVente(vente.getDateVente())
                .observations(vente.getObservations())
                .facture(factureService.buildFactureResponse(facture))
                .build();
    }
}
