// Demandes d'inscription en attente

import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { formatDateTime } from '../../utils/formatters';

const DemandesPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [demandes, setDemandes] = useState([]);

  useEffect(() => {
    fetchDemandes();
  }, []);

  const fetchDemandes = async () => {
    try {
      const response = await axiosInstance.get(API.ADMIN.DEMANDES);
      setDemandes(response.data || []);
    } catch (error) {
      console.error('Erreur', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAction = async (id, action) => {
    try {
      if (action === 'valider') {
        await axiosInstance.put(API.AGENCES.VALIDER(id, user.id));
      } else {
        await axiosInstance.put(API.AGENCES.REJETER(id));
      }
      await fetchDemandes();
    } catch (error) {
      console.error('Erreur', error);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Demandes d'inscription</h1>
      <p className="text-gray-500">Validez ou rejetez les demandes des distributeurs</p>

      <div className="space-y-3">
        {demandes.map((d) => (
          <Card key={d.id} className="flex flex-col md:flex-row items-start md:items-center justify-between gap-4">
            <div>
              <p className="font-semibold text-gray-900">{d.nomAgence}</p>
              <p className="text-sm text-gray-500">{d.nomEnseigne} · {d.nomVille}</p>
              <p className="text-sm text-gray-400">Demandé par {d.nomResponsable} - {formatDateTime(d.dateDemande)}</p>
            </div>
            <div className="flex gap-2">
              <Button variant="success" size="sm" onClick={() => handleAction(d.id, 'valider')}>Valider</Button>
              <Button variant="danger" size="sm" onClick={() => handleAction(d.id, 'rejeter')}>Rejeter</Button>
            </div>
          </Card>
        ))}
        {demandes.length === 0 && <p className="text-center py-10 text-gray-500">Aucune demande en attente</p>}
      </div>
    </div>
  );
};

export default DemandesPage;