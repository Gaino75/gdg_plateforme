// fetch notification

import { useState, useEffect } from 'react';
import axiosInstance from '../services/axiosInstance';
import { API } from '../constants/api';

export const useNotifications = (userId) => {
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (userId) {
      fetchNotifications();
    }
  }, [userId]);

  const fetchNotifications = async () => {
    try {
      const [notifsRes, countRes] = await Promise.all([
        axiosInstance.get(API.NOTIFICATIONS.USER(userId)),
        axiosInstance.get(API.NOTIFICATIONS.USER_UNREAD_COUNT(userId)),
      ]);
      setNotifications(notifsRes.data);
      setUnreadCount(countRes.data?.count || 0);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur de chargement des notifications');
    } finally {
      setLoading(false);
    }
  };

  const markAsRead = async (id) => {
    try {
      await axiosInstance.put(API.NOTIFICATIONS.READ(id));
      fetchNotifications();
    } catch (err) {
      console.error('Erreur lors du marquage comme lu', err);
    }
  };

  const markAllAsRead = async () => {
    try {
      await axiosInstance.put(API.NOTIFICATIONS.READ_ALL(userId));
      fetchNotifications();
    } catch (err) {
      console.error('Erreur lors du marquage tout comme lu', err);
    }
  };

  return { notifications, unreadCount, loading, error, markAsRead, markAllAsRead, refetch: fetchNotifications };
};