// venes, factures

import axiosInstance from './axiosInstance';
import { API } from '../constants/api';

export const venteService = {
  // Ventes
  enregistrerVente: (data) => axiosInstance.post(API.VENTES.BASE, data),
  getVentesAgence: (agenceId) => axiosInstance.get(API.VENTES.AGENCE(agenceId)),
  getVentesDistributeur: (distributeurId) => axiosInstance.get(API.VENTES.DISTRIBUTEUR(distributeurId)),

  // Approvisionnements
  enregistrerApprovisionnement: (data) => axiosInstance.post(API.VENTES.APPROVISIONNEMENTS, data),

  // Factures
  getFacture: (venteId) => axiosInstance.get(API.VENTES.FACTURE(venteId)),
};