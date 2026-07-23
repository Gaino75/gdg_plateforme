// Gestion stock de mon agence 

import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Loader from '../../components/ui/Loader';
import Badge from '../../components/ui/Badge';
import ConfirmDialog from '../../components/ui/ConfirmDialog';
import { formatDateTime } from '../../utils/formatters';
import { Link } from 'lucide-react';
import { Package } from 'lucide-react';
import {ROUTES} from '../../constants/routes';

const StockPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [stock, setStock] = useState([]);
  const [editSeuil, setEditSeuil] = useState(null);
  const [showConfirm, setShowConfirm] = useState(false);

  useEffect(() => {
    fetchStock();
  }, []);

  const fetchStock = async () => {
    try {
      const agenceId = user?.agenceId || 1;
      const response = await axiosInstance.get(API.STOCK.AGENCE(agenceId));
      setStock(response.data);
    } catch (error) {
      console.error('Erreur de chargement du stock', error);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateSeuil = async (id, nouveauSeuil) => {
    try {
      await axiosInstance.patch(API.STOCK.SEUIL(user.agenceId, id), { seuilCritique: nouveauSeuil });
      await fetchStock();
      setEditSeuil(null);
    } catch (error) {
      console.error('Erreur de mise à jour du seuil', error);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Gestion du stock</h1>
          <p className="text-gray-500">Seuils critiques et niveaux par catégorie de produit</p>
        </div>
        <Link to={ROUTES.DISTRIBUTOR_APPROVISIONNEMENT}>
          <Button variant="orange" className="gap-2">
            <Package size={18} /> Approvisionner
          </Button>
        </Link>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Catégorie</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Quantité</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Seuil critique</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Dernière MAJ</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Statut</th>
              <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {stock.map((s) => {
              const isOut = s.quantiteDisponible === 0;
              const isCritical = s.quantiteDisponible > 0 && s.quantiteDisponible <= s.seuilCritique;

              return (
                <tr key={s.categorieProduitId} className="hover:bg-gray-50">
                  <td className="px-4 py-3 font-medium text-gray-900">{s.libelle}</td>
                  <td className="px-4 py-3">{s.quantiteDisponible}</td>
                  <td className="px-4 py-3">
                    {editSeuil === s.categorieProduitId ? (
                      <div className="flex items-center gap-2">
                        <Input
                          type="number"
                          value={s.seuilCritique}
                          onChange={(e) => {
                            const newStock = stock.map(item =>
                              item.categorieProduitId === s.categorieProduitId
                                ? { ...item, seuilCritique: parseInt(e.target.value) }
                                : item
                            );
                            setStock(newStock);
                          }}
                          className="w-20"
                        />
                        <Button
                          size="sm"
                          onClick={() => handleUpdateSeuil(s.categorieProduitId, s.seuilCritique)}
                        >
                          OK
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => setEditSeuil(null)}
                        >
                          Annuler
                        </Button>
                      </div>
                    ) : (
                      <span>{s.seuilCritique}</span>
                    )}
                  </td>
                  <td className="px-4 py-3 text-sm text-gray-500">{formatDateTime(s.derniereMiseAJour)}</td>
                  <td className="px-4 py-3">
                    <Badge status={isOut ? 'out' : isCritical ? 'limited' : 'available'}>
                      {isOut ? 'Rupture' : isCritical ? 'Critique' : 'Normal'}
                    </Badge>
                  </td>
                  <td className="px-4 py-3">
                    {editSeuil !== s.categorieProduitId && (
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => setEditSeuil(s.categorieProduitId)}
                      >
                        Modifier seuil
                      </Button>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default StockPage;