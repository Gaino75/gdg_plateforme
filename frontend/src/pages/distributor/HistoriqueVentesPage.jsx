// Historique toutes les ventes 

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FileText, Calendar, Search } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Loader from '../../components/ui/Loader';
import Badge from '../../components/ui/Badge';
import { ROUTES } from '../../constants/routes';
import { formatDateTime, formatPrice } from '../../utils/formatters';

const HistoriqueVentesPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [ventes, setVentes] = useState([]);
  const [filter, setFilter] = useState('');

  useEffect(() => {
    fetchVentes();
  }, []);

  const fetchVentes = async () => {
    try {
      const response = await axiosInstance.get(API.VENTES.AGENCE(user.agenceId));
      setVentes(response.data || []);
    } catch (error) {
      console.error('Erreur de chargement des ventes', error);
    } finally {
      setLoading(false);
    }
  };

  const filteredVentes = ventes.filter(v =>
    v.referenceVente?.toLowerCase().includes(filter.toLowerCase()) ||
    v.categorieProduitId?.toString().includes(filter)
  );

  const total = filteredVentes.reduce((sum, v) => sum + v.prixTotal, 0);

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between flex-wrap gap-4">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Historique des ventes</h1>
          <p className="text-gray-500">{ventes.length} ventes enregistrées</p>
        </div>
        <Link to={ROUTES.DISTRIBUTOR_VENTE}>
          <Button variant="orange">+ Nouvelle vente</Button>
        </Link>
      </div>

      <div className="flex items-center gap-4">
        <div className="flex-1">
          <Input
            placeholder="Rechercher une vente..."
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
            icon={Search}
          />
        </div>
        <div className="bg-gray-50 px-4 py-2 rounded-lg text-sm">
          Total: <span className="font-bold text-[#1E3A5F]">{formatPrice(total)}</span>
        </div>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Réf.</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Catégorie</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Qté</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Montant</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Paiement</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {filteredVentes.map((v) => (
              <tr key={v.id} className="hover:bg-gray-50">
                <td className="px-4 py-3 font-medium text-gray-900">{v.referenceVente}</td>
                <td className="px-4 py-3">{v.categorieProduitId}</td>
                <td className="px-4 py-3">{v.quantite}</td>
                <td className="px-4 py-3 font-medium">{formatPrice(v.prixTotal)}</td>
                <td className="px-4 py-3">
                  <Badge status={v.modePaiement === 'CASH' ? 'available' : 'pending'}>
                    {v.modePaiement}
                  </Badge>
                </td>
                <td className="px-4 py-3 text-sm text-gray-500">{formatDateTime(v.dateVente)}</td>
                <td className="px-4 py-3">
                  <Link to={`${ROUTES.DISTRIBUTOR_VENTE_DETAIL.replace(':id', v.id)}`}>
                    <Button variant="ghost" size="sm" className="gap-1">
                      <FileText size={14} /> Facture
                    </Button>
                  </Link>
                </td>
              </tr>
            ))}
            {filteredVentes.length === 0 && (
              <tr>
                <td colSpan={7} className="px-4 py-8 text-center text-gray-500">
                  Aucune vente trouvée
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default HistoriqueVentesPage;