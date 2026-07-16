// Mes abonnements

import React, { useState, useEffect } from 'react';
import { Bell, Fuel, MapPin } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import ToggleSwitch from '../../components/ui/ToggleSwitch';
import Loader from '../../components/ui/Loader';
import { formatDate } from '../../utils/formatters';

const AbonnementsPage = () => {
  const { user } = useAuth();
  const [abonnements, setAbonnements] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAbonnements();
  }, []);

  const fetchAbonnements = async () => {
    try {
      const response = await axiosInstance.get(API.NOTIFICATIONS.ABONNEMENTS_CONSOMMATEUR(user.id));
      setAbonnements(response.data);
    } catch (error) {
      console.error('Erreur de chargement des abonnements', error);
    } finally {
      setLoading(false);
    }
  };

  const handleToggle = async (id, actif) => {
    try {
      // TODO: Appel API pour activer/désactiver
      setAbonnements(prev => 
        prev.map(a => a.id === id ? { ...a, actif } : a)
      );
    } catch (error) {
      console.error('Erreur de mise à jour', error);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Mes abonnements</h1>
        <p className="text-gray-500">
          Recevez une alerte dès qu'un produit suivi redevient disponible
        </p>
      </div>

      {abonnements.length === 0 ? (
        <div className="text-center py-10">
          <Bell size={48} className="mx-auto text-gray-300 mb-3" />
          <p className="text-gray-500">Aucun abonnement actif</p>
          <Button variant="outline" className="mt-4">Découvrir les agences</Button>
        </div>
      ) : (
        <div className="space-y-3">
          {abonnements.map((abonnement) => (
            <Card key={abonnement.id} className="flex items-center justify-between">
              <div className="flex items-center gap-4">
                <div className="bg-[#FF6B35]/10 p-3 rounded-lg">
                  <Fuel size={20} className="text-[#FF6B35]" />
                </div>
                <div>
                  <p className="font-semibold text-gray-900">{abonnement.agenceNom || `Agence ${abonnement.agenceId}`}</p>
                  <div className="flex items-center gap-3 text-sm text-gray-500">
                    <span className="flex items-center gap-1">
                      <MapPin size={14} /> {abonnement.ville || 'Toutes catégories'}
                    </span>
                    <span>·</span>
                    <span>Abonné le {formatDate(abonnement.dateAbonnement)}</span>
                  </div>
                </div>
              </div>
              <ToggleSwitch
                checked={abonnement.actif}
                onChange={(checked) => handleToggle(abonnement.id, checked)}
                label={abonnement.actif ? 'Actif' : 'Inactif'}
              />
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default AbonnementsPage;