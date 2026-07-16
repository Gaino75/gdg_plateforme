// Enregistrer une vente 

import React, { useState, useEffect } from 'react';
import { ShoppingCart, User, Phone } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Button from '../ui/Button';
import Input from '../ui/Input';
import Select from '../ui/Select';
import Alert from '../ui/Alert';
import { formatPrice } from '../../utils/formatters';

const VenteForm = ({ onSuccess }) => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState('');
  const [form, setForm] = useState({
    categorieProduitId: '',
    quantite: 1,
    prixUnitaire: 0,
    modePaiement: 'CASH',
    nomClient: '',
    telephoneClient: '',
  });

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    if (form.categorieProduitId && categories.length > 0) {
      const cat = categories.find(c => c.id === parseInt(form.categorieProduitId));
      setForm(prev => ({ ...prev, prixUnitaire: cat?.prixUnitaire || 0 }));
    }
  }, [form.categorieProduitId, categories]);

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
        ...form,
        agenceId: user.agenceId,
        distributeurId: user.id,
        categorieProduitId: parseInt(form.categorieProduitId),
        quantite: parseInt(form.quantite),
        prixUnitaire: parseFloat(form.prixUnitaire),
        prixTotal: parseFloat(form.prixUnitaire) * parseInt(form.quantite),
        typeVente: 'HORS_LIGNE',
      };

      await axiosInstance.post(API.VENTES.BASE, data);
      if (onSuccess) onSuccess();
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de l\'enregistrement');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}

      <Select
        label="Catégorie de produit"
        value={form.categorieProduitId}
        onChange={(e) => setForm({ ...form, categorieProduitId: e.target.value })}
        options={categories.map(c => ({ value: c.id, label: c.libelle }))}
        required
      />

      <div className="grid grid-cols-2 gap-4">
        <Input
          label="Prix unitaire (FCFA)"
          type="number"
          value={form.prixUnitaire}
          onChange={(e) => setForm({ ...form, prixUnitaire: parseFloat(e.target.value) || 0 })}
          required
        />
        <Input
          label="Quantité"
          type="number"
          min={1}
          value={form.quantite}
          onChange={(e) => setForm({ ...form, quantite: Math.max(1, parseInt(e.target.value) || 1) })}
          required
        />
      </div>

      <div className="bg-gray-50 p-4 rounded-lg">
        <div className="flex justify-between items-center">
          <span className="text-gray-600">Total</span>
          <span className="text-xl font-bold text-[#1E3A5F]">
            {formatPrice(form.prixUnitaire * form.quantite)}
          </span>
        </div>
      </div>

      <Select
        label="Mode de paiement"
        value={form.modePaiement}
        onChange={(e) => setForm({ ...form, modePaiement: e.target.value })}
        options={[
          { value: 'CASH', label: 'Cash' },
          { value: 'ORANGE_MONEY', label: 'Orange Money' },
          { value: 'MTN_MOBILE_MONEY', label: 'MTN Mobile Money' },
        ]}
        required
      />

      <div className="grid grid-cols-2 gap-4">
        <Input
          label="Client (optionnel)"
          value={form.nomClient}
          onChange={(e) => setForm({ ...form, nomClient: e.target.value })}
          placeholder="Nom du client"
          icon={User}
        />
        <Input
          label="Téléphone"
          value={form.telephoneClient}
          onChange={(e) => setForm({ ...form, telephoneClient: e.target.value })}
          placeholder="Téléphone"
          icon={Phone}
        />
      </div>

      <Button type="submit" variant="orange" fullWidth loading={loading}>
        <ShoppingCart size={18} /> Valider la vente
      </Button>
    </form>
  );
};

export default VenteForm;