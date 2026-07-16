// Formulaire enregistrer une vente

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ShoppingCart, CreditCard, User, Phone } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Select from '../../components/ui/Select';
import Loader from '../../components/ui/Loader';
import Alert from '../../components/ui/Alert';
import { formatPrice } from '../../utils/formatters';

const EnregistrerVentePage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  const [form, setForm] = useState({
    categorieProduitId: '',
    quantite: 1,
    prixUnitaire: 0,
    modePaiement: 'CASH',
    typeVente: 'HORS_LIGNE',
    nomClient: '',
    telephoneClient: '',
    observations: '',
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
      };

      const response = await axiosInstance.post(API.VENTES.BASE, data);
      setSuccess(true);
      
      setTimeout(() => {
        navigate(ROUTES.DISTRIBUTOR_HISTORIQUE);
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de l\'enregistrement');
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="max-w-md mx-auto text-center py-10">
        <div className="bg-green-50 p-6 rounded-xl">
          <ShoppingCart size={48} className="text-green-500 mx-auto mb-4" />
          <h2 className="text-xl font-bold text-gray-900">Vente enregistrée !</h2>
          <p className="text-gray-500 mt-2">La vente a été enregistrée avec succès.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Enregistrer une vente</h1>
        <p className="text-gray-500">Vente au comptoir — cash ou mobile money</p>
      </div>

      <Card>
        {error && <Alert type="error" message={error} className="mb-4" />}

        <form onSubmit={handleSubmit} className="space-y-4">
          <Select
            label="Catégorie de produit"
            name="categorieProduitId"
            value={form.categorieProduitId}
            onChange={(e) => setForm({ ...form, categorieProduitId: e.target.value })}
            options={categories.map(c => ({ 
              value: c.id, 
              label: c.libelle 
            }))}
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
            name="modePaiement"
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
            />
            <Input
              label="Téléphone"
              value={form.telephoneClient}
              onChange={(e) => setForm({ ...form, telephoneClient: e.target.value })}
              placeholder="Téléphone"
            />
          </div>

          <Button type="submit" variant="orange" fullWidth loading={loading} size="lg">
            <ShoppingCart size={18} /> Valider la vente
          </Button>
        </form>
      </Card>
    </div>
  );
};

export default EnregistrerVentePage;