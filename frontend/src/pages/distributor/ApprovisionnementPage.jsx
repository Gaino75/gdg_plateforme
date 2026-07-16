// Enregistrer un approvisionnement

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Package, Truck, Calendar } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';
import Card from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Select from '../../components/ui/Select';
import Loader from '../../components/ui/Loader';
import Alert from '../../components/ui/Alert';

const ApprovisionnementPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  const [form, setForm] = useState({
    categorieProduitId: '',
    quantite: 1,
    fournisseur: '',
    numeroBonLivraison: '',
    observations: '',
  });

  useEffect(() => {
    fetchCategories();
  }, []);

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
      await axiosInstance.post(API.STOCK.APPROVISIONNER, {
        ...form,
        agenceId: user.agenceId,
        distributeurId: user.id,
        categorieProduitId: parseInt(form.categorieProduitId),
        quantite: parseInt(form.quantite),
      });

      setSuccess(true);
      setTimeout(() => {
        navigate(ROUTES.DISTRIBUTOR_STOCK);
      }, 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Erreur lors de l\'approvisionnement');
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="max-w-md mx-auto text-center py-10">
        <div className="bg-green-50 p-6 rounded-xl">
          <Package size={48} className="text-green-500 mx-auto mb-4" />
          <h2 className="text-xl font-bold text-gray-900">Approvisionnement enregistré !</h2>
          <p className="text-gray-500 mt-2">Le stock a été mis à jour avec succès.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Approvisionnement</h1>
        <p className="text-gray-500">Enregistrer un nouvel approvisionnement</p>
      </div>

      <Card>
        {error && <Alert type="error" message={error} className="mb-4" />}

        <form onSubmit={handleSubmit} className="space-y-4">
          <Select
            label="Catégorie de produit"
            name="categorieProduitId"
            value={form.categorieProduitId}
            onChange={(e) => setForm({ ...form, categorieProduitId: e.target.value })}
            options={categories.map(c => ({ value: c.id, label: c.libelle }))}
            required
          />

          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Quantité reçue"
              type="number"
              min={1}
              value={form.quantite}
              onChange={(e) => setForm({ ...form, quantite: Math.max(1, parseInt(e.target.value) || 1) })}
              required
            />
            <Input
              label="Fournisseur"
              value={form.fournisseur}
              onChange={(e) => setForm({ ...form, fournisseur: e.target.value })}
              placeholder="Nom du fournisseur"
            />
          </div>

          <Input
            label="Numéro de bon de livraison"
            value={form.numeroBonLivraison}
            onChange={(e) => setForm({ ...form, numeroBonLivraison: e.target.value })}
            placeholder="N° BL-2026-001"
          />

          <Input
            label="Observations (optionnel)"
            value={form.observations}
            onChange={(e) => setForm({ ...form, observations: e.target.value })}
            placeholder="Informations complémentaires..."
          />

          <Button type="submit" variant="primary" fullWidth loading={loading} size="lg" className="gap-2">
            <Truck size={18} /> Valider l'approvisionnement
          </Button>
        </form>
      </Card>
    </div>
  );
};

export default ApprovisionnementPage;