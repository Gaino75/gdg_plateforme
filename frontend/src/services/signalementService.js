// signalements consommateurs

import axiosInstance from './axiosInstance';
import { API } from '../constants/api';

export const signalementService = {
  getAll: () => axiosInstance.get(API.NOTIFICATIONS.SIGNALEMENTS),
  getByAgence: (agenceId) => axiosInstance.get(API.NOTIFICATIONS.SIGNALEMENTS_AGENCE(agenceId)),
  create: (data) => axiosInstance.post(API.NOTIFICATIONS.SIGNALEMENTS, data),
  traiter: (id, statut, adminId) => 
    axiosInstance.put(API.NOTIFICATIONS.TRAITER_SIGNALEMENT(id, statut, adminId)),
};