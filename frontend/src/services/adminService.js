// administration de la plateforme

import axiosInstance from './axiosInstance';
import { API } from '../constants/api';

export const adminService = {
  getDashboard: () => axiosInstance.get(API.ADMIN.DASHBOARD),
  getStatistiques: () => axiosInstance.get(API.ADMIN.STATISTIQUES),
  getJournal: (page = 0, size = 20) => 
    axiosInstance.get(`${API.ADMIN.JOURNAL}?page=${page}&size=${size}`),
  getParametres: () => axiosInstance.get(API.ADMIN.PARAMETRES),
  updateParametre: (cle, valeur) => 
    axiosInstance.put(`${API.ADMIN.PARAMETRES}/${cle}`, { valeur }),
  getDemandes: () => axiosInstance.get(API.ADMIN.DEMANDES),
};