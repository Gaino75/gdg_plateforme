// Stock, seuils, mouvements

import axiosInstance from './axiosInstance';
import { API } from '../constants/api';

export const stockService = {
  // Catégories
  getCategories: () => axiosInstance.get(API.STOCK.CATEGORIES),

  // Stock
  getStockAgence: (agenceId) => axiosInstance.get(API.STOCK.AGENCE(agenceId)),
  getStockPublic: (agenceId) => axiosInstance.get(API.STOCK.PUBLIC(agenceId)),
  getStockCritiques: (agenceId) => axiosInstance.get(API.STOCK.CRITIQUES(agenceId)),
  getHistorique: (agenceId) => axiosInstance.get(API.STOCK.HISTORIQUE(agenceId)),
  getStockGlobal: () => axiosInstance.get(API.STOCK.GLOBAL),
  decrementer: (data) => axiosInstance.post(API.STOCK.DECREMENTER, data),
  approvisionner: (data) => axiosInstance.post(API.STOCK.APPROVISIONNER, data),
  updateSeuil: (agenceId, categorieId, seuil) => 
    axiosInstance.patch(API.STOCK.SEUIL(agenceId, categorieId), { seuilCritique: seuil }),
};