//  Detail d'une vente + facture

import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ArrowLeft, FileText, Download, Calendar, CreditCard } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import { ROUTES } from '../../constants/routes';
import { formatDateTime, formatPrice } from '../../utils/formatters';

const VenteDetailPage = () => {
  const { id } = useParams();
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [vente, setVente] = useState(null);

  useEffect(() => {
    fetchVente();
  }, [id]);

  const fetchVente = async () => {
    try {
      // TODO: Remplacer par l'endpoint réel
      // const response = await axiosInstance.get(`${API.VENTES.BASE}/${id}`);
      // setVente(response.data);
      setVente({
        id: 1,
        referenceVente: 'V-8821',
        categorieProduitId: '3 kg',
        quantite: 2,
        prixTotal: 7000,
        modePaiement: 'CASH',
        dateVente: '2026-07-09T18:12:00',
        nomClient: 'Jean Nguema',
        telephoneClient: '+237 677 12 34 56',
        observations: 'Vente au comptoir',
      });
    } catch (error) {
      console.error('Erreur de chargement', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;
  if (!vente) return <p className="text-center py-10 text-gray-500">Vente non trouvée</p>;

  return (
    <div className="space-y-6">
      <Link to={ROUTES.DISTRIBUTOR_HISTORIQUE} className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour
      </Link>

      <Card>
        <div className="flex items-start justify-between">
          <div>
            <h1 className="text-xl font-bold text-gray-900">{vente.referenceVente}</h1>
            <p className="text-gray-500">Vente du {formatDateTime(vente.dateVente)}</p>
          </div>
          <Badge status="confirmed">Confirmée</Badge>
        </div>

        <div className="grid grid-cols-2 gap-4 mt-4 pt-4 border-t">
          <div>
            <p className="text-sm text-gray-500">Catégorie</p>
            <p className="font-medium">{vente.categorieProduitId}</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Quantité</p>
            <p className="font-medium">{vente.quantite}</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Mode de paiement</p>
            <p className="font-medium">{vente.modePaiement}</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Montant total</p>
            <p className="text-xl font-bold text-[#1E3A5F]">{formatPrice(vente.prixTotal)}</p>
          </div>
          {vente.nomClient && (
            <div>
              <p className="text-sm text-gray-500">Client</p>
              <p className="font-medium">{vente.nomClient}</p>
            </div>
          )}
          {vente.telephoneClient && (
            <div>
              <p className="text-sm text-gray-500">Téléphone</p>
              <p className="font-medium">{vente.telephoneClient}</p>
            </div>
          )}
        </div>

        {vente.observations && (
          <div className="mt-4 pt-4 border-t">
            <p className="text-sm text-gray-500">Observations</p>
            <p className="text-sm text-gray-600">{vente.observations}</p>
          </div>
        )}

        <div className="flex flex-wrap gap-3 mt-6 pt-4 border-t">
          <Button variant="outline" className="gap-2">
            <Download size={16} /> Télécharger la facture
          </Button>
        </div>
      </Card>
    </div>
  );
};

export default VenteDetailPage;