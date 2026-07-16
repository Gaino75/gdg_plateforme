// Toutes les agences

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search, Plus, Eye, Ban, CheckCircle, XCircle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import ConfirmDialog from '../../components/ui/ConfirmDialog';
import { formatDateTime } from '../../utils/formatters';

const AgencesPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [agences, setAgences] = useState([]);
  const [filter, setFilter] = useState('all');
  const [search, setSearch] = useState('');
  const [showConfirm, setShowConfirm] = useState(null);

  useEffect(() => {
    fetchAgences();
  }, []);

  const fetchAgences = async () => {
    try {
      const response = await axiosInstance.get(API.AGENCES.BASE);
      setAgences(response.data || []);
    } catch (error) {
      console.error('Erreur de chargement des agences', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredAgences = agences.filter(a => {
    const matchFilter = filter === 'all' || a.statut === filter;
    const matchSearch = a.nom?.toLowerCase().includes(search.toLowerCase()) ||
                        a.ville?.nom?.toLowerCase().includes(search.toLowerCase());
    return matchFilter && matchSearch;
  });

  const handleAction = async (id, action) => {
    try {
      if (action === 'valider') {
        await axiosInstance.put(API.AGENCES.VALIDER(id, user.id));
      } else if (action === 'suspendre') {
        await axiosInstance.put(API.AGENCES.SUSPENDRE(id));
      } else if (action === 'reactiver') {
        await axiosInstance.put(API.AGENCES.REACTIVER(id));
      }
      await fetchAgences();
    } catch (error) {
      console.error('Erreur', error);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between flex-wrap gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Gestion des agences</h1>
          <p className="text-gray-500">{agences.length} agences au total</p>
        </div>
      </div>

      {/* Filtres */}
      <div className="flex flex-wrap items-center gap-3">
        <div className="flex-1 min-w-[200px]">
          <Input
            placeholder="Rechercher une agence..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            icon={Search}
          />
        </div>
        <div className="flex gap-2">
          {['all', 'EN_ATTENTE', 'ACTIF', 'SUSPENDU'].map((f) => (
            <button
              key={f}
              onClick={() => setFilter(f)}
              className={`px-3 py-1.5 rounded-full text-sm font-medium transition-colors ${
                filter === f
                  ? 'bg-[#1E3A5F] text-white'
                  : 'bg-white border border-gray-200 text-gray-600 hover:bg-gray-50'
              }`}
            >
              {f === 'all' ? 'Toutes' : f.replace('_', ' ')}
            </button>
          ))}
        </div>
      </div>

      {/* Liste */}
      <div className="space-y-3">
        {filteredAgences.map((a) => (
          <Card key={a.id} className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
            <div>
              <div className="flex items-center gap-2">
                <span className="font-semibold text-gray-900">{a.nom}</span>
                <Badge status={
                  a.statut === 'ACTIF' ? 'available' :
                  a.statut === 'SUSPENDU' ? 'suspended' : 'pending'
                }>
                  {a.statut === 'ACTIF' ? 'Active' :
                   a.statut === 'SUSPENDU' ? 'Suspendue' : 'En attente'}
                </Badge>
              </div>
              <p className="text-sm text-gray-500">
                {a.enseigne?.nom} · {a.ville?.nom}
              </p>
              <p className="text-xs text-gray-400">
                Créée le {formatDateTime(a.dateCreation)}
                {a.dateValidation && ` · Validée le ${formatDateTime(a.dateValidation)}`}
              </p>
            </div>
            <div className="flex flex-wrap gap-2">
              {a.statut === 'EN_ATTENTE' && (
                <>
                  <Button variant="success" size="sm" className="gap-1" onClick={() => handleAction(a.id, 'valider')}>
                    <CheckCircle size={14} /> Valider
                  </Button>
                  <Button variant="danger" size="sm" className="gap-1">
                    <XCircle size={14} /> Rejeter
                  </Button>
                </>
              )}
              {a.statut === 'ACTIF' && (
                <Button variant="danger" size="sm" className="gap-1" onClick={() => handleAction(a.id, 'suspendre')}>
                  <Ban size={14} /> Suspendre
                </Button>
              )}
              {a.statut === 'SUSPENDU' && (
                <Button variant="success" size="sm" className="gap-1" onClick={() => handleAction(a.id, 'reactiver')}>
                  <CheckCircle size={14} /> Réactiver
                </Button>
              )}
              <Button variant="outline" size="sm" className="gap-1" onClick={() => navigate(`/admin/agence/${a.id}`)}>
                <Eye size={14} /> Voir
              </Button>
            </div>
          </Card>
        ))}
        {filteredAgences.length === 0 && (
          <p className="text-center py-10 text-gray-500">Aucune agence trouvée</p>
        )}
      </div>

      <ConfirmDialog
        isOpen={!!showConfirm}
        onClose={() => setShowConfirm(null)}
        onConfirm={() => {
          // handle action
          setShowConfirm(null);
        }}
        title="Confirmer l'action"
        message="Êtes-vous sûr de vouloir effectuer cette action ?"
      />
    </div>
  );
};

export default AgencesPage;