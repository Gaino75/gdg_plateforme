// reservations

import axiosInstance from './axiosInstance';
import { API } from '../constants/api';

export const reservationService = {
  create: (data) => axiosInstance.post(API.RESERVATIONS.BASE, data),
  getById: (id) => axiosInstance.get(`${API.RESERVATIONS.BASE}/${id}`),
  getByConsommateur: (id) => axiosInstance.get(API.RESERVATIONS.CONSOMMATEUR(id)),
  getActivesByConsommateur: (id) => axiosInstance.get(API.RESERVATIONS.CONSOMMATEUR_ACTIVES(id)),
  getByAgence: (agenceId) => axiosInstance.get(API.RESERVATIONS.AGENCE(agenceId)),
  getByStatut: (statut) => axiosInstance.get(API.RESERVATIONS.STATUT(statut)),
  annuler: (id, motif, effectuePar) => 
    axiosInstance.put(API.RESERVATIONS.ANNULER(id, motif, effectuePar)),
  // recuperer: (id) => axiosInstance.put(API.RESERVATIONS.RECUPERER(id)),
  recuperer: (id, consommateurId) => axiosInstance.put(API.RESERVATIONS.RECUPERER(id, consommateurId)),
  confirmerPaiement: (id, referencePaiement) => 
    axiosInstance.put(API.RESERVATIONS.PAIEMENT(id, referencePaiement)),
  getStatistiques: () => axiosInstance.get(API.RESERVATIONS.STATISTIQUES),
  getHistorique: (id) => axiosInstance.get(API.RESERVATIONS.HISTORIQUE(id)),
};