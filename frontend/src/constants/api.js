// URLs des endpoints backend

// URL de base de la Gateway
const GATEWAY_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

export const API = {
  // Auth
  AUTH: {
    LOGIN: `${GATEWAY_URL}/auth/login`,
    REGISTER_CONSUMER: `${GATEWAY_URL}/auth/register/consommateur`,
    REGISTER_DISTRIBUTOR: `${GATEWAY_URL}/auth/register/distributeur`,
    REGISTER_ADMIN: `${GATEWAY_URL}/auth/register/admin`,
    LOGOUT: `${GATEWAY_URL}/auth/logout`,
    PROFIL: `${GATEWAY_URL}/auth/profil`,
    FORGOT_PASSWORD: `${GATEWAY_URL}/auth/forgot-password`,
    RESET_PASSWORD: `${GATEWAY_URL}/auth/reset-password`,
    ADMIN_USERS: `${GATEWAY_URL}/auth/admin/users`,
    SUSPENDRE_USER: (id) => `${GATEWAY_URL}/auth/admin/utilisateurs/${id}/suspendre`,
    REACTIVER_USER: (id) => `${GATEWAY_URL}/auth/admin/utilisateurs/${id}/reactiver`,
    DELETE_USER: (id) => `${GATEWAY_URL}/auth/admin/utilisateurs/${id}`,
  },

  // Agences
  AGENCES: {
    BASE: `${GATEWAY_URL}/api/agences`,
    ACTIVES: `${GATEWAY_URL}/api/agences/actives`,
    EN_ATTENTE: `${GATEWAY_URL}/api/agences/en-attente`,
    STATISTIQUES: `${GATEWAY_URL}/api/agences/statistiques`,
    DISTRIBUTEUR: `${GATEWAY_URL}/api/agences/distributeur`,
    VALIDER: (id, adminId) =>
      `${GATEWAY_URL}/api/agences/${id}/valider?adminId=${adminId}`,
    REJETER: (id) => `${GATEWAY_URL}/api/agences/${id}/rejeter`,
    SUSPENDRE: (id) => `${GATEWAY_URL}/api/agences/${id}/suspendre`,
    REACTIVER: (id) => `${GATEWAY_URL}/api/agences/${id}/reactiver`,
    ENSEIGNES: `${GATEWAY_URL}/api/enseignes`,
    VILLES: `${GATEWAY_URL}/api/villes`,
    RECHERCHE: (nom) => `${GATEWAY_URL}/api/agences/recherche?nom=${nom}`,
    DETAIL: (id) => `${GATEWAY_URL}/api/agences/${id}`,
    VILLE: (villeId) => `${GATEWAY_URL}/api/agences/ville/${villeId}/actives`,
  },

  // Stock
  STOCK: {
    CATEGORIES: `${GATEWAY_URL}/api/categories`,
    PUBLIC_DISPONIBILITE:
      `${GATEWAY_URL}/api/stocks/public`,//${agenceId}/disponibilite`,

     // PUBLIC_DISPONIBILITE: (agenceId) => `${GATEWAY_URL}/api/stocks/public`,//${agenceId}/disponibilite`,
    AGENCE: (agenceId) => `${GATEWAY_URL}/api/stocks/agence/${agenceId}`,
    CRITIQUES: (agenceId) =>
      `${GATEWAY_URL}/api/stocks/agence/${agenceId}/critiques`,
    HISTORIQUE: (agenceId) =>
      `${GATEWAY_URL}/api/stocks/agence/${agenceId}/historique`,
    GLOBAL: `${GATEWAY_URL}/api/stocks/global`,
    DECREMENTER: `${GATEWAY_URL}/api/stocks/decrementer`,
    APPROVISIONNER: `${GATEWAY_URL}/api/stocks/approvisionner`,
    SEUIL: (agenceId, categorieId) =>
      `${GATEWAY_URL}/api/stocks/agence/${agenceId}/categorie/${categorieId}/seuil`,
  },

  // Ventes
  VENTES: {
    BASE: `${GATEWAY_URL}/api/ventes`,
    AGENCE: (agenceId) => `${GATEWAY_URL}/api/ventes/agence/${agenceId}`,
    DISTRIBUTEUR: (distributeurId) =>
      `${GATEWAY_URL}/api/ventes/distributeur/${distributeurId}`,
    APPROVISIONNEMENTS: `${GATEWAY_URL}/api/approvisionnements`,
    FACTURE: (venteId) => `${GATEWAY_URL}/api/factures/vente/${venteId}`,
    DETAIL: (id) => `${GATEWAY_URL}/api/ventes/${id}`,
    CA: (agenceId) => `${GATEWAY_URL}/api/ventes/agence/${agenceId}/ca`,
    FACTURE_BY_ID: (id) => `${GATEWAY_URL}/api/factures/${id}`,
    FACTURES_AGENCE: (agenceId) =>
      `${GATEWAY_URL}/api/factures/agence/${agenceId}`,
  },

  // Réservations
  RESERVATIONS: {
    BASE: `${GATEWAY_URL}/api/reservations`,
    CONSOMMATEUR: (id) => `${GATEWAY_URL}/api/reservations/consommateur/${id}`,
    CONSOMMATEUR_ACTIVES: (id) =>
      `${GATEWAY_URL}/api/reservations/consommateur/${id}/actives`,
    AGENCE: (agenceId) => `${GATEWAY_URL}/api/reservations/agence/${agenceId}`,
    STATUT: (statut) => `${GATEWAY_URL}/api/reservations/statut/${statut}`,
    STATISTIQUES: `${GATEWAY_URL}/api/reservations/statistiques`,
    ANNULER: (id, motif, effectuePar) =>
      `${GATEWAY_URL}/api/reservations/${id}/annuler?motif=${motif}&effectuePar=${effectuePar}`,
    // RECUPERER: (id) => `${GATEWAY_URL}/api/reservations/${id}/recuperer`,
    RECUPERER: (id, consommateurId) =>
      `${GATEWAY_URL}/api/reservations/${id}/recuperer?consommateurId=${consommateurId}`,
    PAIEMENT: (id, ref) =>
      `${GATEWAY_URL}/api/reservations/${id}/paiement?referencePaiement=${ref}`,
  },

  // Paiements
  PAIEMENTS: {
    BASE: `${GATEWAY_URL}/api/paiements`,
    INITIER: `${GATEWAY_URL}/api/paiements/initier`,
    A_VERIFIER: `${GATEWAY_URL}/api/paiements/a-verifier`,
    CLIENT: (id) => `${GATEWAY_URL}/api/paiements/client/${id}`,
    RESOUDRE: (id, confirmer) =>
      `${GATEWAY_URL}/api/paiements/${id}/resoudre?confirmerQuandMeme=${confirmer}`,
    ANNULER: (id) => `${GATEWAY_URL}/api/paiements/${id}/annuler`,
    VALIDER: (id) => `${GATEWAY_URL}/api/paiements/${id}/valider`,
  },

  // Notifications
  NOTIFICATIONS: {
    BASE: `${GATEWAY_URL}/api/notifications`,
    USER: (id) => `${GATEWAY_URL}/api/notifications/user/${id}`,
    USER_UNREAD: (id) => `${GATEWAY_URL}/api/notifications/user/${id}/unread`,
    USER_UNREAD_COUNT: (id) =>
      `${GATEWAY_URL}/api/notifications/user/${id}/unread/count`,
    READ: (id) => `${GATEWAY_URL}/api/notifications/${id}/read`,
    READ_ALL: (id) => `${GATEWAY_URL}/api/notifications/user/${id}/read-all`,
    ABONNEMENTS: `${GATEWAY_URL}/api/abonnements`,
    ABONNEMENTS_CONSOMMATEUR: (id) =>
      `${GATEWAY_URL}/api/abonnements/consommateur/${id}`,
    SIGNALEMENTS: `${GATEWAY_URL}/api/signalements`,
    SIGNALEMENTS_AGENCE: (agenceId) =>
      `${GATEWAY_URL}/api/signalements/agence/${agenceId}`,
    TRAITER_SIGNALEMENT: (id, statut, adminId) =>
      `${GATEWAY_URL}/api/signalements/${id}/traiter?statut=${statut}&traitePar=${adminId}`,
  },

  // Admin
  ADMIN: {
    DASHBOARD: `${GATEWAY_URL}/admin/dashboard`,
    STATISTIQUES: `${GATEWAY_URL}/admin/statistiques`,
    JOURNAL: `${GATEWAY_URL}/admin/journal`,
    PARAMETRES: `${GATEWAY_URL}/admin/parametres`,
    DEMANDES: `${GATEWAY_URL}/admin/demandes`,
  },
};
