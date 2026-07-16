// Signalements consommateurs

import React, { useState, useEffect } from 'react';
import { AlertTriangle, CheckCircle, XCircle, Search } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { formatDateTime } from '../../utils/formatters';

const SignalementsPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [signalements, setSignalements] = useState([]);
  const [search, setSearch] = useState('');
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    fetchSignalements();
  }, []);

  const fetchSignalements = async () => {
    try {
      const response = await axiosInstance.get(API.NOTIFICATIONS.SIGNALEMENTS);
      setSignalements(response.data || []);
    } catch (error) {
      console.error('Erreur de chargement des signalements', error);
    } finally {
      setLoading(false);
    }
  };

  const handleTraiter = async (id, statut) => {
    try {
      await axiosInstance.put(API.NOTIFICATIONS.TRAITER_SIGNALEMENT(id, statut, user.id));
      await fetchSignalements();
    } catch (error) {
      console.error('Erreur de traitement', error);
    }
  };

  const filteredSignalements = signalements.filter(s => {
    const matchFilter = filter === 'all' || s.statut === filter;
    const matchSearch = s.agenceId?.toString().includes(search) ||
                        s.typeSignalement?.toLowerCase().includes(search.toLowerCase());
    return matchFilter && matchSearch;
  });

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Signalements</h1>
        <p className="text-gray-500">Validez ou rejetez les signalements des consommateurs</p>
      </div>

      <div className="flex flex-wrap items-center gap-3">
        <div className="flex-1 min-w-[200px]">
          <Input
            placeholder="Rechercher..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            icon={Search}
          />
        </div>
        <div className="flex gap-2">
          {['all', 'EN_ATTENTE', 'CONFIRME', 'REJETE'].map((f) => (
            <button
              key={f}
              onClick={() => setFilter(f)}
              className={`px-3 py-1.5 rounded-full text-sm font-medium transition-colors ${
                filter === f
                  ? 'bg-[#1E3A5F] text-white'
                  : 'bg-white border border-gray-200 text-gray-600 hover:bg-gray-50'
              }`}
            >
              {f === 'all' ? 'Tous' : f.replace('_', ' ')}
            </button>
          ))}
        </div>
      </div>

      <div className="space-y-3">
        {filteredSignalements.map((s) => (
          <Card key={s.id} className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
            <div>
              <div className="flex items-center gap-2">
                <span className={`font-semibold ${s.typeSignalement === 'RUPTURE' ? 'text-red-500' : 'text-green-500'}`}>
                  {s.typeSignalement === 'RUPTURE' ? '⚠️ Rupture' : '✅ Disponible'}
                </span>
                <Badge status={
                  s.statut === 'CONFIRME' ? 'confirmed' :
                  s.statut === 'REJETE' ? 'cancelled' : 'pending'
                }>
                  {s.statut === 'CONFIRME' ? 'Confirmé' :
                   s.statut === 'REJETE' ? 'Rejeté' : 'En attente'}
                </Badge>
              </div>
              <p className="text-sm text-gray-600">
                Agence {s.agenceId} · Catégorie {s.categorieProduitId}
              </p>
              <div className="flex items-center gap-3 text-xs text-gray-400">
                <span>Par {s.consommateurId}</span>
                <span>·</span>
                <span>{formatDateTime(s.dateSignalement)}</span>
                {s.commentaire && <span>· "{s.commentaire}"</span>}
              </div>
            </div>
            {s.statut === 'EN_ATTENTE' && (
              <div className="flex gap-2">
                <Button variant="success" size="sm" className="gap-1" onClick={() => handleTraiter(s.id, 'CONFIRME')}>
                  <CheckCircle size={14} /> Confirmer
                </Button>
                <Button variant="danger" size="sm" className="gap-1" onClick={() => handleTraiter(s.id, 'REJETE')}>
                  <XCircle size={14} /> Rejeter
                </Button>
              </div>
            )}
          </Card>
        ))}
        {filteredSignalements.length === 0 && (
          <p className="text-center py-10 text-gray-500">Aucun signalement trouvé</p>
        )}
      </div>
    </div>
  );
};

export default SignalementsPage;