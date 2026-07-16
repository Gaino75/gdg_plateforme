// Toutes les stations(avec filtres )

import React, { useState, useEffect } from 'react';
import { Search, MapPin } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Input from '../../components/ui/Input';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { Link } from 'react-router-dom';

const StationsPage = () => {
  const [agences, setAgences] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    fetchAgences();
  }, []);

  const fetchAgences = async () => {
    try {
      const response = await axiosInstance.get(API.AGENCES.ACTIVES);
      setAgences(response.data);
    } catch (error) {
      console.error('Erreur', error);
    } finally {
      setLoading(false);
    }
  };

  const filtered = agences.filter(a => {
    const matchSearch = a.nom?.toLowerCase().includes(search.toLowerCase()) ||
                        a.ville?.nom?.toLowerCase().includes(search.toLowerCase());
    const matchFilter = filter === 'all' || a.ville?.nom === filter;
    return matchSearch && matchFilter;
  });

  const villes = [...new Set(agences.map(a => a.ville?.nom).filter(Boolean))];

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Trouver du gaz près de vous</h1>
      <p className="text-gray-500">Disponibilité en temps réel sur tout le réseau GDG</p>

      <div className="flex flex-wrap gap-3">
        <div className="flex-1 min-w-[200px]">
          <Input placeholder="Rechercher..." value={search} onChange={(e) => setSearch(e.target.value)} icon={Search} />
        </div>
        <div className="flex flex-wrap gap-2">
          <button onClick={() => setFilter('all')} className={`px-3 py-1.5 rounded-full text-sm font-medium ${filter === 'all' ? 'bg-[#1E3A5F] text-white' : 'bg-white border text-gray-600'}`}>Toutes</button>
          {villes.map(v => (
            <button key={v} onClick={() => setFilter(v)} className={`px-3 py-1.5 rounded-full text-sm font-medium ${filter === v ? 'bg-[#1E3A5F] text-white' : 'bg-white border text-gray-600'}`}>{v}</button>
          ))}
        </div>
      </div>

      <div className="space-y-3">
        {filtered.map(a => (
          <Link to={`/agence/${a.id}`} key={a.id}>
            <Card hover className="flex items-center justify-between">
              <div>
                <span className="font-semibold text-gray-900">{a.nom}</span>
                <div className="flex items-center gap-2 text-sm text-gray-500">
                  <MapPin size={14} /> {a.ville?.nom}
                </div>
              </div>
              <Badge status="available">Disponible</Badge>
            </Card>
          </Link>
        ))}
        {filtered.length === 0 && <p className="text-center py-10 text-gray-500">Aucune station trouvée</p>}
      </div>
    </div>
  );
};

export default StationsPage;