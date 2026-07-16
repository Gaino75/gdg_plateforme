// Inscription consommateur

import React, { useState } from 'react';
import { User, Mail, Phone, MapPin, Lock, ArrowRight } from 'lucide-react';
import Input from '../ui/Input';
import Button from '../ui/Button';
import Alert from '../ui/Alert';

const RegisterConsumerForm = ({ onSubmit, loading = false }) => {
  const [form, setForm] = useState({
    nom: '',
    prenom: '',
    email: '',
    telephone: '',
    motDePasse: '',
    villeResidence: '',
    dateNaissance: '',
  });
  const [accepteConditions, setAccepteConditions] = useState(false);
  const [error, setError] = useState('');

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
        label="Ville de résidence"
        value={form.villeResidence}
        onChange={(e) => setForm({ ...form, villeResidence: e.target.value })}
        placeholder="Dschang"
        icon={MapPin}
      />

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

export default RegisterConsumerForm;