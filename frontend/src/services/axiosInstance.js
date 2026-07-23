// config axios + intercepteurs jwt
/*
import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', // la Gateway, jamais un microservice direct
});

// Ajoute automatiquement le token sur CHAQUE requête
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem('gpg_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Si le token est invalide/expiré (401), on déconnecte proprement
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('gpg_token');
      localStorage.removeItem('gpg_user');
      window.location.href = '/connexion';
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;

*/

import axios from 'axios';

// const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';//cas en local
const API_URL = import.meta.env.VITE_API_URL || '';
const axiosInstance = axios.create({
  baseURL: API_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Intercepteur pour ajouter le token JWT
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('gdg_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Intercepteur pour gérer les erreurs
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      const token = localStorage.getItem('gdg_token');
      localStorage.removeItem('gdg_token');
      localStorage.removeItem('gdg_user');
      window.location.href = '/connexion';
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;