//  Faire une nouvelle reservation

import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { ArrowLeft, Fuel, CreditCard, Calendar } from 'lucide-react';
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

const NouvelleReservationPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  
  const [form, setForm] = useState({
    agenceId: location.state?.agenceId || '',
    categorieProduitId: location.state?.categorieId || '',
    quantite: 1,
    modePaiement: 'ORANGE_MONEY',
    numeroTelephone: user?.telephone || '',
  });

  const [agence, setAgence] = useState(null);
  const [categories, setCategories] = useState([]);
  const [prixUnitaire, setPrixUnitaire] = useState(0);

  useEffect(() => {
    if (form.agenceId) {
      fetchAgence();
      fetchCategories();
    }
  }, [form.agenceId]);

  useEffect(() => {
    if (form.categorieProduitId && categories.length > 0) {
      const cat = categories.find(c => c.id === parseInt(form.categorieProduitId));
      setPrixUnitaire(cat?.prixUnitaire || 0);
    }
  }, [form.categorieProduitId, categories]);

  const fetchAgence = async () => {
    try {
      const response = await axiosInstance.get(`${API.AGENCES.BASE}/${form.agenceId}`);
      setAgence(response.data);
    } catch (error) {
      console.error('Erreur de chargement de l\'agence', error);
    }
  };

  const fetchCategories = async () => {
    try {
      const response = await axiosInstance.get(API.STOCK.CATEGORIES);
      setCategories(response.data);
    } catch (error) {
      console.error('Erreur de chargement des catégories', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const data = {
        agenceId: parseInt(form.agenceId),
        consommateurId: user.id,
        categorieProduitId: parseInt(form.categorieProduitId),
        quantite: parseInt(form.quantite),
        modePaiement: form.modePaiement,
        numeroTelephone: form.numeroTelephone,
      };
      
      const response = await axiosInstance.post(API.RESERVATIONS.BASE, data);
      setSuccess(true);
      
      // Rediriger vers le paiement après 2 secondes
      setTimeout(() => {
        navigate(ROUTES.CONSUMER_PAIEMENT, {
          state: { 
            reservationId: response.data.id, 
            montant: response.data.montantTotal 
          }
        });
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de la création de la réservation');
    } finally {
      setLoading(false);
    }
  };

  const montantTotal = prixUnitaire * (form.quantite || 1);

  if (success) {
    return (
      <div className="max-w-md mx-auto text-center py-10">
        <div className="bg-green-50 p-6 rounded-xl">
          <Fuel size={48} className="text-green-500 mx-auto mb-4" />
          <h2 className="text-xl font-bold text-gray-900">Réservation créée !</h2>
          <p className="text-gray-500 mt-2">Redirection vers le paiement...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <Link to={form.agenceId ? `/agence/${form.agenceId}` : '/consommateur'} 
            className="flex items-center gap-2 text-gray-500 hover:text-gray-700">
        <ArrowLeft size={18} /> Retour
      </Link>

      <Card>
        <h1 className="text-2xl font-bold text-gray-900">Réserver un produit</h1>
        <p className="text-gray-500">Vérifiez les détails avant de confirmer</p>

        {error && <Alert type="error" message={error} className="mt-4" />}

        <form onSubmit={handleSubmit} className="mt-6 space-y-4">
          <Input
            label="Agence"
            value={agence?.nom || 'Chargement...'}
            disabled
            className="text-gray-600"
          />

          <Select
            label="Catégorie de produit"
            name="categorieProduitId"
            value={form.categorieProduitId}
            onChange={(e) => setForm({ ...form, categorieProduitId: e.target.value })}
            options={categories.map(c => ({ 
              value: c.id, 
              label: `${c.libelle} - ${formatPrice(c.prixUnitaire)}` 
            }))}
            required
          />

          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Quantité"
              type="number"
              min={1}
              value={form.quantite}
              onChange={(e) => setForm({ ...form, quantite: Math.max(1, parseInt(e.target.value) || 1) })}
              required
            />
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">Total</label>
              <div className="px-3 py-2.5 bg-gray-50 rounded-lg border border-gray-200 text-lg font-bold text-[#1E3A5F]">
                {formatPrice(montantTotal)}
              </div>
            </div>
          </div>

          <Select
            label="Mode de paiement"
            name="modePaiement"
            value={form.modePaiement}
            onChange={(e) => setForm({ ...form, modePaiement: e.target.value })}
            options={[
              { value: 'ORANGE_MONEY', label: 'Orange Money' },
              { value: 'MTN_MOBILE_MONEY', label: 'MTN Mobile Money' },
              { value: 'CASH', label: 'Cash (en agence)' },
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

          <div className="bg-gray-50 p-4 rounded-lg text-sm text-gray-500 flex items-start gap-2">
            <Calendar size={16} className="text-orange-500 flex-shrink-0 mt-0.5" />
            <span>Cette réservation sera annulée automatiquement si le paiement n'est pas confirmé sous 30 minutes.</span>
          </div>

          <Button type="submit" variant="orange" fullWidth loading={loading} size="lg">
            <CreditCard size={18} /> Continuer vers le paiement
          </Button>
        </form>
      </Card>
    </div>
  );
};

export default NouvelleReservationPage;