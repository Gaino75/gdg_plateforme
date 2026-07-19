-- Supprimer les données existantes (pour les tests uniquement)
DELETE FROM notifications_schema.signalement;
DELETE FROM notifications_schema.abonnement;
DELETE FROM notifications_schema.notification;

-- 1. Insérer un utilisateur test (si vous avez aussi le service Auth)
INSERT INTO auth_schema.utilisateur (id, nom, prenom, email, mot_de_passe, telephone, role, statut, email_verifie)
VALUES 
    (1, 'DUPONT', 'Jean', 'jean.dupont@email.com', '$2a$12$XYZ...', '691234567', 'CONSOMMATEUR', 'ACTIF', true),
    (2, 'MARTIN', 'Marie', 'marie.martin@email.com', '$2a$12$XYZ...', '692345678', 'CONSOMMATEUR', 'ACTIF', true),
    (3, 'KAMGA', 'Paul', 'paul.kamga@email.com', '$2a$12$XYZ...', '693456789', 'DISTRIBUTEUR', 'ACTIF', true)
ON CONFLICT (id) DO NOTHING;

-- 2. Insérer un consommateur
INSERT INTO auth_schema.consommateur (id, ville_residence)
VALUES 
    (1, 'Dschang'),
    (2, 'Bafoussam')
ON CONFLICT (id) DO NOTHING;

-- 3. Insérer un distributeur
INSERT INTO auth_schema.distributeur (id, agence_id, poste)
VALUES 
    (3, 1, 'Gérant')
ON CONFLICT (id) DO NOTHING;

-- 4. Insérer une enseigne
INSERT INTO agences_schema.enseigne (id, nom, logo, statut)
VALUES 
    (1, 'Total', 'total_logo.png', 'ACTIF'),
    (2, 'Ola Energy', 'ola_logo.png', 'ACTIF')
ON CONFLICT (id) DO NOTHING;

-- 5. Insérer une ville
INSERT INTO agences_schema.ville (id, nom, region)
VALUES 
    (1, 'Dschang', 'Ouest'),
    (2, 'Bafoussam', 'Ouest'),
    (3, 'Douala', 'Littoral')
ON CONFLICT (id) DO NOTHING;

-- 6. Insérer une agence
INSERT INTO agences_schema.agence (id, nom, adresse, latitude, longitude, telephone, email, statut, enseigne_id, ville_id)
VALUES 
    (1, 'Total Dschang Centre', 'Avenue de la Libération', 5.4567, 10.1234, '699123456', 'total.dschang@total.cm', 'ACTIF', 1, 1),
    (2, 'Ola Energy Bafoussam', 'Rue Marché', 5.7890, 10.5678, '699234567', 'ola.bafoussam@ola.cm', 'ACTIF', 2, 2)
ON CONFLICT (id) DO NOTHING;

-- 7. Insérer des catégories de produits
INSERT INTO stock_schema.categorie_produit (id, libelle, poids, prix_unitaire)
VALUES 
    (1, 'Bouteille 3 kg', 3.0, 2500),
    (2, 'Bouteille 9 kg', 9.0, 6500),
    (3, 'Bouteille 12.5 kg', 12.5, 9000),
    (4, 'Bouteille 30 kg', 30.0, 20000)
ON CONFLICT (id) DO NOTHING;

-- 8. Insérer des stocks
INSERT INTO stock_schema.stock_produit (id, agence_id, categorie_produit_id, quantite_disponible, seuil_critique)
VALUES 
    (1, 1, 1, 25, 5),
    (2, 1, 2, 15, 3),
    (3, 1, 3, 10, 2),
    (4, 2, 1, 30, 5),
    (5, 2, 2, 20, 3)
ON CONFLICT (id) DO NOTHING;

-- 9. Insérer des abonnements de test
INSERT INTO notifications_schema.abonnement (consommateur_id, agence_id, categorie_produit_id, actif)
VALUES 
    (1, 1, 1, true),   -- Jean est abonné au gaz 3kg chez Total Dschang
    (1, 1, 2, true),   -- Jean est abonné au gaz 9kg chez Total Dschang
    (2, 2, 1, true),   -- Marie est abonnée au gaz 3kg chez Ola Bafoussam
    (2, 2, null, true) -- Marie est abonnée à toutes les catégories chez Ola Bafoussam
ON CONFLICT DO NOTHING;

-- 10. Insérer des notifications de test
INSERT INTO notifications_schema.notification (utilisateur_id, titre, message, type_notification, canal, statut)
VALUES 
    (1, 'Bienvenue sur GDG !', 'Votre compte a été créé avec succès.', 'COMPTE_VALIDE', 'IN_APP', 'LU'),
    (1, 'Stock disponible', 'Le gaz 9kg est disponible à Total Dschang.', 'STOCK_DISPONIBLE', 'IN_APP', 'NON_LU'),
    (2, 'Bienvenue sur GDG !', 'Votre compte a été créé avec succès.', 'COMPTE_VALIDE', 'IN_APP', 'LU')
ON CONFLICT DO NOTHING;

-- 11. Insérer des signalements de test
INSERT INTO notifications_schema.signalement (consommateur_id, agence_id, categorie_produit_id, type_signalement, statut)
VALUES 
    (1, 1, 1, 'DISPONIBLE', 'CONFIRME'),
    (2, 2, 1, 'RUPTURE', 'EN_ATTENTE')
ON CONFLICT DO NOTHING;

-- Vérification
SELECT '✅ Données de test insérées avec succès !' AS message;