//  detail agence (stock detaile)

import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { MapPin, Star, Clock, Fuel, ArrowLeft, AlertTriangle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import StockCard from '../../components/cards/StockCard';
import { ROUTES } from '../../constants/routes';
import { formatTime, formatPrice } from '../../utils/formatters';

const AgenceDetailPage = () => {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [agence, setAgence] = useState(null);
  const [stock, setStock] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAgence();
  }, [id]);

  const fetchAgence = async () => {
    try {
      const [agenceRes, stockRes] = await Promise.all([
        axiosInstance.get(`${API.AGENCES.BASE}/${id}`),
        axiosInstance.get(API.STOCK.PUBLIC(id)),
      ]);
      setAgence(agenceRes.data);
      setStock(stockRes.data);
    } catch (error) {
      console.error('Erreur de chargement', error);
    } finally {
      setLoading(false);
    }
  };

  const handleReservation = (categorieId, prix) => {
    if (!user) {
      navigate('/connexion');
      return;
    }
    navigate(ROUTES.CONSUMER_NEW_RESERVATION, {
      state: { agenceId: id, categorieId, prix }
    });
  };

  if (loading) return <Loader className="min-h-[60vh]" />;
  if (!agence) return <p className="text-center py-10 text-gray-500">Agence non trouvée</p>;

  return (
    <div className="space-y-6">
      {/* Retour */}
      <Link to={user ? '/consommateur' : '/'} className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour
      </Link>

      {/* Infos agence */}
      <Card>
        <div className="flex flex-col md:flex-row md:items-start gap-4">
          <div className="flex-1">
            <h1 className="text-2xl font-bold text-gray-900">{agence.nom}</h1>
            <div className="flex items-center gap-2 text-gray-500 mt-1">
              <MapPin size={16} />
              <span>{agence.adresse}, {agence.ville?.nom}</span>
            </div>
            <div className="flex flex-wrap items-center gap-3 mt-2">
              <Badge status={agence.statut === 'ACTIF' ? 'available' : 'inactive'}>
                {agence.statut === 'ACTIF' ? 'Ouvert' : 'Fermé'}
              </Badge>
              <div className="flex items-center gap-1">
                <Star size={16} className="text-yellow-400 fill-yellow-400" />
                <span className="text-sm font-medium">4.8</span>
                <span className="text-sm text-gray-400">(128 avis)</span>
              </div>
              <span className="text-sm text-gray-400">
                {agence.telephone}
              </span>
            </div>
          </div>
          <Button
            variant="outline"
            className="flex items-center gap-2"
            onClick={() => navigate('/signalement/nouveau', { state: { agenceId: id } })}
          >
            <AlertTriangle size={16} /> Signaler
          </Button>
        </div>
      </Card>

      {/* Stock disponible */}
      <h2 className="text-lg font-semibold text-gray-900">Stock disponible</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {stock.map((item) => (
          <StockCard
            key={item.categorieProduitId}
            stock={item}
            onReserve={() => handleReservation(item.categorieProduitId, item.prixUnitaire)}
          />
        ))}
      </div>
    </div>
  );
};

export default AgenceDetailPage;