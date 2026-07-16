// Gestion enseignes

import React, { useState, useEffect } from 'react';
import { Plus, Edit, Trash2 } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { formatDateTime } from '../../utils/formatters';

const EnseignesPage = () => {
  const [loading, setLoading] = useState(true);
  const [enseignes, setEnseignes] = useState([]);

  useEffect(() => {
    fetchEnseignes();
  }, []);

  const fetchEnseignes = async () => {
    try {
      const response = await axiosInstance.get(API.AGENCES.ENSEIGNES);
      setEnseignes(response.data || []);
    } catch (error) {
      console.error('Erreur', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Enseignes</h1>
          <p className="text-gray-500">Gestion des compagnies partenaires</p>
        </div>
        <Button variant="orange" className="gap-2"><Plus size={18} /> Ajouter</Button>
      </div>

      <div className="space-y-3">
        {enseignes.map((e) => (
          <Card key={e.id} className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              {e.logo ? (
                <img src={e.logo} alt={e.nom} className="h-12 w-12 object-contain" />
              ) : (
                <div className="h-12 w-12 bg-gray-200 rounded-full flex items-center justify-center font-bold text-lg">
                  {e.nom?.[0]}
                </div>
              )}
              <div>
                <p className="font-semibold text-gray-900">{e.nom}</p>
                <p className="text-sm text-gray-500">{e.description}</p>
                <p className="text-xs text-gray-400">{e.agences?.length || 0} agences</p>
              </div>
            </div>
            <div className="flex items-center gap-2">
              <Badge status={e.statut === 'ACTIF' ? 'available' : 'inactive'}>{e.statut}</Badge>
              <Button variant="ghost" size="sm"><Edit size={16} /></Button>
              <Button variant="danger" size="sm"><Trash2 size={16} /></Button>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default EnseignesPage;