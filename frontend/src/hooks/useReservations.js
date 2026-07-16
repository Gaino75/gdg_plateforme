// Fetch reservations

import { useState, useEffect } from 'react';
import axiosInstance from '../services/axiosInstance';
import { API } from '../constants/api';

export const useReservations = (consommateurId) => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (consommateurId) {
      fetchReservations();
    }
  }, [consommateurId]);

  const fetchReservations = async () => {
    try {
      const response = await axiosInstance.get(API.RESERVATIONS.CONSOMMATEUR(consommateurId));
      setReservations(response.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur de chargement des réservations');
    } finally {
      setLoading(false);
    }
  };

  return { reservations, loading, error, refetch: fetchReservations };
};