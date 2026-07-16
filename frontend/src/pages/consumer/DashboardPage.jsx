// Acceuil consommateur connecte

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Package, Bell, Calendar, MapPin, Fuel, AlertTriangle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useReservations } from '../../hooks/useReservations';
import { useNotifications } from '../../hooks/useNotifications';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Loader from '../../components/ui/Loader';
import ReservationCard from '../../components/cards/ReservationCard';
import NotificationCard from '../../components/cards/NotificationCard';
import { ROUTES } from '../../constants/routes';
import { formatNumber } from '../../utils/formatters';

const DashboardPage = () => {
  const { user } = useAuth();
  const { reservations, loading: reservationsLoading } = useReservations(user?.id);
  const { notifications, unreadCount, loading: notifLoading } = useNotifications(user?.id);
  const [stats, setStats] = useState({ total: 0, enAttente: 0, confirmees: 0 });

  useEffect(() => {
    if (reservations) {
      const enAttente = reservations.filter(r => r.statut === 'EN_ATTENTE').length;
      const confirmees = reservations.filter(r => r.statut === 'CONFIRMEE').length;
      setStats({
        total: reservations.length,
        enAttente,
        confirmees,
      });
    }
  }, [reservations]);

  if (reservationsLoading || notifLoading) {
    return <Loader className="min-h-[60vh]" />;
  }

  return (
    <div className="space-y-6">
      {/* En-tête */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">
          Bonjour, {user?.prenom} 👋
        </h1>
        <p className="text-gray-500">Retrouvez vos réservations et notifications</p>
      </div>

      {/* Statistiques */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <Card className="text-center">
          <div className="flex items-center justify-center gap-2 text-[#1E3A5F]">
            <Package size={20} />
            <span className="text-2xl font-bold">{formatNumber(stats.total)}</span>
          </div>
          <p className="text-sm text-gray-500">Réservations totales</p>
        </Card>
        <Card className="text-center border-orange-200">
          <div className="flex items-center justify-center gap-2 text-[#FF6B35]">
            <AlertTriangle size={20} />
            <span className="text-2xl font-bold">{formatNumber(stats.enAttente)}</span>
          </div>
          <p className="text-sm text-gray-500">En attente de paiement</p>
        </Card>
        <Card className="text-center border-green-200">
          <div className="flex items-center justify-center gap-2 text-[#2ECC71]">
            <Calendar size={20} />
            <span className="text-2xl font-bold">{formatNumber(stats.confirmees)}</span>
          </div>
          <p className="text-sm text-gray-500">Confirmées</p>
        </Card>
      </div>

      {/* Actions rapides */}
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <Link to={ROUTES.CONSUMER_NEW_RESERVATION}>
          <Button variant="orange" fullWidth size="lg" className="gap-2">
            <Fuel size={18} /> Nouvelle réservation
          </Button>
        </Link>
        <Link to={ROUTES.CONSUMER_SIGNALEMENTS}>
          <Button variant="outline" fullWidth size="lg" className="gap-2">
            <AlertTriangle size={18} /> Signaler une rupture
          </Button>
        </Link>
      </div>

      {/* Dernières réservations */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-gray-900">Dernières réservations</h2>
          <Link to={ROUTES.CONSUMER_RESERVATIONS} className="text-sm text-[#FF6B35] hover:underline">
            Voir tout
          </Link>
        </div>
        {reservations?.slice(0, 3).map((reservation) => (
          <ReservationCard key={reservation.id} reservation={reservation} />
        ))}
        {(!reservations || reservations.length === 0) && (
          <p className="text-gray-500 text-center py-4">Aucune réservation</p>
        )}
      </div>

      {/* Notifications */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-gray-900">Notifications</h2>
          <Link to={ROUTES.CONSUMER_NOTIFICATIONS} className="text-sm text-[#FF6B35] hover:underline">
            {unreadCount > 0 ? `${unreadCount} non lues` : 'Voir tout'}
          </Link>
        </div>
        {notifications?.slice(0, 3).map((notif) => (
          <NotificationCard key={notif.id} notification={notif} />
        ))}
        {(!notifications || notifications.length === 0) && (
          <p className="text-gray-500 text-center py-4">Aucune notification</p>
        )}
      </div>
    </div>
  );
};

export default DashboardPage;