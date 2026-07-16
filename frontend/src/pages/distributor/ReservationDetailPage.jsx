// detail + valider/annuler 

import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, Calendar, User, Package, Phone, CheckCircle, XCircle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import ConfirmDialog from '../../components/ui/ConfirmDialog';
import { ROUTES } from '../../constants/routes';
import { formatDateTime, formatPrice, getReservationStatusLabel } from '../../utils/formatters';

const ReservationDetailPage = () => {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [reservation, setReservation] = useState(null);
  const [showConfirm, setShowConfirm] = useState(false);

  useEffect(() => {
    fetchReservation();
  }, [id]);

  const fetchReservation = async () => {
    try {
      const response = await axiosInstance.get(`${API.RESERVATIONS.BASE}/${id}`);
      setReservation(response.data);
    } catch (error) {
      console.error('Erreur de chargement', error);
    } finally {
      setLoading(false);
    }
  };

  const handleConfirmer = async () => {
    try {
      await axiosInstance.put(API.RESERVATIONS.RECUPERER(id));
      await fetchReservation();
    } catch (error) {
      console.error('Erreur de confirmation', error);
    }
  };

  const handleAnnuler = async () => {
    try {
      await axiosInstance.put(API.RESERVATIONS.ANNULER(id, 'Annulé par le distributeur', user.id));
      await fetchReservation();
      setShowConfirm(false);
    } catch (error) {
      console.error('Erreur d\'annulation', error);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;
  if (!reservation) return <p className="text-center py-10 text-gray-500">Réservation non trouvée</p>;

  const isPending = reservation.statut === 'EN_ATTENTE' || reservation.statut === 'PAYEE';

  return (
    <div className="space-y-6">
      <Link to={ROUTES.DISTRIBUTOR_RESERVATIONS} className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour
      </Link>

      <Card>
        <div className="flex items-start justify-between">
          <div>
            <h1 className="text-xl font-bold text-gray-900">{reservation.referenceReservation}</h1>
            <p className="text-gray-500">Réservation du {formatDateTime(reservation.dateReservation)}</p>
          </div>
          <Badge status={
            reservation.statut === 'EN_ATTENTE' ? 'pending' :
            reservation.statut === 'CONFIRMEE' ? 'confirmed' :
            reservation.statut === 'ANNULEE' ? 'cancelled' :
            reservation.statut === 'EXPIREE' ? 'expired' : 'available'
          }>
            {getReservationStatusLabel(reservation.statut)}
          </Badge>
        </div>

        <div className="grid grid-cols-2 gap-4 mt-4 pt-4 border-t">
          <div>
            <p className="text-sm text-gray-500">Client</p>
            <p className="font-medium flex items-center gap-1">
              <User size={14} /> {reservation.consommateurId}
            </p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Produit</p>
            <p className="font-medium flex items-center gap-1">
              <Package size={14} /> {reservation.categorieProduitId} × {reservation.quantite}
            </p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Montant</p>
            <p className="text-lg font-bold text-[#1E3A5F]">{formatPrice(reservation.montantTotal)}</p>
          </div>
          <div>
            <p className="text-sm text-gray-500">Paiement</p>
            <p className="font-medium">{reservation.modePaiement || 'Non spécifié'}</p>
          </div>
        </div>

        {isPending && (
          <div className="flex flex-wrap gap-3 mt-6 pt-4 border-t">
            <Button variant="success" onClick={handleConfirmer} className="gap-2">
              <CheckCircle size={16} /> Préparer la commande
            </Button>
            <Button variant="danger" onClick={() => setShowConfirm(true)} className="gap-2">
              <XCircle size={16} /> Annuler
            </Button>
          </div>
        )}
      </Card>

      <ConfirmDialog
        isOpen={showConfirm}
        onClose={() => setShowConfirm(false)}
        onConfirm={handleAnnuler}
        title="Annuler la réservation"
        message="Êtes-vous sûr de vouloir annuler cette réservation ?"
      />
    </div>
  );
};

export default ReservationDetailPage;