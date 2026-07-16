// Creer/modifier agence 

import React, { useState, useEffect } from 'react';
import { Store, MapPin, Phone, Mail } from 'lucide-react';
import Input from '../ui/Input';
import Select from '../ui/Select';
import Button from '../ui/Button';
import Textarea from '../ui/Textarea';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';

const AgenceForm = ({ initialData, onSubmit, loading = false, isEdit = false }) => {
  const [enseignes, setEnseignes] = useState([]);
  const [villes, setVilles] = useState([]);
  const [form, setForm] = useState({
    nom: initialData?.nom || '',
    adresse: initialData?.adresse || '',
    latitude: initialData?.latitude || '',
    longitude: initialData?.longitude || '',
    telephone: initialData?.telephone || '',
    email: initialData?.email || '',
    enseigneId: initialData?.enseigneId || '',
    villeId: initialData?.villeId || '',
    enteteFacture: initialData?.enteteFacture || '',
    piedFacture: initialData?.piedFacture || '',
  });
  const [error, setError] = useState('');

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [enseignesRes, villesRes] = await Promise.all([
        axiosInstance.get(API.AGENCES.ENSEIGNES),
        axiosInstance.get(API.AGENCES.VILLES),
      ]);
      setEnseignes(enseignesRes.data || []);
      setVilles(villesRes.data || []);
    } catch (error) {
      console.error('Erreur de chargement', error);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    if (!form.nom || !form.adresse || !form.enseigneId || !form.villeId) {
      setError('Veuillez remplir tous les champs obligatoires');
      return;
    }
    onSubmit(form);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}

      <Input
        label="Nom de l'agence *"
        value={form.nom}
        onChange={(e) => setForm({ ...form, nom: e.target.value })}
        placeholder="Total Bafoussam Centre"
        required
        icon={Store}
      />

      <Input
        label="Adresse *"
        value={form.adresse}
        onChange={(e) => setForm({ ...form, adresse: e.target.value })}
        placeholder="Route de la gare, Bafoussam"
        required
        icon={MapPin}
      />

      <div className="grid grid-cols-2 gap-4">
        <Input
          label="Latitude"
          value={form.latitude}
          onChange={(e) => setForm({ ...form, latitude: e.target.value })}
          placeholder="5.473"
        />
        <Input
          label="Longitude"
          value={form.longitude}
          onChange={(e) => setForm({ ...form, longitude: e.target.value })}
          placeholder="10.417"
        />
      </div>

      <div className="grid grid-cols-2 gap-4">
        <Input
          label="Téléphone"
          value={form.telephone}
          onChange={(e) => setForm({ ...form, telephone: e.target.value })}
          placeholder="+237 6XX XXX XXX"
          icon={Phone}
        />
        <Input
          label="Email"
          type="email"
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
          placeholder="contact@agence.com"
          icon={Mail}
        />
      </div>

      <Select
        label="Enseigne *"
        value={form.enseigneId}
        onChange={(e) => setForm({ ...form, enseigneId: e.target.value })}
        options={enseignes.map(e => ({ value: e.id, label: e.nom }))}
        required
      />

      <Select
        label="Ville *"
        value={form.villeId}
        onChange={(e) => setForm({ ...form, villeId: e.target.value })}
        options={villes.map(v => ({ value: v.id, label: v.nom }))}
        required
      />

      <Textarea
        label="En-tête de facture"
        value={form.enteteFacture}
        onChange={(e) => setForm({ ...form, enteteFacture: e.target.value })}
        placeholder="Total Bafoussam Centre — Distribution agréée"
        rows={2}
      />

      <Textarea
        label="Pied de facture"
        value={form.piedFacture}
        onChange={(e) => setForm({ ...form, piedFacture: e.target.value })}
        placeholder="Merci de votre confiance"
        rows={2}
      />

      <Button type="submit" variant="primary" fullWidth loading={loading}>
        {isEdit ? 'Mettre à jour' : 'Créer l\'agence'}
      </Button>
    </form>
  );
};

export default AgenceForm;