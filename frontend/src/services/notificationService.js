// Notifications, abonnements

import axiosInstance from './axiosInstance';
import { API } from '../constants/api';

export const notificationService = {
  // Notifications
  getUserNotifications: (userId) => axiosInstance.get(API.NOTIFICATIONS.USER(userId)),
  getUnread: (userId) => axiosInstance.get(API.NOTIFICATIONS.USER_UNREAD(userId)),
  getUnreadCount: (userId) => axiosInstance.get(API.NOTIFICATIONS.USER_UNREAD_COUNT(userId)),
  markAsRead: (id) => axiosInstance.put(API.NOTIFICATIONS.READ(id)),
  markAllAsRead: (userId) => axiosInstance.put(API.NOTIFICATIONS.READ_ALL(userId)),

  // Abonnements
  getAbonnements: (consommateurId) => axiosInstance.get(API.NOTIFICATIONS.ABONNEMENTS_CONSOMMATEUR(consommateurId)),
  createAbonnement: (data) => axiosInstance.post(API.NOTIFICATIONS.ABONNEMENTS, data),
  deleteAbonnement: (consommateurId, agenceId) => 
    axiosInstance.delete(`${API.NOTIFICATIONS.ABONNEMENTS}?consommateurId=${consommateurId}&agenceId=${agenceId}`),

  // Signalements
  getSignalements: () => axiosInstance.get(API.NOTIFICATIONS.SIGNALEMENTS),
  getSignalementsAgence: (agenceId) => axiosInstance.get(API.NOTIFICATIONS.SIGNALEMENTS_AGENCE(agenceId)),
  createSignalement: (data) => axiosInstance.post(API.NOTIFICATIONS.SIGNALEMENTS, data),
  traiterSignalement: (id, statut, adminId) => 
    axiosInstance.put(API.NOTIFICATIONS.TRAITER_SIGNALEMENT(id, statut, adminId)),
};