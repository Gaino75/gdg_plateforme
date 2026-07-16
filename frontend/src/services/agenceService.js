// CRUD originAgentCluster, enseigne, villes

import axiosInstance from './axiosInstance';
import { API } from '../constants/api';

export const agenceService = {
  // Enseignes
  getAllEnseignes: () => axiosInstance.get(API.AGENCES.ENSEIGNES),
  getEnseigneById: (id) => axiosInstance.get(`${API.AGENCES.ENSEIGNES}/${id}`),
  createEnseigne: (data) => axiosInstance.post(API.AGENCES.ENSEIGNES, data),
  updateEnseigne: (id, data) => axiosInstance.put(`${API.AGENCES.ENSEIGNES}/${id}`, data),
  deleteEnseigne: (id) => axiosInstance.delete(`${API.AGENCES.ENSEIGNES}/${id}`),

  // Villes
  getAllVilles: () => axiosInstance.get(API.AGENCES.VILLES),
  getVilleById: (id) => axiosInstance.get(`${API.AGENCES.VILLES}/${id}`),
  createVille: (data) => axiosInstance.post(API.AGENCES.VILLES, data),
  updateVille: (id, data) => axiosInstance.put(`${API.AGENCES.VILLES}/${id}`, data),
  deleteVille: (id) => axiosInstance.delete(`${API.AGENCES.VILLES}/${id}`),

  // Agences
  getAllAgences: () => axiosInstance.get(API.AGENCES.BASE),
  getAgencesActives: () => axiosInstance.get(API.AGENCES.ACTIVES),
  getAgencesEnAttente: () => axiosInstance.get(API.AGENCES.EN_ATTENTE),
  getAgenceById: (id) => axiosInstance.get(`${API.AGENCES.BASE}/${id}`),
  createAgence: (data) => axiosInstance.post(API.AGENCES.DISTRIBUTEUR, data),
  updateAgence: (id, data) => axiosInstance.put(`${API.AGENCES.BASE}/${id}`, data),
  validerAgence: (id, adminId) => axiosInstance.put(API.AGENCES.VALIDER(id, adminId)),
  rejeterAgence: (id) => axiosInstance.put(API.AGENCES.REJETER(id)),
  suspendreAgence: (id) => axiosInstance.put(API.AGENCES.SUSPENDRE(id)),
  reactiverAgence: (id) => axiosInstance.put(API.AGENCES.REACTIVER(id)),
  getStatistiques: () => axiosInstance.get(API.AGENCES.STATISTIQUES),
};