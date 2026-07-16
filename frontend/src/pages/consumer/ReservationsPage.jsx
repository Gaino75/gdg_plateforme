// Mes reservations

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Filter, Calendar, Clock } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useReservations } from '../../hooks/useReservations';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Loader from '../../components/ui/Loader';
import ReservationCard from '../../components/cards/ReservationCard';
import { ROUTES } from '../../constants/routes';

const ReservationsPage = () => {
  const { user } = useAuth();
  const { reservations, loading, refetch } = useReservations(user?.id);
  const [filter, setFilter] = useState('all');

  const filteredReservations = reservations?.filter(r => {
    if (filter === 'all') return true;
    return r.statut === filter;
  });

  const stats = {
    total: reservations?.length || 0,
    enAttente: reservations?.filter(r => r.statut === 'EN_ATTENTE').length || 0,
    confirmees: reservations?.filter(r => r.statut === 'CONFIRMEE').length || 0,
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Mes réservations</h1>
          <p className="text-gray-500">Historique et suivi de vos réservations</p>
        </div>
        <Link to={ROUTES.CONSUMER_NEW_RESERVATION}>
          <Button variant="orange">+ Nouvelle réservation</Button>
        </Link>
      </div>

      {/* Statistiques */}
      <div className="grid grid-cols-3 gap-4">
        <div className="bg-white p-3 rounded-lg border border-gray-200 text-center">
          <span className="text-lg font-bold text-gray-900">{stats.total}</span>
          <p className="text-xs text-gray-500">Total</p>
        </div>
        <div className="bg-white p-3 rounded-lg border border-orange-200 text-center">
          <span className="text-lg font-bold text-[#FF6B35]">{stats.enAttente}</span>
          <p className="text-xs text-gray-500">En attente</p>
        </div>
        <div className="bg-white p-3 rounded-lg border border-green-200 text-center">
          <span className="text-lg font-bold text-[#2ECC71]">{stats.confirmees}</span>
          <p className="text-xs text-gray-500">Confirmées</p>
        </div>
      </div>

      {/* Filtres */}
      <div className="flex flex-wrap gap-2">
        {['all', 'EN_ATTENTE', 'PAYEE', 'CONFIRMEE', 'ANNULEE', 'EXPIREE', 'RECUPEREE'].map((f) => (
          <button
            key={f}
            onClick={() => setFilter(f)}
            className={`px-3 py-1.5 rounded-full text-sm font-medium transition-colors ${
              filter === f
                ? 'bg-[#1E3A5F] text-white'
                : 'bg-white border border-gray-200 text-gray-600 hover:bg-gray-50'
            }`}
          >
            {f === 'all' ? 'Toutes' : f.replace('_', ' ')}
          </button>
        ))}
      </div>

      {/* Liste */}
      {filteredReservations?.length === 0 ? (
        <div className="text-center py-10">
          <Calendar size={48} className="mx-auto text-gray-300 mb-3" />
          <p className="text-gray-500">Aucune réservation trouvée</p>
        </div>
      ) : (
        <div className="space-y-3">
          {filteredReservations.map((reservation) => (
            <ReservationCard key={reservation.id} reservation={reservation} />
          ))}
        </div>
      )}
    </div>
  );
};

export default ReservationsPage;