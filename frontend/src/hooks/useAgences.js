// fetch agences avec etat

import { useState, useEffect } from 'react';
import axiosInstance from '../services/axiosInstance';
import { API } from '../constants/api';

export const useAgences = () => {
  const [agences, setAgences] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchAgences();
  }, []);

  const fetchAgences = async () => {
    try {
      const response = await axiosInstance.get(API.AGENCES.ACTIVES);
      setAgences(response.data);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur de chargement');
    } finally {
      setLoading(false);
    }
  };

  return { agences, loading, error, refetch: fetchAgences };
};