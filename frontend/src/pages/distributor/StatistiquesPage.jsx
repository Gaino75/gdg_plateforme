// Statistiques de mon agence 

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
  const [ventesParJour, setVentesParJour] = useState([]);
  const [ventesParCategorie, setVentesParCategorie] = useState([]);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      // Ventes de l'agence
      const ventesRes = await axiosInstance.get(API.VENTES.AGENCE(user.agenceId));
      const ventes = ventesRes.data || [];

      // Stats globales
      const total = ventes.reduce((sum, v) => sum + v.prixTotal, 0);
      const totalVentes = ventes.length;

      // Ventes par catégorie
      const parCategorie = ventes.reduce((acc, v) => {
        const key = v.categorieProduitId || 'Autre';
        acc[key] = (acc[key] || 0) + v.prixTotal;
        return acc;
      }, {});
      const categoriesList = Object.entries(parCategorie).map(([label, montant]) => ({ label, montant }));

      // Ventes des 7 derniers jours
      const jours = Array.from({ length: 7 }, (_, i) => {
        const d = new Date();
        d.setDate(d.getDate() - i);
        return d.toISOString().split('T')[0];
      }).reverse();

      const parJour = jours.map(jour => {
        const jourVentes = ventes.filter(v => v.dateVente?.startsWith(jour));
        return {
          jour,
          total: jourVentes.reduce((sum, v) => sum + v.prixTotal, 0),
          count: jourVentes.length,
        };
      });

      setStats({ total, totalVentes });
      setVentesParJour(parJour);
      setVentesParCategorie(categoriesList);

    } catch (error) {
      console.error('Erreur de chargement des statistiques', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Statistiques</h1>
        <p className="text-gray-500">Analyse des ventes de votre agence</p>
      </div>

      {/* KPI */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card className="text-center">
          <p className="text-3xl font-bold text-[#1E3A5F]">{formatPrice(stats?.total || 0)}</p>
          <p className="text-sm text-gray-500">Chiffre d'affaires total</p>
        </Card>
        <Card className="text-center">
          <p className="text-3xl font-bold text-[#FF6B35]">{formatNumber(stats?.totalVentes || 0)}</p>
          <p className="text-sm text-gray-500">Ventes totales</p>
        </Card>
        <Card className="text-center">
          <p className="text-3xl font-bold text-[#2ECC71]">
            {ventesParJour.length > 0 ? formatPrice(ventesParJour[ventesParJour.length - 1]?.total || 0) : '0 FCFA'}
          </p>
          <p className="text-sm text-gray-500">Aujourd'hui</p>
        </Card>
      </div>

      {/* Ventes par jour */}
      <Card>
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Ventes des 7 derniers jours</h2>
        <div className="space-y-2">
          {ventesParJour.map((item) => (
            <div key={item.jour} className="flex items-center justify-between py-2 border-b border-gray-100">
              <span className="text-sm text-gray-600">
                {new Date(item.jour).toLocaleDateString('fr-FR', { weekday: 'short', day: '2-digit', month: 'short' })}
              </span>
              <div className="flex items-center gap-4">
                <span className="text-sm text-gray-500">{item.count} vente{item.count > 1 ? 's' : ''}</span>
                <span className="font-medium text-[#1E3A5F]">{formatPrice(item.total)}</span>
              </div>
            </div>
          ))}
        </div>
      </Card>

      {/* Ventes par catégorie */}
      <Card>
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Ventes par catégorie</h2>
        <div className="space-y-2">
          {ventesParCategorie.map((item) => {
            const percentage = stats?.total > 0 ? (item.montant / stats.total) * 100 : 0;
            return (
              <div key={item.label} className="space-y-1">
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">{item.label}</span>
                  <span className="font-medium">{formatPrice(item.montant)}</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div
                    className="bg-[#1E3A5F] h-2 rounded-full"
                    style={{ width: `${Math.min(percentage, 100)}%` }}
                  />
                </div>
              </div>
            );
          })}
        </div>
      </Card>
    </div>
  );
};

export default StatistiquesPage;