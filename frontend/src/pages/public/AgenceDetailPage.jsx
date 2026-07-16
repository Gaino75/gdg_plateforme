// detail agence (stock public )

import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { ArrowLeft, MapPin, Star, Clock, Fuel, AlertTriangle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import StockCard from '../../components/cards/StockCard';
import { ROUTES } from '../../constants/routes';

const AgenceDetailPage = () => {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [agence, setAgence] = useState(null);
  const [stock, setStock] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, [id]);

  const fetchData = async () => {
    try {
      const [agenceRes, stockRes] = await Promise.all([
        axiosInstance.get(`${API.AGENCES.BASE}/${id}`),
        axiosInstance.get(API.STOCK.PUBLIC(id)),
      ]);
      setAgence(agenceRes.data);
      setStock(stockRes.data);
    } catch (error) {
      console.error('Erreur', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;
  if (!agence) return <p className="text-center py-10 text-gray-500">Agence non trouvée</p>;

  return (
    <div className="space-y-6">
      <Link to="/" className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour
      </Link>
      <Card>
        <div className="flex flex-col md:flex-row md:items-start justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">{agence.nom}</h1>
            <div className="flex items-center gap-2 text-gray-500 mt-1">
              <MapPin size={16} /> {agence.adresse}, {agence.ville?.nom}
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
            </div>
          </div>
          <Button variant="outline" className="flex items-center gap-2" onClick={() => navigate('/signalement/nouveau')}>
            <AlertTriangle size={16} /> Signaler
          </Button>
        </div>
      </Card>
      <h2 className="text-lg font-semibold text-gray-900">Stock disponible</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {stock.map((item) => (
          <StockCard key={item.categorieProduitId} stock={item} />
        ))}
      </div>
    </div>
  );
};

export default AgenceDetailPage;