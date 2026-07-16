// Detail enseigne

import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, Building, Globe, Phone, Mail } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { ROUTES } from '../../constants/routes';
import { formatDateTime } from '../../utils/formatters';

const EnseigneDetailPage = () => {
  const { id } = useParams();
  const [loading, setLoading] = useState(true);
  const [enseigne, setEnseigne] = useState(null);

  useEffect(() => {
    fetchEnseigne();
  }, [id]);

  const fetchEnseigne = async () => {
    try {
      const response = await axiosInstance.get(`${API.AGENCES.ENSEIGNES}/${id}`);
      setEnseigne(response.data);
    } catch (error) {
      console.error('Erreur de chargement', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;
  if (!enseigne) return <p className="text-center py-10 text-gray-500">Enseigne non trouvée</p>;

  return (
    <div className="space-y-6">
      <Link to={ROUTES.ADMIN_ENSEIGNES} className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour
      </Link>

      <Card>
        <div className="flex items-center gap-4">
          {enseigne.logo ? (
            <img src={enseigne.logo} alt={enseigne.nom} className="h-20 w-20 object-contain" />
          ) : (
            <div className="h-20 w-20 bg-gray-200 rounded-full flex items-center justify-center text-2xl font-bold">
              {enseigne.nom?.[0]}
            </div>
          )}
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-2xl font-bold text-gray-900">{enseigne.nom}</h1>
              <Badge status={enseigne.statut === 'ACTIF' ? 'available' : 'inactive'}>
                {enseigne.statut}
              </Badge>
            </div>
            <p className="text-gray-500">{enseigne.description}</p>
            <div className="flex flex-wrap items-center gap-3 mt-2 text-sm text-gray-500">
              {enseigne.siteWeb && (
                <span className="flex items-center gap-1"><Globe size={14} /> {enseigne.siteWeb}</span>
              )}
              {enseigne.telephone && (
                <span className="flex items-center gap-1"><Phone size={14} /> {enseigne.telephone}</span>
              )}
              {enseigne.emailContact && (
                <span className="flex items-center gap-1"><Mail size={14} /> {enseigne.emailContact}</span>
              )}
            </div>
            <p className="text-sm text-gray-400 mt-2">
              Créée le {formatDateTime(enseigne.dateCreation)} · {enseigne.agences?.length || 0} agences
            </p>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default EnseigneDetailPage;