//gestion auth + roles

/*
import { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('gpg_user');
    return saved ? JSON.parse(saved) : null;
  });

  const login = (token, userData) => {
    localStorage.setItem('gpg_token', token);
    localStorage.setItem('gpg_user', JSON.stringify(userData));
    setUser(userData);
  };

  const logout = () => {
    localStorage.removeItem('gpg_token');
    localStorage.removeItem('gpg_user');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);

*/

/* Version Final */


import React, { createContext, useState, useContext, useEffect } from 'react';
import axiosInstance from '../services/axiosInstance';
import { API } from '../constants/api';
import { ROLES } from '../constants/roles';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('gdg_user');
    return saved ? JSON.parse(saved) : null;
  });
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(localStorage.getItem('gdg_token'));

  useEffect(() => {
    if (token && !user) {
      loadUser();
    } else {
      setLoading(false);
    }
  }, [token]);

  const loadUser = async () => {
    try {
      const response = await axiosInstance.get(API.AUTH.PROFIL);
      setUser(response.data);
      localStorage.setItem('gdg_user', JSON.stringify(response.data));
    } catch (error) {
      localStorage.removeItem('gdg_token');
      localStorage.removeItem('gdg_user');
      setToken(null);
    } finally {
      setLoading(false);
    }
  };

  const login = async (email, motDePasse) => {
    const response = await axiosInstance.post(API.AUTH.LOGIN, { email, motDePasse });
    const { token, ...userData } = response.data;
    
    localStorage.setItem('gdg_token', token);
    localStorage.setItem('gdg_user', JSON.stringify(userData));
    setToken(token);
    setUser(userData);
    
    return userData;
  };

  const logout = async () => {
    try {
      await axiosInstance.post(API.AUTH.LOGOUT);
    } catch (error) {
      // Ignorer les erreurs de déconnexion
    }
    localStorage.removeItem('gdg_token');
    localStorage.removeItem('gdg_user');
    setToken(null);
    setUser(null);
  };

  const hasRole = (role) => user?.role === role;
  const isAuthenticated = !!user;

  const value = {
    user,
    loading,
    token,
    login,
    logout,
    hasRole,
    isAuthenticated,
    isConsumer: hasRole(ROLES.CONSOMMATEUR),
    isDistributor: hasRole(ROLES.DISTRIBUTEUR),
    isAdmin: hasRole(ROLES.ADMIN),
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth doit être utilisé dans un AuthProvider');
  }
  return context;
};


/*

// ============================================================
// VERSION DE TEST AVEC UTILISATEUR FICTIF (ACTIVE)
// ============================================================

import React, { createContext, useState, useContext, useEffect } from 'react';
import axiosInstance from '../services/axiosInstance';
import { API } from '../constants/api';
import { ROLES } from '../constants/roles';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  // ============================================================
  // UTILISATEUR FICTIF POUR LE TEST - ACTIF
  // ============================================================
  const [user, setUser] = useState(() => {
    // Pour le développement, créer un utilisateur fictif
    // Change le rôle ici pour tester différents profils :
    // ROLES.CONSOMMATEUR, ROLES.DISTRIBUTEUR, ROLES.ADMIN
    if (import.meta.env.DEV) {
      return {
        id: 1,
        nom: 'Kamdem',
        prenom: 'Éric',
        email: 'eric@test.com',
        telephone: '+237 677 12 34 56',
        // Consommateur
        role: ROLES.CONSOMMATEUR,  // ← Change ici pour tester les rôles
        //Distributeur
        //role: ROLES.DISTRIBUTEUR,
        // Admin	
        //role: ROLES.ADMIN,
        agenceId: 1,
        dateInscription: '2026-01-01T00:00:00',
      };
    }
    // En production, récupérer depuis localStorage
    const saved = localStorage.getItem('gdg_user');
    return saved ? JSON.parse(saved) : null;
  });

  const [loading, setLoading] = useState(false);
  const [token, setToken] = useState(localStorage.getItem('gdg_token'));

  useEffect(() => {
    if (token && !user) {
      loadUser();
    } else {
      setLoading(false);
    }
  }, [token]);

  const loadUser = async () => {
    try {
      const response = await axiosInstance.get(API.AUTH.PROFIL);
      setUser(response.data);
      localStorage.setItem('gdg_user', JSON.stringify(response.data));
    } catch (error) {
      localStorage.removeItem('gdg_token');
      localStorage.removeItem('gdg_user');
      setToken(null);
    } finally {
      setLoading(false);
    }
  };

  const login = async (email, motDePasse) => {
    const response = await axiosInstance.post(API.AUTH.LOGIN, { email, motDePasse });
    const { token, ...userData } = response.data;
    
    localStorage.setItem('gdg_token', token);
    localStorage.setItem('gdg_user', JSON.stringify(userData));
    setToken(token);
    setUser(userData);
    
    return userData;
  };

  const logout = async () => {
    try {
      await axiosInstance.post(API.AUTH.LOGOUT);
    } catch (error) {
      // Ignorer les erreurs de déconnexion
    }
    localStorage.removeItem('gdg_token');
    localStorage.removeItem('gdg_user');
    setToken(null);
    setUser(null);
  };

  const hasRole = (role) => user?.role === role;
  const isAuthenticated = !!user;

  const value = {
    user,
    loading,
    token,
    login,
    logout,
    hasRole,
    isAuthenticated,
    isConsumer: hasRole(ROLES.CONSOMMATEUR),
    isDistributor: hasRole(ROLES.DISTRIBUTEUR),
    isAdmin: hasRole(ROLES.ADMIN),
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth doit être utilisé dans un AuthProvider');
  }
  return context;
};
*/