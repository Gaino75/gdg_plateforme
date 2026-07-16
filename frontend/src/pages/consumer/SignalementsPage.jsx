// Mes signalements 

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { AlertTriangle, CheckCircle, Clock, Plus } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { ROUTES } from '../../constants/routes';
import { formatDateTime } from '../../utils/formatters';

const SignalementsPage = () => {
  const { user } = useAuth();
  const [signalements, setSignalements] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchSignalements();
  }, []);

  const fetchSignalements = async () => {
    try {
      // TODO: Remplacer par l'endpoint réel
      const response = await axiosInstance.get(`${API.NOTIFICATIONS.SIGNALEMENTS}/consommateur/${user.id}`);
      setSignalements(response.data);
    } catch (error) {
      console.error('Erreur de chargement des signalements', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Mes signalements</h1>
          <p className="text-gray-500">
            Un signalement n'est publié qu'après 2 confirmations concordantes
          </p>
        </div>
        <Link to={ROUTES.CONSUMER_NEW_SIGNALEMENT}>
          <Button variant="orange" size="sm" className="gap-2">
            <Plus size={16} /> Nouveau signalement
          </Button>
        </Link>
      </div>

      <div className="space-y-3">
        {signalements.map((s) => (
          <Card key={s.id} className="flex items-center justify-between">
            <div className="flex items-start gap-4">
              <div className={`p-2 rounded-lg ${
                s.typeSignalement === 'RUPTURE' ? 'bg-red-50' : 'bg-green-50'
              }`}>
                {s.typeSignalement === 'RUPTURE' ? (
                  <AlertTriangle size={20} className="text-red-500" />
                ) : (
                  <CheckCircle size={20} className="text-green-500" />
                )}
              </div>
              <div>
                <p className="font-semibold text-gray-900">
                  {s.typeSignalement === 'RUPTURE' ? 'Rupture' : 'Disponibilité'} — {s.agenceNom || `Agence ${s.agenceId}`}
                </p>
                <div className="flex items-center gap-3 text-sm text-gray-500">
                  <span>{s.nbConcordants || 0}/2 concordants</span>
                  <span>·</span>
                  <span>{formatDateTime(s.dateSignalement)}</span>
                </div>
              </div>
            </div>
            <Badge status={
              s.statut === 'CONFIRME' ? 'confirmed' :
              s.statut === 'REJETE' ? 'cancelled' : 'pending'
            }>
              {s.statut === 'CONFIRME' ? 'Validé' :
               s.statut === 'REJETE' ? 'Rejeté' : 'En attente'}
            </Badge>
          </Card>
        ))}

        {signalements.length === 0 && (
          <div className="text-center py-10">
            <AlertTriangle size={48} className="mx-auto text-gray-300 mb-3" />
            <p className="text-gray-500">Aucun signalement effectué</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default SignalementsPage;