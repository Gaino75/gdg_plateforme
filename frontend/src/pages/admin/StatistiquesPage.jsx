// Statistiques globales

import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Loader from '../../components/ui/Loader';
import { formatPrice, formatNumber } from '../../utils/formatters';

const StatistiquesPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState(null);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const response = await axiosInstance.get(API.ADMIN.STATISTIQUES);
      setStats(response.data);
    } catch (error) {
      console.error('Erreur', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Statistiques globales</h1>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <Card className="text-center">
          <div className="text-2xl font-bold text-[#1E3A5F]">{formatNumber(stats?.totalAgences || 0)}</div>
          <p className="text-sm text-gray-500">Agences</p>
        </Card>
        <Card className="text-center">
          <div className="text-2xl font-bold text-[#FF6B35]">{formatNumber(stats?.totalUtilisateurs || 0)}</div>
          <p className="text-sm text-gray-500">Utilisateurs</p>
        </Card>
        <Card className="text-center">
          <div className="text-2xl font-bold text-[#2ECC71]">{formatNumber(stats?.ventesTotal || 0)}</div>
          <p className="text-sm text-gray-500">Ventes</p>
        </Card>
        <Card className="text-center">
          <div className="text-2xl font-bold text-[#1E3A5F]">{formatPrice(stats?.caTotal || 0)}</div>
          <p className="text-sm text-gray-500">CA total</p>
        </Card>
      </div>
    </div>
  );
};

export default StatistiquesPage;