// Tableau de bord global

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Store, Users, AlertTriangle, CreditCard, Package, TrendingUp } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Loader from '../../components/ui/Loader';
import Badge from '../../components/ui/Badge';
import { ROUTES } from '../../constants/routes';
import { formatNumber, formatDateTime } from '../../utils/formatters';

const DashboardPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    agences: 0,
    utilisateurs: 0,
    signalements: 0,
    paiements: 0,
    ventesJour: 0,
    caJour: 0,
  });
  const [demandes, setDemandes] = useState([]);
  const [alertes, setAlertes] = useState([]);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      // Dashboard
      const dashRes = await axiosInstance.get(API.ADMIN.DASHBOARD);
      const data = dashRes.data;

      setStats({
        agences: data.nbAgences || 0,
        utilisateurs: data.nbUtilisateurs || 0,
        signalements: data.nbSignalements || 0,
        paiements: data.nbPaiementsAVerifier || 0,
        ventesJour: data.ventesJour || 0,
        caJour: data.caJour || 0,
      });

      setDemandes(data.demandesEnAttente || []);
      setAlertes(data.alertesStock || []);

    } catch (error) {
      console.error('Erreur de chargement du dashboard', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Tableau de bord</h1>
        <p className="text-gray-500">Vue d'ensemble de la plateforme GDG</p>
      </div>

      {/* Statistiques */}
      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
        <Card className="text-center">
          <div className="text-2xl font-bold text-[#1E3A5F]">{formatNumber(stats.agences)}</div>
          <p className="text-sm text-gray-500">Agences</p>
        </Card>
        <Card className="text-center">
          <div className="text-2xl font-bold text-[#1E3A5F]">{formatNumber(stats.utilisateurs)}</div>
          <p className="text-sm text-gray-500">Utilisateurs</p>
        </Card>
        <Card className="text-center border-orange-200">
          <div className="text-2xl font-bold text-[#FF6B35]">{formatNumber(stats.signalements)}</div>
          <p className="text-sm text-gray-500">Signalements</p>
        </Card>
        <Card className="text-center border-yellow-200">
          <div className="text-2xl font-bold text-yellow-500">{formatNumber(stats.paiements)}</div>
          <p className="text-sm text-gray-500">Paiements à vérifier</p>
        </Card>
        <Card className="text-center border-green-200">
          <div className="text-2xl font-bold text-[#2ECC71]">{formatNumber(stats.ventesJour)}</div>
          <p className="text-sm text-gray-500">Ventes du jour</p>
        </Card>
        <Card className="text-center border-blue-200">
          <div className="text-2xl font-bold text-blue-500">{stats.caJour} FCFA</div>
          <p className="text-sm text-gray-500">CA du jour</p>
        </Card>
      </div>

      {/* Demandes en attente */}
      {demandes.length > 0 && (
        <div>
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Demandes en attente</h2>
          <div className="space-y-2">
            {demandes.slice(0, 3).map((d) => (
              <Card key={d.id} className="flex items-center justify-between">
                <div>
                  <p className="font-medium text-gray-900">{d.nomAgence}</p>
                  <p className="text-sm text-gray-500">{d.nomEnseigne} · {d.nomVille}</p>
                </div>
                <div className="flex gap-2">
                  <Button variant="success" size="sm">Valider</Button>
                  <Button variant="danger" size="sm">Rejeter</Button>
                </div>
              </Card>
            ))}
          </div>
          {demandes.length > 3 && (
            <Link to={ROUTES.ADMIN_DEMANDES} className="text-sm text-[#FF6B35] hover:underline mt-2 block">
              Voir toutes les demandes ({demandes.length})
            </Link>
          )}
        </div>
      )}

      {/* Alertes stock */}
      {alertes.length > 0 && (
        <div>
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Alertes stock critiques</h2>
          <div className="space-y-2">
            {alertes.slice(0, 3).map((a, index) => (
              <div key={index} className="bg-red-50 border border-red-200 p-4 rounded-lg">
                <p className="text-sm text-red-800">
                  <strong>{a.agenceNom}</strong> — {a.categorie} : {a.quantite} unités restantes
                </p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Actions rapides */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Link to={ROUTES.ADMIN_DEMANDES}>
          <Button variant="orange" fullWidth className="gap-2">
            <Store size={18} /> Valider des agences
          </Button>
        </Link>
        <Link to={ROUTES.ADMIN_SIGNALEMENTS}>
          <Button variant="outline" fullWidth className="gap-2">
            <AlertTriangle size={18} /> Traiter signalements
          </Button>
        </Link>
        <Link to={ROUTES.ADMIN_PAIEMENTS}>
          <Button variant="outline" fullWidth className="gap-2">
            <CreditCard size={18} /> Vérifier paiements
          </Button>
        </Link>
        <Link to={ROUTES.ADMIN_STOCKS}>
          <Button variant="outline" fullWidth className="gap-2">
            <Package size={18} /> Superviser stocks
          </Button>
        </Link>
      </div>
    </div>
  );
};

export default DashboardPage;