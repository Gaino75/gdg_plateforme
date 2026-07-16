// Inscription distributeur

import React, { useState, useEffect } from 'react';
import { User, Mail, Phone, Store, MapPin, Lock, ArrowRight } from 'lucide-react';
import Input from '../ui/Input';
import Select from '../ui/Select';
import Button from '../ui/Button';
import Alert from '../ui/Alert';
import axiosInstance from '../../services/axiosInstance';
import { API } from '../../constants/api';

const RegisterDistributorForm = ({ onSubmit, loading = false }) => {
  const [enseignes, setEnseignes] = useState([]);
  const [form, setForm] = useState({
    nom: '',
    prenom: '',
    email: '',
    telephone: '',
    motDePasse: '',
    nomAgence: '',
    villeAgence: '',
    enseigneId: '',
  });
  const [accepteConditions, setAccepteConditions] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchEnseignes();
  }, []);

  const fetchEnseignes = async () => {
    try {
      const response = await axiosInstance.get(API.AGENCES.ENSEIGNES);
      setEnseignes(response.data || []);
    } catch (error) {
      console.error('Erreur de chargement', error);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    if (!accepteConditions) {
      setError('Merci d\'accepter les conditions d\'utilisation');
      return;
    }
    if (form.motDePasse.length < 8) {
      setError('Le mot de passe doit contenir au moins 8 caractères');
      return;
    }
    onSubmit(form);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {error && <Alert type="error" message={error} />}

      <div className="grid grid-cols-2 gap-3">
        <Input
          label="Nom"
          value={form.nom}
          onChange={(e) => setForm({ ...form, nom: e.target.value })}
          placeholder="Kamdem"
          required
          icon={User}
        />
        <Input
          label="Prénom"
          value={form.prenom}
          onChange={(e) => setForm({ ...form, prenom: e.target.value })}
          placeholder="Éric"
          required
          icon={User}
        />
      </div>

      <Input
        label="Adresse email"
        type="email"
        value={form.email}
        onChange={(e) => setForm({ ...form, email: e.target.value })}
        placeholder="vous@exemple.com"
        required
        icon={Mail}
      />

      <Input
        label="Téléphone"
        value={form.telephone}
        onChange={(e) => setForm({ ...form, telephone: e.target.value })}
        placeholder="+237 6XX XXX XXX"
        required
        icon={Phone}
      />

      <Input
        label="Nom de l'agence"
        value={form.nomAgence}
        onChange={(e) => setForm({ ...form, nomAgence: e.target.value })}
        placeholder="Total Bafoussam Centre"
        required
        icon={Store}
      />

      <Input
        label="Ville de l'agence"
        value={form.villeAgence}
        onChange={(e) => setForm({ ...form, villeAgence: e.target.value })}
        placeholder="Bafoussam"
        required
        icon={MapPin}
      />

      <Select
        label="Enseigne"
        value={form.enseigneId}
        onChange={(e) => setForm({ ...form, enseigneId: e.target.value })}
        options={enseignes.map(e => ({ value: e.id, label: e.nom }))}
        required
      />

      <p className="text-xs text-amber-700 bg-amber-50 rounded-lg px-3 py-2">
        Votre agence sera visible après validation par un administrateur GDG.
      </p>

      <Input
        label="Mot de passe"
        type="password"
        value={form.motDePasse}
        onChange={(e) => setForm({ ...form, motDePasse: e.target.value })}
        placeholder="8 caractères minimum"
        required
        icon={Lock}
      />

      <label className="flex items-center gap-2 text-sm text-gray-600">
        <input
          type="checkbox"
          checked={accepteConditions}
          onChange={(e) => setAccepteConditions(e.target.checked)}
        />
        J'accepte les <span className="text-[#FF6B35] font-medium">conditions d'utilisation</span>
      </label>

      <Button type="submit" variant="orange" fullWidth loading={loading} className="gap-2">
        Créer mon compte <ArrowRight size={18} />
      </Button>
    </form>
  );
};

export default RegisterDistributorForm;