// Tableau de bord (stock + ventes)
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Package, ShoppingCart, Calendar, AlertTriangle, TrendingUp, Store } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Loader from '../../components/ui/Loader';
import StockJauge from '../../components/ui/StockJauge';
import { ROUTES } from '../../constants/routes';
import { formatPrice, formatNumber } from '../../utils/formatters';

const DashboardPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({ ventesJour: 0, caJour: 0, reservations: 0, alertes: 0 });
  const [stock, setStock] = useState([]);
  const [reservations, setReservations] = useState([]);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const agenceId = user?.agenceId || 1;

      // Stock
      const stockRes = await axiosInstance.get(API.STOCK.AGENCE(agenceId));
      setStock(stockRes.data);

      // Ventes du jour
      const ventesRes = await axiosInstance.get(API.VENTES.AGENCE(agenceId));
      const today = new Date().toDateString();
      const ventesToday = ventesRes.data?.filter(v => new Date(v.dateVente).toDateString() === today) || [];
      const totalCa = ventesToday.reduce((sum, v) => sum + v.prixTotal, 0);

      setStats({
        ventesJour: ventesToday.length,
        caJour: totalCa,
        reservations: 3, // À remplacer par un vrai appel
        alertes: stockRes.data?.filter(s => s.quantiteDisponible <= s.seuilCritique).length || 0,
      });

      // Réservations
      const resRes = await axiosInstance.get(API.RESERVATIONS.AGENCE(agenceId));
      setReservations(resRes.data?.filter(r => r.statut === 'EN_ATTENTE').slice(0, 3) || []);

    } catch (error) {
      console.error('Erreur de chargement', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Tableau de bord</h1>
        <p className="text-gray-500">
          {user?.nomAgence || 'Mon agence'} - Aujourd'hui
        </p>
      </div>

      {/* Alertes */}
      {stats.alertes > 0 && (
        <div className="bg-orange-50 border-l-4 border-[#FF6B35] p-4 rounded-lg">
          <div className="flex items-start gap-3">
            <AlertTriangle className="text-[#FF6B35] flex-shrink-0 mt-0.5" size={20} />
            <div>
              <p className="font-medium text-orange-800">
                {stats.alertes} catégorie{stats.alertes > 1 ? 's' : ''} nécessite{stats.alertes > 1 ? 'nt' : ''} votre attention
              </p>
              <p className="text-sm text-orange-600">
                {stock.filter(s => s.quantiteDisponible === 0).map(s => s.libelle).join(', ')} en rupture
                {stock.filter(s => s.quantiteDisponible > 0 && s.quantiteDisponible <= s.seuilCritique).length > 0 && 
                  ` : ${stock.filter(s => s.quantiteDisponible > 0 && s.quantiteDisponible <= s.seuilCritique).map(s => s.libelle).join(', ')} sous le seuil critique`
                }
              </p>
            </div>
          </div>
        </div>
      )}

      {/* Statistiques */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <Card className="text-center">
          <div className="text-2xl font-bold text-[#1E3A5F]">{formatNumber(stats.ventesJour)}</div>
          <p className="text-sm text-gray-500">Ventes du jour</p>
        </Card>
        <Card className="text-center border-green-200">
          <div className="text-2xl font-bold text-[#2ECC71]">{formatPrice(stats.caJour)}</div>
          <p className="text-sm text-gray-500">Chiffre d'affaires</p>
        </Card>
        <Card className="text-center border-orange-200">
          <div className="text-2xl font-bold text-[#FF6B35]">{formatNumber(stats.reservations)}</div>
          <p className="text-sm text-gray-500">Réservations</p>
        </Card>
        <Card className="text-center border-red-200">
          <div className="text-2xl font-bold text-red-500">{formatNumber(stats.alertes)}</div>
          <p className="text-sm text-gray-500">Alertes actives</p>
        </Card>
      </div>

      {/* Stock */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-gray-900">État du stock</h2>
          <Link to={ROUTES.DISTRIBUTOR_STOCK} className="text-sm text-[#FF6B35] hover:underline">
            Gérer le stock
          </Link>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {stock.map((s) => (
            <StockJauge
              key={s.categorieProduitId}
              label={s.libelle}
              quantity={s.quantiteDisponible}
              seuil={s.seuilCritique}
            />
          ))}
        </div>
      </div>

      {/* Actions */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Link to={ROUTES.DISTRIBUTOR_VENTE}>
          <Button variant="orange" fullWidth size="lg" className="gap-2">
            <ShoppingCart size={18} /> Enregistrer une vente
          </Button>
        </Link>
        <Link to={ROUTES.DISTRIBUTOR_APPROVISIONNEMENT}>
          <Button variant="primary" fullWidth size="lg" className="gap-2">
            <Package size={18} /> Approvisionner
          </Button>
        </Link>
        <Link to={ROUTES.DISTRIBUTOR_RESERVATIONS}>
          <Button variant="outline" fullWidth size="lg" className="gap-2">
            <Calendar size={18} /> Gérer les réservations
          </Button>
        </Link>
      </div>

      {/* Réservations reçues */}
      {reservations.length > 0 && (
        <div>
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Réservations reçues</h2>
          <div className="space-y-2">
            {reservations.map((r) => (
              <Card key={r.id} className="flex items-center justify-between">
                <div>
                  <p className="font-medium text-gray-900">
                    {r.consommateurId} — {r.categorieProduitId} × {r.quantite}
                  </p>
                  <p className="text-sm text-gray-500">{r.referenceReservation}</p>
                </div>
                <Badge status="pending">En attente</Badge>
              </Card>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default DashboardPage;