// paiement Mobile money

import axiosInstance from './axiosInstance';
import { API } from '../constants/api';

export const paiementService = {
  initier: (data) => axiosInstance.post(API.PAIEMENTS.INITIER, data),
  getById: (id) => axiosInstance.get(`${API.PAIEMENTS.BASE}/${id}`),
  getByClient: (id) => axiosInstance.get(API.PAIEMENTS.CLIENT(id)),
  getAVerifier: () => axiosInstance.get(API.PAIEMENTS.A_VERIFIER),
  resoudre: (id, confirmer) => axiosInstance.put(API.PAIEMENTS.RESOUDRE(id, confirmer)),
  annuler: (id) => axiosInstance.put(API.PAIEMENTS.ANNULER(id)),
  valider: (id) => axiosInstance.put(API.PAIEMENTS.VALIDER(id)),
};