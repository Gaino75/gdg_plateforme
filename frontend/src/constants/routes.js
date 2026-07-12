// Toutes les routes de l'app

export const ROUTES = {
  // Routes publiques
  HOME: '/',
  ENSEIGNE: '/enseigne/:id',
  AGENCE: '/agence/:id',
  STATIONS: '/stations',
  CONTACT: '/contact',
  
  // Auth
  LOGIN: '/connexion',
  REGISTER: '/inscription',
  REGISTER_CONSUMER: '/inscription/consommateur',
  REGISTER_DISTRIBUTOR: '/inscription/distributeur',
  FORGOT_PASSWORD: '/mot-de-passe-oublie',
  RESET_PASSWORD: '/reinitialiser-mot-de-passe',
  
  // Consumer
  CONSUMER_DASHBOARD: '/consommateur',
  CONSUMER_AGENCE: '/consommateur/agence/:id',
  CONSUMER_RESERVATIONS: '/consommateur/reservations',
  CONSUMER_RESERVATION: '/consommateur/reservation/:id',
  CONSUMER_NEW_RESERVATION: '/consommateur/reservation/nouvelle',
  CONSUMER_PAIEMENT: '/consommateur/paiement',
  CONSUMER_FACTURES: '/consommateur/factures',
  CONSUMER_NOTIFICATIONS: '/consommateur/notifications',
  CONSUMER_ABONNEMENTS: '/consommateur/abonnements',
  CONSUMER_SIGNALEMENTS: '/consommateur/signalements',
  CONSUMER_NEW_SIGNALEMENT: '/consommateur/signalement/nouveau',
  CONSUMER_PROFIL: '/consommateur/profil',
  
  // Distributor
  DISTRIBUTOR_DASHBOARD: '/distributeur',
  DISTRIBUTOR_STOCK: '/distributeur/stock',
  DISTRIBUTOR_VENTE: '/distributeur/vente/enregistrer',
  DISTRIBUTOR_HISTORIQUE: '/distributeur/ventes/historique',
  DISTRIBUTOR_VENTE_DETAIL: '/distributeur/vente/:id',
  DISTRIBUTOR_APPROVISIONNEMENT: '/distributeur/approvisionnement',
  DISTRIBUTOR_RESERVATIONS: '/distributeur/reservations',
  DISTRIBUTOR_RESERVATION: '/distributeur/reservation/:id',
  DISTRIBUTOR_STATISTIQUES: '/distributeur/statistiques',
  DISTRIBUTOR_NOTIFICATIONS: '/distributeur/notifications',
  DISTRIBUTOR_PARAMETRES: '/distributeur/parametres',
  DISTRIBUTOR_PROFIL: '/distributeur/profil',
  
  // Admin
  ADMIN_DASHBOARD: '/admin',
  ADMIN_ENSEIGNES: '/admin/enseignes',
  ADMIN_ENSEIGNE: '/admin/enseigne/:id',
  ADMIN_VILLES: '/admin/villes',
  ADMIN_AGENCES: '/admin/agences',
  ADMIN_AGENCE: '/admin/agence/:id',
  ADMIN_DEMANDES: '/admin/demandes',
  ADMIN_UTILISATEURS: '/admin/utilisateurs',
  ADMIN_SIGNALEMENTS: '/admin/signalements',
  ADMIN_PAIEMENTS: '/admin/paiements',
  ADMIN_STOCKS: '/admin/stocks',
  ADMIN_STATISTIQUES: '/admin/statistiques',
  ADMIN_JOURNAL: '/admin/journal',
  ADMIN_PARAMETRES: '/admin/parametres',
  ADMIN_PROFIL: '/admin/profil',
};