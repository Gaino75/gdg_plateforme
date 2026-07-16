// detail d'une reservation

import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, Clock, Calendar, FileText, CreditCard } from 'lucide-react';
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
  const [reservation, setReservation] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showCancelDialog, setShowCancelDialog] = useState(false);

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

  const handleCancel = async () => {
    try {
      await axiosInstance.put(API.RESERVATIONS.ANNULER(id, 'Annulé par le client', user.id));
      await fetchReservation();
    } catch (error) {
      console.error('Erreur d\'annulation', error);
    }
  };

  const handlePaiement = () => {
    navigate(ROUTES.CONSUMER_PAIEMENT, { state: { reservationId: id, montant: reservation.montantTotal } });
  };

  if (loading) return <Loader className="min-h-[60vh]" />;
  if (!reservation) return <p className="text-center py-10 text-gray-500">Réservation non trouvée</p>;

  const isPending = reservation.statut === 'EN_ATTENTE';
  const isPaid = reservation.statut === 'PAYEE' || reservation.statut === 'CONFIRMEE';

  return (
    <div className="space-y-6">
      <Link to="/consommateur/reservations" className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour aux réservations
      </Link>

      <Card>
        <div className="flex items-start justify-between">
          <div>
            <h1 className="text-xl font-bold text-gray-900">{reservation.referenceReservation}</h1>
            <p className="text-gray-500">{reservation.agenceId}</p>
          </div>
          <Badge status={reservation.statut === 'EN_ATTENTE' ? 'pending' : 
                         reservation.statut === 'CONFIRMEE' ? 'available' :
                         reservation.statut === 'ANNULEE' ? 'cancelled' :
                         reservation.statut === 'EXPIREE' ? 'expired' : 'default'}>
            {getReservationStatusLabel(reservation.statut)}
          </Badge>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
          <div className="space-y-2">
            <div className="flex items-center gap-2 text-sm">
              <Calendar size={16} className="text-gray-400" />
              <span>Créée le {formatDateTime(reservation.dateReservation)}</span>
            </div>
            {reservation.dateExpiration && (
              <div className="flex items-center gap-2 text-sm text-orange-500">
                <Clock size={16} />
                <span>Expire le {formatDateTime(reservation.dateExpiration)}</span>
              </div>
            )}
          </div>
          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <span className="text-gray-500">Quantité</span>
              <span className="font-medium">{reservation.quantite} x {reservation.categorieProduitId}</span>
            </div>
            <div className="flex items-center justify-between border-t pt-2">
              <span className="font-semibold text-gray-900">Total</span>
              <span className="text-lg font-bold text-[#1E3A5F]">{formatPrice(reservation.montantTotal)}</span>
            </div>
          </div>
        </div>

        {/* Actions */}
        <div className="flex flex-wrap gap-3 mt-6 pt-4 border-t">
          {isPending && (
            <>
              <Button variant="orange" onClick={handlePaiement} className="gap-2">
                <CreditCard size={16} /> Payer maintenant
              </Button>
              <Button variant="danger" onClick={() => setShowCancelDialog(true)}>
                Annuler
              </Button>
            </>
          )}
          {isPaid && (
            <Button variant="outline" className="gap-2">
              <FileText size={16} /> Télécharger la facture
            </Button>
          )}
        </div>
      </Card>
    </div>
  );
};

export default ReservationDetailPage;