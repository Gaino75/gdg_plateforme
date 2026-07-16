// Approvisionnement 

import React, { useState, useEffect } from 'react';
import { Package, Truck, FileText } from 'lucide-react';
import Input from '../ui/Input';
import Select from '../ui/Select';
import Button from '../ui/Button';
import Alert from '../ui/Alert';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';

const ApprovisionnementForm = ({ agenceId, onSubmit, loading = false }) => {
  const [categories, setCategories] = useState([]);
  const [form, setForm] = useState({
    categorieProduitId: '',
    quantite: 1,
    fournisseur: '',
    numeroBonLivraison: '',
    observations: '',
  });
  const [error, setError] = useState('');

  useEffect(() => {
    fetchCategories();
  }, []);

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
      setError('Veuillez remplir tous les champs obligatoires');
      return;
    }
    onSubmit({ ...form, agenceId });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}

      <Select
        label="Catégorie de produit *"
        value={form.categorieProduitId}
        onChange={(e) => setForm({ ...form, categorieProduitId: e.target.value })}
        options={categories.map(c => ({ value: c.id, label: c.libelle }))}
        required
      />

      <div className="grid grid-cols-2 gap-4">
        <Input
          label="Quantité *"
          type="number"
          min={1}
          value={form.quantite}
          onChange={(e) => setForm({ ...form, quantite: Math.max(1, parseInt(e.target.value) || 1) })}
          required
          icon={Package}
        />
        <Input
          label="Fournisseur"
          value={form.fournisseur}
          onChange={(e) => setForm({ ...form, fournisseur: e.target.value })}
          placeholder="Nom du fournisseur"
          icon={Truck}
        />
      </div>

      <Input
        label="Numéro de bon de livraison"
        value={form.numeroBonLivraison}
        onChange={(e) => setForm({ ...form, numeroBonLivraison: e.target.value })}
        placeholder="N° BL-2026-001"
        icon={FileText}
      />

      <Input
        label="Observations"
        value={form.observations}
        onChange={(e) => setForm({ ...form, observations: e.target.value })}
        placeholder="Informations complémentaires..."
      />

      <Button type="submit" variant="primary" fullWidth loading={loading}>
        <Truck size={18} /> Valider l'approvisionnement
      </Button>
    </form>
  );
};

export default ApprovisionnementForm;