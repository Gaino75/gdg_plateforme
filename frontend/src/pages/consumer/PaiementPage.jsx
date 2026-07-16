// Paiement Mobile money ou cash 

import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { CreditCard, ArrowLeft, CheckCircle, XCircle, Fuel } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Select from '../../components/ui/Select';
import Loader from '../../components/ui/Loader';
import Alert from '../../components/ui/Alert';
import { ROUTES } from '../../constants/routes';
import { formatPrice } from '../../utils/formatters';

const PaiementPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [loading, setLoading] = useState(false);
  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const [reservation, setReservation] = useState(null);

  const [form, setForm] = useState({
    modePaiement: 'ORANGE_MONEY',
    numeroTelephone: user?.telephone || '',
  });

  const reservationId = location.state?.reservationId;
  const montant = location.state?.montant || 0;

  useEffect(() => {
    if (reservationId) {
      fetchReservation();
    }
  }, [reservationId]);

  const fetchReservation = async () => {
    try {
      const response = await axiosInstance.get(`${API.RESERVATIONS.BASE}/${reservationId}`);
      setReservation(response.data);
    } catch (error) {
      console.error('Erreur de chargement', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setProcessing(true);

    try {
      // Simuler l'appel au service de paiement
      await new Promise(resolve => setTimeout(resolve, 1500));

      // Pour la démo, on simule un paiement réussi
      const referencePaiement = `PAY-${Date.now()}`;
      
      // Confirmer le paiement
      await axiosInstance.put(API.RESERVATIONS.PAIEMENT(reservationId, referencePaiement));
      
      setSuccess(true);
      setTimeout(() => {
        navigate(ROUTES.CONSUMER_RESERVATIONS);
      }, 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors du paiement');
    } finally {
      setProcessing(false);
    }
  };

  if (!reservationId) {
    return <p className="text-center py-10 text-gray-500">Aucune réservation en cours</p>;
  }

  if (success) {
    return (
      <div className="max-w-md mx-auto text-center py-10">
        <div className="bg-green-50 p-6 rounded-xl">
          <CheckCircle size={48} className="text-green-500 mx-auto mb-4" />
          <h2 className="text-xl font-bold text-gray-900">Paiement réussi !</h2>
          <p className="text-gray-500 mt-2">Votre réservation est confirmée.</p>
          <p className="text-sm text-gray-400 mt-4">Redirection vers mes réservations...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-md mx-auto space-y-6">
      <Link to={ROUTES.CONSUMER_RESERVATIONS} className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour
      </Link>

      <Card>
        <div className="text-center mb-6">
          <div className="bg-[#1E3A5F] text-white rounded-full w-16 h-16 flex items-center justify-center mx-auto">
            <CreditCard size={24} />
          </div>
          <h1 className="text-2xl font-bold text-gray-900 mt-3">Paiement</h1>
          <p className="text-3xl font-bold text-[#1E3A5F] mt-2">
            {formatPrice(montant)}
          </p>
        </div>

        {error && <Alert type="error" message={error} className="mb-4" />}

        <form onSubmit={handleSubmit}>
          <Select
            label="Mode de paiement"
            name="modePaiement"
            value={form.modePaiement}
            onChange={(e) => setForm({ ...form, modePaiement: e.target.value })}
            options={[
              { value: 'ORANGE_MONEY', label: 'Orange Money' },
              { value: 'MTN_MOBILE_MONEY', label: 'MTN Mobile Money' },
            ]}
            required
          />

          <Input
            label="Numéro de téléphone"
            type="tel"
            value={form.numeroTelephone}
            onChange={(e) => setForm({ ...form, numeroTelephone: e.target.value })}
            placeholder="+237 6XX XXX XXX"
            required
          />

          <Button
            type="submit"
            variant="orange"
            fullWidth
            loading={processing}
            size="lg"
            className="mt-4"
          >
            {processing ? 'Traitement...' : `Payer ${formatPrice(montant)} →`}
          </Button>
        </form>
      </Card>
    </div>
  );
};

export default PaiementPage;