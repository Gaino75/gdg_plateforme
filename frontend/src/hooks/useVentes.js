// fetch ventes

import { useState, useEffect } from 'react';
import axiosInstance from '../services/axiosInstance';
import { API } from '../constants/api';

export const useVentes = (agenceId) => {
  const [ventes, setVentes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (agenceId) {
      fetchVentes();
    }
  }, [agenceId]);

  const fetchVentes = async () => {
    try {
      const response = await axiosInstance.get(API.VENTES.AGENCE(agenceId));
      setVentes(response.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur de chargement des ventes');
    } finally {
      setLoading(false);
    }
  };

  return { ventes, loading, error, refetch: fetchVentes };
};