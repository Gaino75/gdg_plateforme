// Reservation 

import React, { useState, useEffect } from 'react';
import { Fuel, Calendar, CreditCard } from 'lucide-react';
import Input from '../ui/Input';
import Select from '../ui/Select';
import Button from '../ui/Button';
import Alert from '../ui/Alert';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import { formatPrice } from '../../utils/formatters';

const ReservationForm = ({ agenceId, utilisateurId, onSubmit, loading = false }) => {
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState({
    categorieProduitId: '',
    quantite: 1,
    modePaiement: 'ORANGE_MONEY',
    numeroTelephone: '',
  });
  const [prixUnitaire, setPrixUnitaire] = useState(0);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    if (form.categorieProduitId && categories.length > 0) {
      const cat = categories.find(c => c.id === parseInt(form.categorieProduitId));
      setPrixUnitaire(cat?.prixUnitaire || 0);
    }
  }, [form.categorieProduitId, categories]);

  const fetchCategories = async () => {
    try {
      const response = await axiosInstance.get(API.STOCK.CATEGORIES);
      setCategories(response.data || []);
    } catch (error) {
      console.error('Erreur de chargement', error);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    if (!form.categorieProduitId || !form.quantite) {
      setError('Veuillez remplir tous les champs');
      return;
    }
    onSubmit({
      agenceId,
      consommateurId: utilisateurId,
      ...form,
      categorieProduitId: parseInt(form.categorieProduitId),
      quantite: parseInt(form.quantite),
      prixUnitaire,
      montantTotal: prixUnitaire * parseInt(form.quantite),
    });
  };

  const montantTotal = prixUnitaire * (form.quantite || 1);

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}

      <Select
        label="Catégorie de produit"
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

      <Button type="submit" variant="orange" fullWidth loading={loading} className="gap-2">
        <CreditCard size={18} /> Confirmer la réservation
      </Button>
    </form>
  );
};

export default ReservationForm;