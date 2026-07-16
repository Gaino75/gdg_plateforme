// Supervision stock global

import React, { useState, useEffect } from 'react';
import { Package, Search, AlertTriangle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Input from '../../components/ui/Input';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { formatDateTime } from '../../utils/formatters';

const StocksPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [stocks, setStocks] = useState([]);
  const [search, setSearch] = useState('');

  useEffect(() => {
    fetchStocks();
  }, []);

  const fetchStocks = async () => {
    try {
      const response = await axiosInstance.get(API.STOCK.GLOBAL);
      setStocks(response.data || []);
    } catch (error) {
      console.error('Erreur de chargement des stocks', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredStocks = stocks.filter(s =>
    s.agenceNom?.toLowerCase().includes(search.toLowerCase()) ||
    s.enseigneNom?.toLowerCase().includes(search.toLowerCase()) ||
    s.categorie?.toLowerCase().includes(search.toLowerCase())
  );

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Stock global</h1>
        <p className="text-gray-500">Vue consolidée de toutes les agences</p>
      </div>

      <div className="max-w-sm">
        <Input
          placeholder="Rechercher une agence ou catégorie..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          icon={Search}
        />
      </div>

      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Enseigne</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Agence</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Catégorie</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Quantité</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Statut</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Dernière MAJ</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {filteredStocks.map((s, index) => {
              const isOut = s.quantite === 0;
              const isCritical = s.quantite > 0 && s.quantite <= s.seuil;
              return (
                <tr key={index} className="hover:bg-gray-50">
                  <td className="px-4 py-3 font-medium text-gray-900">{s.enseigneNom || '-'}</td>
                  <td className="px-4 py-3">{s.agenceNom || '-'}</td>
                  <td className="px-4 py-3">{s.categorie || '-'}</td>
                  <td className={`px-4 py-3 font-medium ${isOut ? 'text-red-500' : isCritical ? 'text-orange-500' : 'text-gray-900'}`}>
                    {s.quantite}
                  </td>
                  <td className="px-4 py-3">
                    <Badge status={isOut ? 'out' : isCritical ? 'limited' : 'available'}>
                      {isOut ? 'Rupture' : isCritical ? 'Critique' : 'Normal'}
                    </Badge>
                  </td>
                  <td className="px-4 py-3 text-sm text-gray-500">{formatDateTime(s.derniereMiseAJour)}</td>
                </tr>
              );
            })}
            {filteredStocks.length === 0 && (
              <tr>
                <td colSpan={6} className="px-4 py-8 text-center text-gray-500">
                  Aucun stock trouvé
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default StocksPage;