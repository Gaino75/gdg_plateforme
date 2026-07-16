// carte reservation dans historique des reservations

import React from 'react';
import { Link } from 'react-router-dom';
import { Calendar, Clock, FileText } from 'lucide-react';
import Card from '../ui/Card';
import Badge from '../ui/Badge';
import Button from '../ui/Button';
import { formatDateTime, formatPrice, getReservationStatusLabel } from '../../utils/formatters';

const ReservationCard = ({ reservation }) => {
  const isPending = reservation.statut === 'EN_ATTENTE';
  const isPaid = reservation.statut === 'PAYEE' || reservation.statut === 'CONFIRMEE';

  return (
    <Card>
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
        <div>
          <div className="flex items-center gap-2">
            <span className="font-semibold text-gray-900">
              {reservation.referenceReservation}
            </span>
            <Badge status={
              reservation.statut === 'EN_ATTENTE' ? 'pending' :
              reservation.statut === 'CONFIRMEE' ? 'confirmed' :
              reservation.statut === 'ANNULEE' ? 'cancelled' :
              reservation.statut === 'EXPIREE' ? 'expired' : 'available'
            }>
              {getReservationStatusLabel(reservation.statut)}
            </Badge>
          </div>
          <p className="text-sm text-gray-500">
            {reservation.agenceId} · {reservation.categorieProduitId} × {reservation.quantite}
          </p>
          <div className="flex items-center gap-3 text-xs text-gray-400">
            <span className="flex items-center gap-1">
              <Calendar size={12} /> {formatDateTime(reservation.dateReservation)}
            </span>
            {reservation.dateExpiration && (
              <span className="flex items-center gap-1 text-orange-500">
                <Clock size={12} /> Expire le {formatDateTime(reservation.dateExpiration)}
              </span>
            )}
          </div>
        </div>
        <div className="flex flex-wrap items-center gap-2">
          <span className="font-bold text-[#1E3A5F]">{formatPrice(reservation.montantTotal)}</span>
          {isPending && (
            <Link to={`/consommateur/reservation/${reservation.id}`}>
              <Button variant="orange" size="sm">Payer</Button>
            </Link>
          )}
          {isPaid && (
            <Button variant="outline" size="sm" className="gap-1">
              <FileText size={14} /> Facture
            </Button>
          )}
          <Link to={`/consommateur/reservation/${reservation.id}`}>
            <Button variant="ghost" size="sm">Détail</Button>
          </Link>
        </div>
      </div>
    </Card>
  );
};

export default ReservationCard;