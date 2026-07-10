package com.gdg.service_stock.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdg.service_stock.config.RabbitMQConfig;
import com.gdg.service_stock.dtos.DtoMouvement.MouvementResultDTO;
import com.gdg.service_stock.dtos.DistributeurInfoDTO;
import com.gdg.service_stock.dtos.DtoMouvement.MouvementStockDTO;
import com.gdg.service_stock.dtos.DtoStock.ApprovisionnerStockDTO;
import com.gdg.service_stock.dtos.DtoStock.DecrementStockDTO;
import com.gdg.service_stock.dtos.DtoStock.StockProduitDTO;
import com.gdg.service_stock.dtos.DtoStock.StockPublicDTO;
import com.gdg.service_stock.dtos.DtoStock.UpdateSeuilDTO;
import com.gdg.service_stock.entity.MouvementStock;
import com.gdg.service_stock.entity.StockProduit;
import com.gdg.service_stock.enums.TypeMouvement;
import com.gdg.service_stock.exception.StockInsuffisantException;
import com.gdg.service_stock.repository.CategorieProduitRepository;
import com.gdg.service_stock.repository.MouvementStockRepository;
import com.gdg.service_stock.repository.StockProduitRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


import java.util.HashMap;
import java.util.Map;

/**
 * Couche métier du service stock.
 *
 * Toutes les opérations qui modifient le stock sont @Transactional :
 * si une étape échoue (ex : sauvegarde du mouvement), tout est rollback.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockProduitService {

    private final StockProduitRepository stockRepo;
    private final MouvementStockRepository mouvementRepo;
    private final CategorieProduitRepository categorieRepo;
    private final RabbitTemplate rabbitTemplate;
    
    private final AuthServiceClient authServiceClient;

    public List<StockProduitDTO>getStockGlobal(){
        return stockRepo.findAll()
               .stream()
               .map(this::toDTO)
               .toList();
    }
    // ─── Lecture ──────────────────────────────────────────────────────────

    /** Tout le stock détaillé d'une agence (pour consommateur connecté) */
    @Transactional(readOnly = true)
    public List<StockProduitDTO> getStockParAgence(Long agenceId) {
        return stockRepo.findByAgenceId(agenceId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /** Disponibilité globale d'une agence (pour visiteur non connecté — RG-08) */
    @Transactional(readOnly = true)
    public StockPublicDTO getDisponibilitePublique(Long agenceId) {
        boolean dispo = stockRepo.existsStockDisponibleParAgence(agenceId);
        return StockPublicDTO.builder()
                .agenceId(agenceId)
                .disponible(dispo)
                .build();
    }

    /** Stock d'un produit précis dans une agence */
    @Transactional(readOnly = true)
    public StockProduitDTO getStockParAgenceEtCategorie(Long agenceId, Long categorieId) {
        return stockRepo.findByAgenceIdAndCategorieProduitId(agenceId, categorieId)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Aucun stock trouvé pour agenceId=" + agenceId +
                    " categorieId=" + categorieId));
    }

    /** Stocks critiques d'une agence (pour le dashboard distributeur) */
    @Transactional(readOnly = true)
    public List<StockProduitDTO> getStocksCritiques(Long agenceId) {
        return stockRepo.findStocksCritiquesParAgence(agenceId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ─── Décrémentation (vente) ───────────────────────────────────────────

    /**
     * Décrémente le stock après une vente ou une réservation confirmée.
     * Implémente RG-05 et RG-06.
     *
     * @throws StockInsuffisantException si quantiteDisponible < quantite demandée
     * @throws EntityNotFoundException   si le stock n'existe pas
     */
    @Transactional
    public MouvementResultDTO decrementerStock(DecrementStockDTO dto) {
        StockProduit stock = stockRepo
                .findByAgenceIdAndCategorieProduitId(dto.getAgenceId(), dto.getCategorieProduitId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Stock introuvable : agence=" + dto.getAgenceId() +
                    " categorie=" + dto.getCategorieProduitId()));

        int avant = stock.getQuantiteDisponible();

        try {
            stock.decrementer(dto.getQuantite());
        } catch (IllegalArgumentException e) {
            throw new StockInsuffisantException(
                "Stock insuffisant pour agence=" + dto.getAgenceId() +
                " categorie=" + dto.getCategorieProduitId() +
                " : demandé=" + dto.getQuantite() +
                ", disponible=" + avant
            );
        }

        boolean alerteDeclenchee = false;
        if (stock.estCritique() && !stock.getAlerteEnvoyee()) {
            stock.setAlerteEnvoyee(true);
            alerteDeclenchee = true;
            log.warn("[ALERTE CRITIQUE] agence={} categorie={} quantite={}",
                     stock.getAgenceId(),
                     stock.getCategorieProduit().getLibelle(),
                     stock.getQuantiteDisponible());
            publierAlerteCritique(stock);
        }

        stockRepo.save(stock);

        MouvementStock mouvement = MouvementStock.builder()
                .agenceId(dto.getAgenceId())
                .categorieProduit(stock.getCategorieProduit())
                .typeMouvement(TypeMouvement.SORTIE)
                .quantite(dto.getQuantite())
                .quantiteAvant(avant)
                .quantiteApres(stock.getQuantiteDisponible())
                .motif("Vente")
                .referenceExterne(dto.getReferenceExterne())
                .effectuePar(dto.getEffectuePar())
                .dateMouvement(LocalDateTime.now())
                .build();

        MouvementStock saved = mouvementRepo.save(mouvement);

        return MouvementResultDTO.builder()
                .mouvementId(saved.getId())
                .typeMouvement(TypeMouvement.SORTIE)
                .quantiteAvant(avant)
                .quantiteApres(stock.getQuantiteDisponible())
                .etatApres(stock.getEtat())
                .alerteCritiqueDeclenchee(alerteDeclenchee)
                .dateMouvement(saved.getDateMouvement())
                .build();
    }

    // ─── Incrémentation (approvisionnement) ───────────────────────────────

    @Transactional
    public MouvementResultDTO approvisionnerStock(ApprovisionnerStockDTO dto) {
        StockProduit stock = stockRepo
                .findByAgenceIdAndCategorieProduitId(dto.getAgenceId(), dto.getCategorieProduitId())
                .orElseGet(() -> creerNouveauStock(dto.getAgenceId(), dto.getCategorieProduitId()));

        int avant = stock.getQuantiteDisponible();
        stock.incrementer(dto.getQuantite());
        stockRepo.save(stock);

        MouvementStock mouvement = MouvementStock.builder()
                .agenceId(dto.getAgenceId())
                .categorieProduit(stock.getCategorieProduit())
                .typeMouvement(TypeMouvement.ENTREE)
                .quantite(dto.getQuantite())
                .quantiteAvant(avant)
                .quantiteApres(stock.getQuantiteDisponible())
                .motif("Approvisionnement" + (dto.getFournisseur() != null
                       ? " - " + dto.getFournisseur() : ""))
                .referenceExterne(dto.getNumeroBonLivraison())
                .effectuePar(dto.getEffectuePar())
                .dateMouvement(LocalDateTime.now())
                .build();

        MouvementStock saved = mouvementRepo.save(mouvement);

        log.info("[APPRO] agence={} categorie={} +{} unités (avant={} après={})",
                 stock.getAgenceId(), stock.getCategorieProduit().getLibelle(),
                 dto.getQuantite(), avant, stock.getQuantiteDisponible());

        if (avant == 0 && stock.getQuantiteDisponible() > 0) {
            publierStockDisponible(stock);
        }

        return MouvementResultDTO.builder()
                .mouvementId(saved.getId())
                .typeMouvement(TypeMouvement.ENTREE)
                .quantiteAvant(avant)
                .quantiteApres(stock.getQuantiteDisponible())
                .etatApres(stock.getEtat())
                .alerteCritiqueDeclenchee(false)
                .dateMouvement(saved.getDateMouvement())
                .build();
    }

    // ─── Mise à jour du seuil critique ────────────────────────────────────

    /** RG-07 : chaque agence définit son propre seuil par catégorie */
    @Transactional
    public StockProduitDTO updateSeuilCritique(Long agenceId, Long categorieId,
                                               UpdateSeuilDTO dto) {
        StockProduit stock = stockRepo
                .findByAgenceIdAndCategorieProduitId(agenceId, categorieId)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Stock introuvable : agence=" + agenceId + " categorie=" + categorieId));

        stock.setSeuilCritique(dto.getSeuilCritique());
        stock.setDerniereMiseAJour(LocalDateTime.now());

        if (!stock.estCritique()) {
            stock.setAlerteEnvoyee(false);
        }

        return toDTO(stockRepo.save(stock));
    }

    // ─── Historique ───────────────────────────────────────────────────────

    public List<MouvementStockDTO> getHistorique(Long agenceId) {
        return mouvementRepo
                .findByAgenceIdOrderByDateMouvementDesc(agenceId)
                .stream()
                .map(this::toMouvementDTO)
                .toList();
    }

    // ─── Helpers privés ───────────────────────────────────────────────────

    private StockProduit creerNouveauStock(Long agenceId, Long categorieId) {
        var categorie = categorieRepo.findById(categorieId)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Catégorie introuvable : id=" + categorieId));
        return StockProduit.builder()
                .agenceId(agenceId)
                .categorieProduit(categorie)
                .quantiteDisponible(0)
                .seuilCritique(5)
                .alerteEnvoyee(false)
                .derniereMiseAJour(LocalDateTime.now())
                .build();
    }

    private StockProduitDTO toDTO(StockProduit s) {
        return StockProduitDTO.builder()
                .id(s.getId())
                .agenceId(s.getAgenceId())
                .categorieProduitId(s.getCategorieProduit().getId())
                .categorieLibelle(s.getCategorieProduit().getLibelle())
                .poids(s.getCategorieProduit().getPoids())
                .prixUnitaire(s.getCategorieProduit().getPrixUnitaire())
                .quantiteDisponible(s.getQuantiteDisponible())
                .seuilCritique(s.getSeuilCritique())
                .etat(s.getEtat())
                .alerteEnvoyee(s.getAlerteEnvoyee())
                .derniereMiseAJour(s.getDerniereMiseAJour())
                .build();
    }

    private MouvementStockDTO toMouvementDTO(MouvementStock m) {
        return MouvementStockDTO.builder()
                .id(m.getId())
                .agenceId(m.getAgenceId())
                .categorieLibelle(m.getCategorieProduit().getLibelle())
                .typeMouvement(m.getTypeMouvement())
                .quantite(m.getQuantite())
                .quantiteAvant(m.getQuantiteAvant())
                .quantiteApres(m.getQuantiteApres())
                .motif(m.getMotif())
                .referenceExterne(m.getReferenceExterne())
                .effectuePar(m.getEffectuePar())
                .dateMouvement(m.getDateMouvement())
                .build();
    }

    private void publierAlerteCritique(StockProduit stock) {
        try {
            DistributeurInfoDTO distributeur=authServiceClient.getDistributeurByAgence(stock.getAgenceId());

            Map<String, Object> event = new HashMap<>();
            event.put("agenceId", stock.getAgenceId());
            event.put("categorieProduitId", stock.getCategorieProduit().getId());
            event.put("categorieLibelle", stock.getCategorieProduit().getLibelle());
            event.put("quantiteDisponible", stock.getQuantiteDisponible());
            event.put("seuilCritique", stock.getSeuilCritique());
            event.put("distributeurId",distributeur != null? distributeur.getId():null);
            event.put("emailDistributeur",distributeur != null ? distributeur.getEmail():null);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.KEY_STOCK_CRITIQUE, event);
        } catch (Exception e) {
            log.warn("RabbitMQ indisponible — alerte critique non publiée: {}", e.getMessage());
        }
    }

    private void publierStockDisponible(StockProduit stock) {
        try {
            DistributeurInfoDTO distributeur= authServiceClient.getDistributeurByAgence(stock.getAgenceId());

            Map<String, Object> event = new HashMap<>();
            event.put("agenceId", stock.getAgenceId());
            event.put("categorieProduitId", stock.getCategorieProduit().getId());
            event.put("categorieLibelle", stock.getCategorieProduit().getLibelle());
            event.put("quantiteDisponible", stock.getQuantiteDisponible());
            event.put("distributeurId",distributeur !=null ? distributeur.getId() :null);
            event.put("emailDistributeur",distributeur !=null ? distributeur.getEmail() :null);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.KEY_STOCK_DISPONIBLE, event);
        } catch (Exception e) {
            log.warn("RabbitMQ indisponible — notification stock non publiée: {}", e.getMessage());
        }
    }
}
