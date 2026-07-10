-- Supprimer les données existantes
DELETE FROM ventes_schema.facture;
DELETE FROM ventes_schema.vente;

-- Réinitialiser les séquences
ALTER SEQUENCE ventes_schema.vente_id_seq RESTART WITH 1;
ALTER SEQUENCE ventes_schema.facture_id_seq RESTART WITH 1;

-- Insérer des ventes de test
INSERT INTO ventes_schema.vente (
    reference_vente, agence_id, distributeur_id, consommateur_id,
    categorie_produit_id, quantite, prix_unitaire, prix_total,
    mode_paiement, type_vente, statut, observations
) VALUES 
    ('VTE-20260627-00001', 1, 3, 1, 1, 2, 2500, 5000, 'CASH', 'HORS_LIGNE', 'CONFIRMEE', 'Vente test 1'),
    ('VTE-20260627-00002', 1, 3, 2, 2, 1, 6500, 6500, 'ORANGE_MONEY', 'EN_LIGNE', 'CONFIRMEE', 'Vente test 2'),
    ('VTE-20260627-00003', 2, 3, 1, 3, 1, 9000, 9000, 'CASH', 'HORS_LIGNE', 'CONFIRMEE', 'Vente test 3');

-- Insérer des factures
INSERT INTO ventes_schema.facture (
    numero_facture, vente_id, nom_client, telephone_client,
    montant_ht, taux_tva, montant_tva, montant_ttc
) VALUES 
    ('FAC-20260627-00001', 1, 'Jean Dupont', '691234567', 5000, 0, 0, 5000),
    ('FAC-20260627-00002', 2, 'Marie Martin', '692345678', 6500, 0, 0, 6500),
    ('FAC-20260627-00003', 3, 'Jean Dupont', '691234567', 9000, 0, 0, 9000);