-- Données de stock initiales pour tests (agence 1)
-- Exécuter après schema.sql : psql -U postgres -d gdg_auth -f scripts/init-stock.sql

INSERT INTO stock_schema.stock_produit
    (agence_id, categorie_produit_id, quantite_disponible, seuil_critique, alerte_envoyee)
VALUES
    (1, 1, 20, 5, false),
    (1, 2, 50, 5, false),
    (1, 3, 30, 5, false),
    (1, 4, 10, 3, false)
ON CONFLICT (agence_id, categorie_produit_id)
DO UPDATE SET quantite_disponible = EXCLUDED.quantite_disponible;
