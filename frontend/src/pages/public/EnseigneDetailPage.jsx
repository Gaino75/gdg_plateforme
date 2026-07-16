// Detail enseigne (stations)

import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, MapPin, Star } from 'lucide-react';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import AgenceCard from '../../components/cards/AgenceCard';

const EnseigneDetailPage = () => {
  const { id } = useParams();
  const [enseigne, setEnseigne] = useState(null);
  const [agences, setAgences] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, [id]);

  const fetchData = async () => {
    try {
      const [enseigneRes, agencesRes] = await Promise.all([
        axiosInstance.get(`${API.AGENCES.ENSEIGNES}/${id}`),
        axiosInstance.get(`${API.AGENCES.BASE}/enseigne/${id}/actives`),
      ]);
      setEnseigne(enseigneRes.data);
      setAgences(agencesRes.data);
    } catch (error) {
      console.error('Erreur', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;
  if (!enseigne) return <p className="text-center py-10 text-gray-500">Enseigne non trouvée</p>;

  return (
    <div className="space-y-6">
      <Link to="/" className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour
      </Link>
      <Card>
        <div className="flex items-center gap-4">
          {enseigne.logo ? (
            <img src={enseigne.logo} alt={enseigne.nom} className="h-20 w-20 object-contain" />
          ) : (
            <div className="h-20 w-20 bg-gray-200 rounded-full flex items-center justify-center text-2xl">
              {enseigne.nom?.[0]}
            </div>
          )}
          <div>
            <h1 className="text-2xl font-bold text-gray-900">{enseigne.nom}</h1>
            <p className="text-gray-500">{enseigne.description}</p>
            <div className="flex items-center gap-3 mt-1 text-sm">
              <span>{agences.length} stations</span>
              <Badge status="available">Disponible</Badge>
            </div>
          </div>
        </div>
      </Card>
      <h2 className="text-lg font-semibold text-gray-900">Stations disponibles</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {agences.map((agence) => (
          <AgenceCard key={agence.id} agence={agence} />
        ))}
        {agences.length === 0 && <p className="text-gray-500">Aucune station disponible</p>}
      </div>
    </div>
  );
};

export default EnseigneDetailPage;