// Reservations recues

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Calendar, CheckCircle, XCircle } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Loader from '../../components/ui/Loader';
import ConfirmDialog from '../../components/ui/ConfirmDialog';
import { formatDateTime } from '../../utils/formatters';

const ReservationsPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [reservations, setReservations] = useState([]);
  const [showConfirm, setShowConfirm] = useState(null);

  useEffect(() => {
    fetchReservations();
  }, []);

  const fetchReservations = async () => {
    try {
      const response = await axiosInstance.get(API.RESERVATIONS.AGENCE(user.agenceId));
      setReservations(response.data || []);
    } catch (error) {
      console.error('Erreur de chargement des réservations', error);
    } finally {
      setLoading(false);
    }
  };

  const handleConfirm = async (id) => {
    try {
      await axiosInstance.put(API.RESERVATIONS.RECUPERER(id));
      await fetchReservations();
    } catch (error) {
      console.error('Erreur de confirmation', error);
    }
  };

  if (loading) return <Loader className="min-h-[60vh]" />;

  const enAttente = reservations.filter(r => r.statut === 'EN_ATTENTE' || r.statut === 'PAYEE');

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Réservations reçues</h1>
        <p className="text-gray-500">
          {enAttente.length} réservation{enAttente.length > 1 ? 's' : ''} en attente de traitement
        </p>
      </div>

      {enAttente.length === 0 ? (
        <div className="text-center py-10">
          <Calendar size={48} className="mx-auto text-gray-300 mb-3" />
          <p className="text-gray-500">Aucune réservation en attente</p>
        </div>
      ) : (
        <div className="space-y-3">
          {enAttente.map((r) => (
            <Card key={r.id} className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
              <div>
                <p className="font-semibold text-gray-900">
                  {r.consommateurId} — {r.categorieProduitId} × {r.quantite}
                </p>
                <div className="flex flex-wrap items-center gap-3 text-sm text-gray-500">
                  <span>{r.referenceReservation}</span>
                  <span>·</span>
                  <span>{formatDateTime(r.dateReservation)}</span>
                  <Badge status={r.statut === 'PAYEE' ? 'confirmed' : 'pending'}>
                    {r.statut === 'PAYEE' ? 'Payée' : 'En attente'}
                  </Badge>
                </div>
              </div>
              <div className="flex flex-wrap gap-2">
                <Button
                  variant="success"
                  size="sm"
                  className="gap-1"
                  onClick={() => handleConfirm(r.id)}
                >
                  <CheckCircle size={14} /> Préparer
                </Button>
                <Button
                  variant="danger"
                  size="sm"
                  className="gap-1"
                  onClick={() => setShowConfirm(r.id)}
                >
                  <XCircle size={14} /> Annuler
                </Button>
              </div>
            </Card>
          ))}
        </div>
      )}

      <ConfirmDialog
        isOpen={!!showConfirm}
        onClose={() => setShowConfirm(null)}
        onConfirm={() => {
          // TODO: Implémenter l'annulation
          setShowConfirm(null);
        }}
        title="Annuler la réservation"
        message="Êtes-vous sûr de vouloir annuler cette réservation ?"
      />
    </div>
  );
};

export default ReservationsPage;