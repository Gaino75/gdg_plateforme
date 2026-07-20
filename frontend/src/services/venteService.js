// venes, factures

import axiosInstance from './axiosInstance';
import { API } from '../constants/api';

export const venteService = {
  // Ventes
  enregistrerVente: (data) => axiosInstance.post(API.VENTES.BASE, data),
  getVentesAgence: (agenceId) => axiosInstance.get(API.VENTES.AGENCE(agenceId)),
  getVentesDistributeur: (distributeurId) => axiosInstance.get(API.VENTES.DISTRIBUTEUR(distributeurId)),
  getVenteById:(id) => axiosInstance.get(API.VENTES.DETAIL(id)),
  getChiffreAffaires: (agenceId) => axiosInstance.get(API.VENTES.CA(agenceId)),
 

  // Approvisionnements
  enregistrerApprovisionnement: (data) => axiosInstance.post(API.VENTES.APPROVISIONNEMENTS, data),

  // Factures
  getFacture: (venteId) => axiosInstance.get(API.VENTES.FACTURE(venteId)),
  getFactureById: (id) => axiosInstance.get(API.VENTES.FACTURE_BY_ID(id)),
  getFacturesAgence: (agenceId) => axiosInstance.get(API.VENTES.FACTURES_AGENCE(agenceId)),
};