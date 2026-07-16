// Detail agence + actions 

import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, Store, MapPin, Phone, Mail, Calendar } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { ROUTES } from '../../constants/routes';
import { formatDateTime } from '../../utils/formatters';

const AgenceDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [agence, setAgence] = useState(null);

  useEffect(() => {
    fetchAgence();
  }, [id]);

  const fetchAgence = async () => {
    try {
      const response = await axiosInstance.get(`${API.AGENCES.BASE}/${id}`);
      setAgence(response.data);
    } catch (error) {
      console.error('Erreur de chargement', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAction = async (action) => {
    try {
      if (action === 'valider') {
        await axiosInstance.put(API.AGENCES.VALIDER(id, 1));
      } else if (action === 'suspendre') {
        await axiosInstance.put(API.AGENCES.SUSPENDRE(id));
      } else if (action === 'reactiver') {
        await axiosInstance.put(API.AGENCES.REACTIVER(id));
      }
      await fetchAgence();
    } catch (error) {
      console.error('Erreur', error);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;
  if (!agence) return <p className="text-center py-10 text-gray-500">Agence non trouvée</p>;

  return (
    <div className="space-y-6">
      <Link to={ROUTES.ADMIN_AGENCES} className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour
      </Link>

      <Card>
        <div className="flex flex-col md:flex-row md:items-start justify-between gap-4">
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-2xl font-bold text-gray-900">{agence.nom}</h1>
              <Badge status={
                agence.statut === 'ACTIF' ? 'available' :
                agence.statut === 'SUSPENDU' ? 'suspended' : 'pending'
              }>
                {agence.statut}
              </Badge>
            </div>
            <div className="flex items-center gap-2 text-gray-500 mt-1">
              <Store size={16} /> {agence.enseigne?.nom}
            </div>
            <div className="flex items-center gap-2 text-gray-500 mt-1">
              <MapPin size={16} /> {agence.adresse}, {agence.ville?.nom}
            </div>
            {agence.telephone && (
              <div className="flex items-center gap-2 text-gray-500 mt-1">
                <Phone size={16} /> {agence.telephone}
              </div>
            )}
            {agence.email && (
              <div className="flex items-center gap-2 text-gray-500 mt-1">
                <Mail size={16} /> {agence.email}
              </div>
            )}
            <div className="flex items-center gap-2 text-sm text-gray-400 mt-2">
              <Calendar size={14} /> Créée le {formatDateTime(agence.dateCreation)}
              {agence.dateValidation && ` · Validée le ${formatDateTime(agence.dateValidation)}`}
            </div>
          </div>
          <div className="flex flex-wrap gap-2">
            {agence.statut === 'EN_ATTENTE' && (
              <>
                <Button variant="success" onClick={() => handleAction('valider')}>Valider</Button>
                <Button variant="danger" onClick={() => handleAction('rejeter')}>Rejeter</Button>
              </>
            )}
            {agence.statut === 'ACTIF' && (
              <Button variant="danger" onClick={() => handleAction('suspendre')}>Suspendre</Button>
            )}
            {agence.statut === 'SUSPENDU' && (
              <Button variant="success" onClick={() => handleAction('reactiver')}>Réactiver</Button>
            )}
          </div>
        </div>
      </Card>
    </div>
  );
};

export default AgenceDetailPage;