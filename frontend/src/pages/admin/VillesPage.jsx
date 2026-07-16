// Gestion villes

import React, { useState, useEffect } from 'react';
import { Plus, Edit, Trash2 } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Loader from '../../components/ui/Loader';

const VillesPage = () => {
  const [loading, setLoading] = useState(true);
  const [villes, setVilles] = useState([]);

  useEffect(() => {
    fetchVilles();
  }, []);

  const fetchVilles = async () => {
    try {
      const response = await axiosInstance.get(API.AGENCES.VILLES);
      setVilles(response.data || []);
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
          <h1 className="text-2xl font-bold text-gray-900">Villes</h1>
          <p className="text-gray-500">Gestion des villes couvertes</p>
        </div>
        <Button variant="orange" className="gap-2"><Plus size={18} /> Ajouter</Button>
      </div>

      <div className="space-y-3">
        {villes.map((v) => (
          <Card key={v.id} className="flex items-center justify-between">
            <div>
              <p className="font-semibold text-gray-900">{v.nom}</p>
              <p className="text-sm text-gray-500">{v.region} · {v.pays}</p>
            </div>
            <div className="flex items-center gap-2">
              <Button variant="ghost" size="sm"><Edit size={16} /></Button>
              <Button variant="danger" size="sm"><Trash2 size={16} /></Button>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default VillesPage;