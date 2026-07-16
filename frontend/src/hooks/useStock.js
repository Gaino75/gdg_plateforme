// fetch stock avec etat

import { useState, useEffect } from 'react';
import axiosInstance from '../services/axiosInstance';
import { API } from '../constants/api';

export const useStock = (agenceId) => {
  const [stock, setStock] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (agenceId) {
      fetchStock();
    }
  }, [agenceId]);

  const fetchStock = async () => {
    try {
      const response = await axiosInstance.get(API.STOCK.AGENCE(agenceId));
      setStock(response.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur de chargement du stock');
    } finally {
      setLoading(false);
    }
  };

  return { stock, loading, error, refetch: fetchStock };
};