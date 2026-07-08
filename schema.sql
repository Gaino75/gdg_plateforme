-- ============================================================
-- SUPPRESSION ET RECRÉATION PROPRE DE TOUS LES SCHÉMAS
-- ============================================================

CREATE SCHEMA IF NOT EXISTS auth_schema;
CREATE SCHEMA IF NOT EXISTS agences_schema;
CREATE SCHEMA IF NOT EXISTS stock_schema;
CREATE SCHEMA IF NOT EXISTS ventes_schema;
CREATE SCHEMA IF NOT EXISTS reservations_schema;
CREATE SCHEMA IF NOT EXISTS paiement_schema;
CREATE SCHEMA IF NOT EXISTS notifications_schema;
CREATE SCHEMA IF NOT EXISTS admin_schema;

-- ============================================================
-- SCHÉMA 1 : AUTH_SCHEMA — Service Authentification
-- ============================================================

-- Table principale de tous les utilisateurs
CREATE TABLE IF NOT EXISTS auth_schema.utilisateur (
    id                BIGSERIAL PRIMARY KEY,
    nom               VARCHAR(100) NOT NULL,
    prenom            VARCHAR(100) NOT NULL,
    email             VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe      VARCHAR(255) NOT NULL,
    telephone         VARCHAR(20),
    role              VARCHAR(20)  NOT NULL
                      CHECK (role IN ('CONSOMMATEUR','DISTRIBUTEUR','ADMIN')),
    statut            VARCHAR(20)  NOT NULL DEFAULT 'ACTIF'
                      CHECK (statut IN ('ACTIF','INACTIF','SUSPENDU')),
    date_inscription  TIMESTAMP    NOT NULL DEFAULT NOW(),
    email_verifie     BOOLEAN      NOT NULL DEFAULT FALSE,
    token_verification VARCHAR(255),
    date_expiration_token TIMESTAMP
);

-- Table spécifique au consommateur
CREATE TABLE IF NOT EXISTS auth_schema.consommateur (
    id               BIGINT PRIMARY KEY
                     REFERENCES auth_schema.utilisateur(id)
                     ON DELETE CASCADE,
    ville_residence  VARCHAR(100),
    date_naissance   DATE
);

-- Table spécifique au distributeur
CREATE TABLE IF NOT EXISTS auth_schema.distributeur (
    id        BIGINT PRIMARY KEY
              REFERENCES auth_schema.utilisateur(id)
              ON DELETE CASCADE,
    agence_id BIGINT,
    poste     VARCHAR(100)
);

-- Table spécifique à l'administrateur
CREATE TABLE IF NOT EXISTS auth_schema.administrateur (
    id              BIGINT PRIMARY KEY
                    REFERENCES auth_schema.utilisateur(id)
                    ON DELETE CASCADE,
    niveau_acces    VARCHAR(20) NOT NULL DEFAULT 'STANDARD'
                    CHECK (niveau_acces IN ('STANDARD','SUPER_ADMIN'))
);

-- Table des tokens JWT révoqués (blacklist)
CREATE TABLE IF NOT EXISTS auth_schema.token_blacklist (
    id          BIGSERIAL PRIMARY KEY,
    token       TEXT        NOT NULL UNIQUE,
    utilisateur_id BIGINT   NOT NULL
                  REFERENCES auth_schema.utilisateur(id),
    date_revocation TIMESTAMP NOT NULL DEFAULT NOW(),
    raison      VARCHAR(100)
);

-- Table des tentatives de connexion (sécurité brute force)
CREATE TABLE IF NOT EXISTS auth_schema.tentative_connexion (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(150) NOT NULL,
    adresse_ip      VARCHAR(50),
    succes          BOOLEAN      NOT NULL,
    date_tentative  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- ============================================================
-- SCHÉMA 2 : AGENCES_SCHEMA — Service Agences
-- ============================================================

-- Table des enseignes (Total, Ola, Tradex...)
CREATE TABLE IF NOT EXISTS agences_schema.enseigne (
    id            BIGSERIAL PRIMARY KEY,
    nom           VARCHAR(100) NOT NULL UNIQUE,
    logo          VARCHAR(255),
    description   TEXT,
    site_web      VARCHAR(255),
    telephone     VARCHAR(20),
    email_contact VARCHAR(150),
    statut        VARCHAR(20)  NOT NULL DEFAULT 'ACTIF'
                  CHECK (statut IN ('ACTIF','INACTIF')),
    date_creation TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Table des villes
CREATE TABLE IF NOT EXISTS agences_schema.ville (
    id      BIGSERIAL PRIMARY KEY,
    nom     VARCHAR(100) NOT NULL,
    region  VARCHAR(100) NOT NULL,
    pays    VARCHAR(100) NOT NULL DEFAULT 'Cameroun'
);

-- Table des agences (points de vente)
CREATE TABLE IF NOT EXISTS agences_schema.agence (
    id               BIGSERIAL PRIMARY KEY,
    nom              VARCHAR(150) NOT NULL,
    adresse          VARCHAR(255) NOT NULL,
    latitude         DOUBLE PRECISION,
    longitude        DOUBLE PRECISION,
    telephone        VARCHAR(20),
    email            VARCHAR(150),
    logo_facture     VARCHAR(255),
    entete_facture   TEXT,
    pied_facture     TEXT,
    statut           VARCHAR(20)  NOT NULL DEFAULT 'EN_ATTENTE'
                     CHECK (statut IN ('EN_ATTENTE','ACTIF','SUSPENDU')),
    date_creation    TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_validation  TIMESTAMP,
    valide_par       BIGINT,
    enseigne_id      BIGINT       NOT NULL
                     REFERENCES agences_schema.enseigne(id),
    ville_id         BIGINT       NOT NULL
                     REFERENCES agences_schema.ville(id)
);

-- Table des horaires d'ouverture
CREATE TABLE IF NOT EXISTS agences_schema.horaire_ouverture (
    id          BIGSERIAL PRIMARY KEY,
    agence_id   BIGINT      NOT NULL
                REFERENCES agences_schema.agence(id)
                ON DELETE CASCADE,
    jour_semaine VARCHAR(15) NOT NULL
                 CHECK (jour_semaine IN (
                     'LUNDI','MARDI','MERCREDI',
                     'JEUDI','VENDREDI','SAMEDI','DIMANCHE'
                 )),
    heure_ouverture TIME,
    heure_fermeture TIME,
    ferme       BOOLEAN     NOT NULL DEFAULT FALSE
);

-- ============================================================
-- SCHÉMA 3 : STOCK_SCHEMA — Service Stock
-- ============================================================

-- Table des catégories de produits gaz
CREATE TABLE IF NOT EXISTS stock_schema.categorie_produit (
    id             BIGSERIAL PRIMARY KEY,
    libelle        VARCHAR(100) NOT NULL UNIQUE,
    poids          DOUBLE PRECISION NOT NULL,
    prix_unitaire  DOUBLE PRECISION NOT NULL,
    description    TEXT,
    actif          BOOLEAN NOT NULL DEFAULT TRUE
);

-- Table du stock par agence et par catégorie
CREATE TABLE IF NOT EXISTS stock_schema.stock_produit (
    id                    BIGSERIAL PRIMARY KEY,
    agence_id             BIGINT   NOT NULL,
    categorie_produit_id  BIGINT   NOT NULL
                          REFERENCES stock_schema.categorie_produit(id),
    quantite_disponible   INTEGER  NOT NULL DEFAULT 0
                          CHECK (quantite_disponible >= 0),
    seuil_critique        INTEGER  NOT NULL DEFAULT 5
                          CHECK (seuil_critique >= 0),
    alerte_envoyee        BOOLEAN  NOT NULL DEFAULT FALSE,
    derniere_mise_a_jour  TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(agence_id, categorie_produit_id)
);

-- Table des mouvements de stock (entrées et sorties)
CREATE TABLE IF NOT EXISTS stock_schema.mouvement_stock (
    id                    BIGSERIAL PRIMARY KEY,
    agence_id             BIGINT   NOT NULL,
    categorie_produit_id  BIGINT   NOT NULL
                          REFERENCES stock_schema.categorie_produit(id),
    type_mouvement        VARCHAR(20) NOT NULL
                          CHECK (type_mouvement IN ('ENTREE','SORTIE')),
    quantite              INTEGER  NOT NULL CHECK (quantite > 0),
    quantite_avant        INTEGER  NOT NULL,
    quantite_apres        INTEGER  NOT NULL,
    motif                 VARCHAR(100),
    reference_externe     VARCHAR(100),
    effectue_par          BIGINT,
    date_mouvement        TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Table des approvisionnements
CREATE TABLE IF NOT EXISTS stock_schema.approvisionnement (
    id                    BIGSERIAL PRIMARY KEY,
    agence_id             BIGINT   NOT NULL,
    distributeur_id       BIGINT   NOT NULL,
    categorie_produit_id  BIGINT   NOT NULL
                          REFERENCES stock_schema.categorie_produit(id),
    quantite              INTEGER  NOT NULL CHECK (quantite > 0),
    fournisseur           VARCHAR(150),
    numero_bon_livraison  VARCHAR(100),
    date_approvisionnement TIMESTAMP NOT NULL DEFAULT NOW(),
    observations          TEXT
);

-- Données initiales des catégories
INSERT INTO stock_schema.categorie_produit
    (libelle, poids, prix_unitaire, description)
VALUES
    ('Bouteille 3 kg',    3.0,   2500,  'Petite bouteille domestique'),
    ('Bouteille 9 kg',    9.0,   6500,  'Bouteille standard domestique'),
    ('Bouteille 12.5 kg', 12.5,  9000,  'Bouteille familiale'),
    ('Bouteille 30 kg',   30.0,  20000, 'Bouteille industrielle')
ON CONFLICT (libelle) DO NOTHING;

-- ============================================================
-- SCHÉMA 4 : VENTES_SCHEMA — Service Ventes
-- ============================================================

-- Table des ventes
CREATE TABLE IF NOT EXISTS ventes_schema.vente (
    id                    BIGSERIAL PRIMARY KEY,
    reference_vente       VARCHAR(50) NOT NULL UNIQUE,
    agence_id             BIGINT      NOT NULL,
    distributeur_id       BIGINT      NOT NULL,
    consommateur_id       BIGINT,
    categorie_produit_id  BIGINT      NOT NULL,
    quantite              INTEGER     NOT NULL CHECK (quantite > 0),
    prix_unitaire         DOUBLE PRECISION NOT NULL,
    prix_total            DOUBLE PRECISION NOT NULL,
    mode_paiement         VARCHAR(20) NOT NULL
                          CHECK (mode_paiement IN
                          ('CASH','ORANGE_MONEY','MTN_MOBILE_MONEY')),
    type_vente            VARCHAR(20) NOT NULL DEFAULT 'HORS_LIGNE'
                          CHECK (type_vente IN ('EN_LIGNE','HORS_LIGNE')),
    statut                VARCHAR(20) NOT NULL DEFAULT 'CONFIRMEE'
                          CHECK (statut IN
                          ('CONFIRMEE','ANNULEE','REMBOURSEE')),
    reference_paiement    VARCHAR(150),
    date_vente            TIMESTAMP   NOT NULL DEFAULT NOW(),
    observations          TEXT
);

-- Table des factures
CREATE TABLE IF NOT EXISTS ventes_schema.facture (
    id               BIGSERIAL PRIMARY KEY,
    numero_facture   VARCHAR(50)  NOT NULL UNIQUE,
    vente_id         BIGINT       NOT NULL UNIQUE
                     REFERENCES ventes_schema.vente(id),
    date_emission    TIMESTAMP    NOT NULL DEFAULT NOW(),
    url_pdf          VARCHAR(255),
    logo_agence      VARCHAR(255),
    entete_agence    TEXT,
    pied_agence      TEXT,
    nom_client       VARCHAR(200),
    telephone_client VARCHAR(20),
    montant_ht       DOUBLE PRECISION,
    taux_tva         DOUBLE PRECISION DEFAULT 0,
    montant_tva      DOUBLE PRECISION DEFAULT 0,
    montant_ttc      DOUBLE PRECISION NOT NULL
);

-- ============================================================
-- SCHÉMA 5 : RESERVATIONS_SCHEMA — Service Réservations
-- ============================================================

CREATE TABLE IF NOT EXISTS reservations_schema.reservation (
    id                    BIGSERIAL PRIMARY KEY,
    reference_reservation VARCHAR(50) NOT NULL UNIQUE,
    agence_id             BIGINT      NOT NULL,
    consommateur_id       BIGINT      NOT NULL,
    categorie_produit_id  BIGINT      NOT NULL,
    quantite              INTEGER     NOT NULL CHECK (quantite > 0),
    prix_unitaire         DOUBLE PRECISION NOT NULL,
    montant_total         DOUBLE PRECISION NOT NULL,
    statut                VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE'
                          CHECK (statut IN (
                              'EN_ATTENTE',
                              'PAYEE',
                              'CONFIRMEE',
                              'ANNULEE',
                              'EXPIREE',
                              'RECUPEREE'
                          )),
    mode_paiement         VARCHAR(20)
                          CHECK (mode_paiement IN
                          ('CASH','ORANGE_MONEY','MTN_MOBILE_MONEY')),
    reference_paiement    VARCHAR(150),
    date_reservation      TIMESTAMP   NOT NULL DEFAULT NOW(),
    date_expiration       TIMESTAMP   NOT NULL
                          DEFAULT (NOW() + INTERVAL '30 minutes'),
    date_confirmation     TIMESTAMP,
    date_recuperation     TIMESTAMP,
    motif_annulation      TEXT,
    observations          TEXT
);

-- Table historique des changements de statut
CREATE TABLE IF NOT EXISTS reservations_schema.historique_reservation (
    id              BIGSERIAL PRIMARY KEY,
    reservation_id  BIGINT      NOT NULL
                    REFERENCES reservations_schema.reservation(id)
                    ON DELETE CASCADE,
    ancien_statut   VARCHAR(20),
    nouveau_statut  VARCHAR(20) NOT NULL,
    commentaire     TEXT,
    effectue_par    BIGINT,
    date_changement TIMESTAMP   NOT NULL DEFAULT NOW()
);

-- ============================================================
-- SCHÉMA 6 : PAIEMENT_SCHEMA — Service Paiement
-- ============================================================

CREATE TABLE IF NOT EXISTS paiement_schema.transaction_paiement (
    id                   BIGSERIAL PRIMARY KEY,
    reference            VARCHAR(100) NOT NULL UNIQUE,
    reservation_id       BIGINT,
    vente_id             BIGINT,
    consommateur_id      BIGINT       NOT NULL,
    montant              DOUBLE PRECISION NOT NULL CHECK (montant > 0),
    mode_paiement        VARCHAR(20)  NOT NULL
                         CHECK (mode_paiement IN
                         ('CASH','ORANGE_MONEY','MTN_MOBILE_MONEY')),
    statut               VARCHAR(20)  NOT NULL DEFAULT 'EN_ATTENTE'
                         CHECK (statut IN (
                             'EN_ATTENTE',
                             'CONFIRME',
                             'CONFIRME_A_VERIFIER',
                             'ECHOUE',
                             'REMBOURSE',
                             'EXPIRE'
                         )),
    numero_telephone     VARCHAR(20),
    operateur            VARCHAR(10)
                         CHECK (operateur IN ('ORANGE','MTN',NULL)),
    reference_operateur  VARCHAR(150),
    date_initiation      TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_confirmation    TIMESTAMP,
    date_expiration      TIMESTAMP    NOT NULL
                         DEFAULT (NOW() + INTERVAL '30 minutes'),
    message_erreur       TEXT,
    agence_id            BIGINT       NOT NULL,
    nombre_tentatives    INTEGER      NOT NULL DEFAULT 0
);

-- Table des remboursements
CREATE TABLE IF NOT EXISTS paiement_schema.remboursement (
    id                       BIGSERIAL PRIMARY KEY,
    reference_remboursement  VARCHAR(100) NOT NULL UNIQUE,
    transaction_id           BIGINT       NOT NULL
                             REFERENCES paiement_schema.transaction_paiement(id),
    montant_rembourse        DOUBLE PRECISION NOT NULL,
    motif                    TEXT         NOT NULL,
    statut                   VARCHAR(20)  NOT NULL DEFAULT 'EN_ATTENTE'
                             CHECK (statut IN
                             ('EN_ATTENTE','EFFECTUE','ECHOUE')),
    reference_operateur      VARCHAR(150),
    demande_par              BIGINT,
    date_demande             TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_execution           TIMESTAMP
);

-- Table des callbacks des opérateurs Mobile Money
CREATE TABLE IF NOT EXISTS paiement_schema.callback_operateur (
    id              BIGSERIAL PRIMARY KEY,
    transaction_id  BIGINT
                    REFERENCES paiement_schema.transaction_paiement(id),
    operateur       VARCHAR(10)  NOT NULL
                    CHECK (operateur IN ('ORANGE','MTN')),
    payload_recu    TEXT         NOT NULL,
    statut_recu     VARCHAR(50),
    date_reception  TIMESTAMP    NOT NULL DEFAULT NOW(),
    traite          BOOLEAN      NOT NULL DEFAULT FALSE,
    erreur_traitement TEXT
);

-- ============================================================
-- SCHÉMA 7 : NOTIFICATIONS_SCHEMA — Service Notifications
-- ============================================================

-- Table des notifications envoyées
CREATE TABLE IF NOT EXISTS notifications_schema.notification (
    id                 BIGSERIAL PRIMARY KEY,
    utilisateur_id     BIGINT      NOT NULL,
    titre              VARCHAR(200) NOT NULL,
    message            TEXT         NOT NULL,
    type_notification  VARCHAR(30)  NOT NULL
                       CHECK (type_notification IN (
                           'STOCK_DISPONIBLE',
                           'SEUIL_CRITIQUE',
                           'RESERVATION_CONFIRMEE',
                           'RESERVATION_ANNULEE',
                           'RESERVATION_EXPIREE',
                           'PAIEMENT_CONFIRME',
                           'PAIEMENT_ECHOUE',
                           'REMBOURSEMENT',
                           'SIGNALEMENT_TRAITE',
                           'COMPTE_VALIDE',
                           'AGENCE_VALIDEE'
                       )),
    canal              VARCHAR(20)  NOT NULL DEFAULT 'IN_APP'
                       CHECK (canal IN ('IN_APP','EMAIL','SMS')),
    statut             VARCHAR(20)  NOT NULL DEFAULT 'NON_LU'
                       CHECK (statut IN ('NON_LU','LU','ENVOYE','ECHOUE')),
    reference_id       BIGINT,
    reference_type     VARCHAR(50),
    date_envoi         TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_lecture       TIMESTAMP
);

-- Table des abonnements aux alertes
CREATE TABLE IF NOT EXISTS notifications_schema.abonnement (
    id                    BIGSERIAL PRIMARY KEY,
    consommateur_id       BIGINT   NOT NULL,
    agence_id             BIGINT   NOT NULL,
    categorie_produit_id  BIGINT,
    actif                 BOOLEAN  NOT NULL DEFAULT TRUE,
    date_abonnement       TIMESTAMP NOT NULL DEFAULT NOW(),
    date_desabonnement    TIMESTAMP,
    UNIQUE(consommateur_id, agence_id, categorie_produit_id)
);

-- Table des signalements consommateurs
CREATE TABLE IF NOT EXISTS notifications_schema.signalement (
    id                    BIGSERIAL PRIMARY KEY,
    consommateur_id       BIGINT      NOT NULL,
    agence_id             BIGINT      NOT NULL,
    categorie_produit_id  BIGINT      NOT NULL,
    type_signalement      VARCHAR(20) NOT NULL
                          CHECK (type_signalement IN
                          ('RUPTURE','DISPONIBLE')),
    statut                VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE'
                          CHECK (statut IN
                          ('EN_ATTENTE','CONFIRME','REJETE')),
    commentaire           TEXT,
    date_signalement      TIMESTAMP   NOT NULL DEFAULT NOW(),
    date_traitement       TIMESTAMP,
    traite_par            BIGINT
);

-- ============================================================
-- SCHÉMA 8 : ADMIN_SCHEMA — Service Administration
-- ============================================================

-- Table du journal d'audit (toutes les actions importantes)
CREATE TABLE IF NOT EXISTS admin_schema.journal_audit (
    id               BIGSERIAL PRIMARY KEY,
    utilisateur_id   BIGINT,
    role_utilisateur VARCHAR(20),
    action           VARCHAR(100) NOT NULL,
    entite_type      VARCHAR(50),
    entite_id        BIGINT,
    details          TEXT,
    adresse_ip       VARCHAR(50),
    date_action      TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Table des statistiques journalières (précalculées)
CREATE TABLE IF NOT EXISTS admin_schema.statistique_journaliere (
    id                     BIGSERIAL PRIMARY KEY,
    date_stat              DATE         NOT NULL,
    agence_id              BIGINT,
    nb_ventes              INTEGER      NOT NULL DEFAULT 0,
    montant_total_ventes   DOUBLE PRECISION NOT NULL DEFAULT 0,
    nb_reservations        INTEGER      NOT NULL DEFAULT 0,
    nb_reservations_annulees INTEGER    NOT NULL DEFAULT 0,
    nb_nouveaux_clients    INTEGER      NOT NULL DEFAULT 0,
    nb_signalements        INTEGER      NOT NULL DEFAULT 0,
    nb_alertes_stock       INTEGER      NOT NULL DEFAULT 0,
    UNIQUE(date_stat, agence_id)
);

-- Table des paramètres globaux de la plateforme
CREATE TABLE IF NOT EXISTS admin_schema.parametre_plateforme (
    id          BIGSERIAL PRIMARY KEY,
    cle         VARCHAR(100) NOT NULL UNIQUE,
    valeur      TEXT         NOT NULL,
    description TEXT,
    modifie_par BIGINT,
    date_modification TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Paramètres par défaut
INSERT INTO admin_schema.parametre_plateforme
    (cle, valeur, description)
VALUES
    ('DUREE_EXPIRATION_RESERVATION', '30',
     'Durée en minutes avant expiration d une réservation non payée'),
    ('NB_SIGNALEMENTS_CONFIRMATION', '2',
     'Nombre de signalements concordants pour confirmer une rupture'),
    ('DELAI_SIGNALEMENT_HEURES', '2',
     'Délai minimum en heures entre deux signalements du même utilisateur'),
    ('TAUX_TVA', '0',
     'Taux de TVA applicable en pourcentage')
ON CONFLICT (cle) DO NOTHING;

-- Table des demandes d'inscription agences en attente
CREATE TABLE IF NOT EXISTS admin_schema.demande_inscription_agence (
    id              BIGSERIAL PRIMARY KEY,
    nom_agence      VARCHAR(150) NOT NULL,
    nom_enseigne    VARCHAR(100) NOT NULL,
    nom_ville       VARCHAR(100) NOT NULL,
    adresse         VARCHAR(255),
    telephone       VARCHAR(20),
    email           VARCHAR(150),
    nom_responsable VARCHAR(200) NOT NULL,
    email_responsable VARCHAR(150) NOT NULL,
    telephone_responsable VARCHAR(20),
    statut          VARCHAR(20)  NOT NULL DEFAULT 'EN_ATTENTE'
                    CHECK (statut IN
                    ('EN_ATTENTE','APPROUVEE','REJETEE')),
    motif_rejet     TEXT,
    traite_par      BIGINT,
    date_demande    TIMESTAMP    NOT NULL DEFAULT NOW(),
    date_traitement TIMESTAMP
);
