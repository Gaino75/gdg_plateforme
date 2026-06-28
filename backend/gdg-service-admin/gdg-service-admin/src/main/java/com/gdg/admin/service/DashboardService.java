package com.gdg.admin.service;

import com.gdg.admin.dto.*;
import com.gdg.admin.model.DemandeInscriptionAgence.StatutDemande;
import com.gdg.admin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.Arrays;

@Service
public class DashboardService {

    @Autowired
    private DemandeInscriptionAgenceRepository demandeRepository;

    @Autowired
    private StatistiqueJournaliereRepository statistiqueRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String AUTH_URL =
        "http://localhost:8081";
    private static final String AGENCES_URL =
        "http://localhost:8082";
    private static final String STOCK_URL =
        "http://localhost:8083";
    private static final String NOTIF_URL =
        "http://localhost:8087";

    public DashboardDTO getDashboard() {

        DashboardDTO dashboard = new DashboardDTO();

        // ── Données locales ──
        dashboard.setNbDemandesEnAttente(
            (int) demandeRepository
                .countByStatut(StatutDemande.EN_ATTENTE));

        Integer nbVentes = statistiqueRepository
            .getTotalVentesParDate(LocalDate.now());
        dashboard.setNbVentesJour(
            nbVentes != null ? nbVentes : 0);

        Double montant = statistiqueRepository
            .getTotalMontantGlobal();
        dashboard.setMontantVentesJour(
            montant != null ? montant : 0.0);

        // ── Appel Service Agences ──
        try {
            AgenceDTO[] actives = restTemplate.getForObject(
                AGENCES_URL + "/agences?statut=ACTIF",
                AgenceDTO[].class);
            dashboard.setNbAgencesActives(
                actives != null ? actives.length : 0);

            AgenceDTO[] attente = restTemplate.getForObject(
                AGENCES_URL + "/agences?statut=EN_ATTENTE",
                AgenceDTO[].class);
            dashboard.setNbAgencesEnAttente(
                attente != null ? attente.length : 0);

            AgenceDTO[] suspendues = restTemplate.getForObject(
                AGENCES_URL + "/agences?statut=SUSPENDU",
                AgenceDTO[].class);
            dashboard.setNbAgencesSuspendues(
                suspendues != null ? suspendues.length : 0);

        } catch (Exception e) {
            // Service Agences indisponible → valeurs à 0
            dashboard.setNbAgencesActives(0);
            dashboard.setNbAgencesEnAttente(0);
            dashboard.setNbAgencesSuspendues(0);
        }

        // ── Appel Service Auth ──
        try {
            UtilisateurDTO[] users = restTemplate.getForObject(
                AUTH_URL + "/admin/utilisateurs",
                UtilisateurDTO[].class);
            if (users != null) {
                dashboard.setNbUtilisateursTotal(users.length);
                long nbConso = Arrays.stream(users)
                    .filter(u -> "CONSOMMATEUR".equals(u.getRole()))
                    .count();
                dashboard.setNbConsommateurs((int) nbConso);
                long nbDist = Arrays.stream(users)
                    .filter(u -> "DISTRIBUTEUR".equals(u.getRole()))
                    .count();
                dashboard.setNbDistributeurs((int) nbDist);
            }
        } catch (Exception e) {
            dashboard.setNbUtilisateursTotal(0);
            dashboard.setNbConsommateurs(0);
            dashboard.setNbDistributeurs(0);
        }

        // ── Appel Service Stock ──
        try {
            StockDTO[] stocks = restTemplate.getForObject(
                STOCK_URL + "/admin/stocks/global",
                StockDTO[].class);
            if (stocks != null) {
                long alertes = Arrays.stream(stocks)
                    .filter(s -> s.getQuantiteDisponible()
                        <= s.getSeuilCritique())
                    .count();
                dashboard.setNbAlertesStockCritique((int) alertes);
            }
        } catch (Exception e) {
            dashboard.setNbAlertesStockCritique(0);
        }

        // ── Appel Service Notifications ──
        try {
            SignalementDTO[] signalements = restTemplate.getForObject(
                NOTIF_URL + "/admin/signalements?statut=EN_ATTENTE",
                SignalementDTO[].class);
            dashboard.setNbSignalementsEnAttente(
                signalements != null ? signalements.length : 0);
        } catch (Exception e) {
            dashboard.setNbSignalementsEnAttente(0);
        }

        return dashboard;
    }
}